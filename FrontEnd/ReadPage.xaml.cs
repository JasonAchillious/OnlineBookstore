using Microsoft.Graphics.Canvas;
using Microsoft.Graphics.Canvas.UI.Xaml;
using System;
using System.Collections.Generic;
using System.Collections.ObjectModel;
using System.Diagnostics;
using System.IO;
using System.Linq;
using System.Net.Http;
using System.Numerics;
using System.Threading.Tasks;
using Windows.Data.Pdf;
using Windows.Foundation;
using Windows.Storage;
using Windows.Storage.Streams;
using Windows.UI.Xaml.Controls;
using Windows.UI.Xaml.Media.Imaging;
using Windows.UI.Xaml.Navigation;

// https://go.microsoft.com/fwlink/?LinkId=234238 上介绍了“空白页”项模板

namespace Frontend
{
	/// <summary>
	/// 可用于自身或导航至 Frame 内部的空白页。
	/// </summary>
	public sealed partial class ReadPage : Page, IRefreshAdminInterface, ISendDanmuInterface
	{
		public ReadPage()
		{
			this.InitializeComponent();

			this.bulletScreen = new BulletScreen();
			this.bulletPool = new List<LiveComment>();
		}

		protected override void OnNavigatedTo(NavigationEventArgs e)
		{
			base.OnNavigatedTo(e);
			var (key, url, bookId) = ((string key, string url, int bookId))e.Parameter;
			if (this.bookId == bookId)
				return;
			this.privateKey = key;
			this.bookId = bookId;
			this.Source = new Uri(url);
			this.LoadAsync();
		}

		private int bookId = -1;
		private string privateKey;

		private Uri Source { set; get; }
		private ObservableCollection<BitmapImage> PdfPages {
			get;
			set;
		} = new ObservableCollection<BitmapImage>();


		private async void LoadAsync()
		{
			if (Source == null)
			{
				PdfPages.Clear();
			}
			else
			{
				PdfDocument doc;
				if (Source.IsFile || !Source.IsWebUri())
				{
					doc = await LoadFromLocalAsync();
				}
				else if (Source.IsWebUri())
				{
					doc = await LoadFromRemoteAsync();
				}
				else
				{
					Debug.WriteLine($"Source '{Source.ToString()}' could not be recognized!");
					return;
				}
				LoadBulletsAsync(doc.PageCount);
			}
		}

		private async void LoadBulletsAsync(uint maxPageNum)
		{
			for (uint i = 1; i <= maxPageNum; ++i)
			{
				var collection = new DanmuCollection(this.bookId, i);
				await collection.Reload();
				this.bulletPool.AddRange(collection.Danmus.Select(d => new LiveComment(d, i)));
			}
		}

		private async Task<PdfDocument> LoadFromRemoteAsync()
		{
			MemoryStream memStream;
			while (true)
			{
				try
				{
					HttpClient client = new HttpClient();
					var stream = await client.GetStreamAsync(Source);
					memStream = new MemoryStream();
					await stream.CopyToAsync(memStream);
				}
				catch (Exception)
				{
					continue;
				}
				break;
			}
			memStream.Position = 0;
			var doc = await PdfDocument.LoadFromStreamAsync(memStream.AsRandomAccessStream(), privateKey);

			Load(doc);
			return doc;
		}

		private async Task<PdfDocument> LoadFromLocalAsync()
		{
			var f = await StorageFile.GetFileFromApplicationUriAsync(Source);
			var doc = await PdfDocument.LoadFromFileAsync(f, privateKey);

			Load(doc);
			return doc;
		}

		private async void Load(PdfDocument pdfDoc)
		{
			PdfPages.Clear();
			this.LoadingControl(pdfDoc.PageCount);
			for (uint i = 0; i < pdfDoc.PageCount; i++)
			{
				BitmapImage image = new BitmapImage();

				var page = pdfDoc.GetPage(i);

				using (InMemoryRandomAccessStream stream = new InMemoryRandomAccessStream())
				{
					await page.RenderToStreamAsync(stream);
					await image.SetSourceAsync(stream);
				}

				PdfPages.Add(image);
			}
		}

		private async void LoadingControl(uint pageCount, uint preloadCount = 10)
		{
			loadingControl.IsLoading = true;
			while (PdfPages.Count < Math.Min(preloadCount, pageCount))
			{
				await Task.Delay(Util.REFRESH_RATE);
			}
			loadingControl.IsLoading = false;
		}

		public void RefreshButtonPressed()
		{
			this.LoadAsync();
		}

		public void AdminButtonPressed(bool isChecked)
		{
			if (isChecked)
				Util.OpenTerminal();
		}

		private readonly BulletScreen bulletScreen;

		private readonly List<LiveComment> bulletPool;

		/// <summary>
		/// Load comments according to the playback position
		/// </summary>
		private void LoadComments(uint pageNumNow)
		{
			// Get all the avaliable bullets's id.
			var idList = bulletScreen.Bullets.ToList().
											  Where(o => !o.IsObsolete).
											  Select(o => o.CommentItem.ID).
											  ToList();
			// Get comments need to load to screen from the comment pool.
			var list = this.bulletPool.Where(o => o.PageNum == pageNumNow && !idList.Contains(o.ID));

			// default positon is top right.
			Vector2 startPosition = new Vector2(1, 0);
			// If there's any comment need to load to screen
			if (list.Count() > 0)
			{
				// Animated control's actual size
				Size ControlSize = this.animatedControl.Size;
				// Get last bullet in the bullet queue
				CommentBullet lastBullet;
				IList<CommentBullet> bulletsNow = bulletScreen.Bullets.ToList();
				if (bulletsNow.Count > 0)
					lastBullet = bulletsNow.LastOrDefault();
				else
					lastBullet = null;

				foreach (LiveComment comment in list)
				{
					// set start position's y value to last bullet's y value
					if (lastBullet != null && !lastBullet.IsObsolete)
					{
						startPosition.Y = lastBullet.Position.Y;
					}
					// add comment's height
					startPosition.Y += comment.Height / (float)ControlSize.Height;
					// If y value is overflow, set new bullet to top.
					if (startPosition.Y > 1)
					{
						startPosition.Y = comment.Height / (float)ControlSize.Height;
					}
					// Add bullet to bullet screen
					CommentBullet bullet = new CommentBullet(
									new Vector2(startPosition.X, startPosition.Y),
									comment);
					bulletScreen.Bullets.Add(bullet);
					lastBullet = bullet;
				}
			}
		}

		/// <summary>
		/// Set all the bullets in the bullet screen to obsolete
		/// Reload comments to bullet screen		
		/// </summary>
		private void ReloadComments(uint pagePos)
		{
			foreach (var b in bulletScreen.Bullets.ToList())
			{
				b.IsObsolete = true;
			}
			LoadComments(pagePos);
		}

		/// <summary>
		/// Draw bullets
		/// </summary>
		private void AnimatedControl_Draw(ICanvasAnimatedControl sender,
										  CanvasAnimatedDrawEventArgs args)
		{
			bulletScreen.Draw(args.DrawingSession, sender.Size);
		}

		/// <summary>
		/// Update bullets
		/// </summary>
		private void AnimatedControl_Update(ICanvasAnimatedControl sender,
											CanvasAnimatedUpdateEventArgs args)
		{
			bulletScreen.Update();
		}

		private void ScrollViewer_ViewChanged(object sender, ScrollViewerViewChangedEventArgs e)
		{
			if (e.IsIntermediate)
			{
			}
			else
			{
				ReloadComments(GetCurrentPageNum());
			}
		}

		private uint GetCurrentPageNum()
		{
			var yScroll = scroller.VerticalOffset;
			if (double.IsNaN(yScroll))
				yScroll = 0;
			var zoom = scroller.ZoomFactor;
			yScroll /= zoom;
			uint currentPagePos = (uint)PdfPages.Count;
			for (int i = 1; i <= PdfPages.Count; ++i)
			{
				var pageHeight = (double)PdfPages[i - 1].PixelHeight;
				pageHeight *= scroller.ViewportWidth / PdfPages[i - 1].PixelWidth;
				yScroll -= pageHeight + scroller.Margin.Top;
				if (yScroll <= 0f)
				{
					currentPagePos = (uint)i;
					break;
				}
			}
			return currentPagePos;
		}
		
		public async void SendDanmuPressed()
		{
			TextBox text = new TextBox
			{
				PlaceholderText = "The bullet content",
				Header = "Please input the bullet comment you want to send"
			};
			ContentDialog dialog = new ContentDialog
			{
				Content = text,
				Title = "Send your bullet",
				IsSecondaryButtonEnabled = true,
				PrimaryButtonText = "Send",
				SecondaryButtonText = "Cancle"
			};
			if (await dialog.ShowAsync() == ContentDialogResult.Primary)
			{
				if (text.Text.Length <= 2)
				{
					notification.Show("You cannot send a danmu with such short content", 4000);
				}
				else if (text.Text.Length >= 100)
				{
					notification.Show("You cannot send a danmu with such long content", 4000);
				}
				var page = GetCurrentPageNum();
				bool success = await NetworkSet.CreateDanmu(text.Text, bookId, page);
				if (success)
				{
					this.bulletPool.Add(new LiveComment(new Danmu(0) { Content = text.Text }, page));
					notification.Show($"Success in sending a bullet at page {page} \"{text.Text}\"", 4000);
				}
				else
				{
					notification.Show($"Something wrong in sending a bullet at page {page}. " +
										"Please try again later.", 4000);
				}
			}
		}
	}

	/// <summary>
	/// Bullet Screen
	/// draw and update bullet in bulk
	/// </summary>
	internal class BulletScreen
	{
		internal  SynchronizedCollection<CommentBullet> Bullets { set; get; }
		internal BulletScreen()
		{
			Bullets = new SynchronizedCollection<CommentBullet>();
		}

		/// <summary>
		/// Draw bullets
		/// </summary>
		internal void Draw(CanvasDrawingSession ds, Size size)
		{
			Matrix3x2 transform = Matrix3x2.CreateScale(size.ToVector2());
			// Before drawing the bullets, remove the discard bullet first.
			foreach (var b in Bullets.ToList())
			{
				if (b.IsObsolete)
					Bullets.Remove(b);
			}
			foreach (var b in Bullets.ToList())
			{
				b.Draw(ds, transform);
			}
		}

		/// <summary>
		/// Update bullets
		/// </summary>
		internal void Update()
		{
			foreach (var bullet in Bullets.ToList())
			{
				bullet.Update();
			}
		}
	}

	internal class CommentBullet
	{
		//tex:
		//Default speed $$v_\mathrm{default}=\frac{v_\mathrm{set}}{10000}$$
		//Changed according the comment width by
		//$$v_\mathrm{actual}=v_\mathrm{default}+\frac{l_\mathrm{comment}\cdot v_\mathrm{set}/20}{1000000}$$
		private readonly float MoveSpeed = Storage.DanmuSpeed / 10000;

		internal CommentBullet(Vector2 pos, LiveComment liveComment)
		{
			MoveSpeed += liveComment.Width * Storage.DanmuSpeed / 20 / 1000000;
			Position = pos;
			CommentItem = liveComment;
			IsObsolete = false;
		}

		internal LiveComment CommentItem { get; set; }
		internal Vector2 Position { get; set; }
		internal bool IsObsolete { get; set; }

		/// <summary>
		/// Draw the bullet
		/// </summary>
		internal void Draw(CanvasDrawingSession ds, Matrix3x2 transform)
		{
			var pos = Vector2.Transform(Position, transform);
			// Set center position of bullet
			var center = new Vector2(pos.X + CommentItem.Width / 2, pos.Y - CommentItem.Height / 2);
			//If bullet is out of screen, set to obsolete
			if ((pos.X + CommentItem.Width) < 0)
			{
				IsObsolete = true;
			}
			// Draw avaliable bullet
			if (!IsObsolete)
			{
				ds.DrawText(CommentItem.Comment, center, Storage.DanmuColor, Storage.DanmuTextFormat);
			}
		}

		/// <summary>
		/// move bullet to left 
		/// </summary>
		internal void Update()
		{
			Position = new Vector2(Position.X - MoveSpeed, Position.Y);
		}
	}

	internal class LiveComment
	{
		internal int ID { get; private set; }

		private readonly Danmu danmu;

		internal string Comment { get => this.danmu.Content; }
		internal uint PageNum { get; private set; }
		internal float Width { get => this.danmu.Content.Length * Storage.DanmuSize; }
		internal float Height { get => Storage.DanmuSize + Storage.DanmuSpacing; }

		internal LiveComment(Danmu danmu, uint pagenum)
		{
			this.ID = danmu.ID;
			this.danmu = danmu;
			this.PageNum = pagenum;
		}
	}
}
