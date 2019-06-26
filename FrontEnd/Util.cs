using Microsoft.Graphics.Canvas.Text;
using Newtonsoft.Json;
using QRCoder;
using System;
using System.Collections.Generic;
using System.Collections.ObjectModel;
using System.Text;
using System.Threading.Tasks;
using Windows.Storage.Streams;
using Windows.UI;
using Windows.UI.Xaml;
using Windows.UI.Xaml.Controls;
using Windows.UI.Xaml.Media.Imaging;


namespace Frontend
{
	internal static class Storage
	{
		internal static bool Test { get; } = false;

		internal static int UserId { set; get; } = -1;
		internal static Visibility SignUpVisibility { get => (!NetworkGet.IsValidID(UserId)).ToVisibility(); }
		internal static bool IsAdmin { set; get; } = false;
		internal static Color DanmuColor { set; get; } = Colors.Black;
		internal static float DanmuSize { set; get; } = 20;
		internal static float DanmuSpacing { set; get; } = 6;
		internal static float DanmuSpeed { set; get; } = 20;
		internal static bool IsDanmuOn { set; get; } = true;
		internal static Visibility DanmuVisibility { get => IsDanmuOn.ToVisibility(); }
		internal static CanvasTextFormat DanmuTextFormat { get; }
			= new CanvasTextFormat
			{
				HorizontalAlignment = CanvasHorizontalAlignment.Center,
				VerticalAlignment = CanvasVerticalAlignment.Center,
				Options = CanvasDrawTextOptions.NoPixelSnap
			};

		internal static ObservableCollection<Label> LABELS { set; get; }
	}

	internal static class Util
	{
		internal const int PREVIEW_AMOUNT = 8;
		internal const int INIT_AMOUNT = 14;
		internal const int ADD_AMOUNT = 6;
		internal const int RELATE_BOOK_AMOUNT = 7;
		internal const int REVIEW_AMOUNT_ONE_TIME = 4;

		internal const string TO_BOOK_DETAIL = "toDetail";
		internal const string FROM_BOOK_DETAIL = "fromDetail";

		internal const int REFRESH_RATE = 500;

		internal const string WAIT_STR = "Waiting...";

		internal static MainPage MainElem { set; get; }

		internal static Visibility ToVisibility(this bool visible)
		{
			return visible ? Visibility.Visible : Visibility.Collapsed;
		}

		internal static string SHA256(string data)
		{
			byte[] bytes = Encoding.UTF8.GetBytes(data);
			byte[] hash = System.Security.Cryptography.SHA256.Create().ComputeHash(bytes);

			StringBuilder builder = new StringBuilder();
			for (int i = 0; i < hash.Length; i++)
			{
				builder.Append(hash[i].ToString("X2"));
			}

			return builder.ToString();
		}

		internal const int LEVEL_DataTemplate = 10;

		internal static UIElement GetParentUpto(this UIElement elem, int level = 1)
		{
			if (level < 1)
				return elem;
			UIElement parent = Windows.UI.Xaml.Media.VisualTreeHelper.GetParent(elem) as UIElement;
			for (int i = 1; i < level; ++i)
			{
				parent = Windows.UI.Xaml.Media.VisualTreeHelper.GetParent(parent) as UIElement;
			}
			return parent;
		}

		internal static string WordSplit(this string s)
		{
			return System.Text.RegularExpressions.Regex.Replace(s, @"(\p{Lu})", " $1").TrimStart();
		}

		internal static string EnumToString(this Enum e)
		{
			return e.ToString("F").WordSplit();
		}

		internal static T CloneThroughJson<T>(this T source)
		{
			if (source == null)
			{
				return default;
			}
			var deserializeSettings = new JsonSerializerSettings
			{ ObjectCreationHandling = ObjectCreationHandling.Replace };
			return JsonConvert.DeserializeObject<T>
				(JsonConvert.SerializeObject(source), deserializeSettings);
		}

		internal static async Task<string> InputTextDialogAsync(string title, string placeholder,
																string previousContent)
		{
			TextBox inputTextBox = new TextBox
			{
				AcceptsReturn = false,
				TextWrapping = TextWrapping.Wrap,
				MinHeight = 32,
				MaxWidth = 650,
				Text = previousContent,
				PlaceholderText = placeholder
			};
			ContentDialog dialog = new ContentDialog
			{
				//HorizontalAlignment = HorizontalAlignment.Center,
				//VerticalAlignment = VerticalAlignment.Center,
				Content = inputTextBox,
				Title = title,
				IsSecondaryButtonEnabled = true,
				PrimaryButtonText = "Confirm",
				SecondaryButtonText = "Cancel"
			};
			if (await dialog.ShowAsync() == ContentDialogResult.Primary)
				return inputTextBox.Text;
			else
				return previousContent;
		}

		internal static async Task BuyBookAsync(int bookId, BookDetail book, ObservableCollection<BookDetail> books,
												Microsoft.Toolkit.Uwp.UI.Controls.InAppNotification notification)
		{
			string buyURL = await NetworkSet.BuyBook(bookId);
			if (buyURL == null || buyURL.Length <= 4)
				return;
			ContentDialog dialog = new ContentDialog()
			{
				Content = new Image()
				{
					Stretch = Windows.UI.Xaml.Media.Stretch.Uniform,
					Source = await buyURL.ToQRCode()
				},
				Title = "Buying Book",
				IsSecondaryButtonEnabled = true,
				PrimaryButtonText = "I've paid",
				SecondaryButtonText = "Cancel"
			};
			if (await dialog.ShowAsync() == ContentDialogResult.Primary)
			{ // click finish paying
				var finish = await NetworkSet.CheckBuyComplete(bookId);
				if (finish)
				{
					if (books != null)
						books.Remove(book);
					else
					{
						book.CanBuy = false;
						book.CanAddWishList = false;
					}
					notification.Show("Payment success, wish you enjoy reading", 4000);
				}
				else
				{
					notification.Show("Payment failure, please try again later", 4000);
				}
			}
			else
			{
				if (await NetworkSet.CancelTransaction(bookId))
					notification.Show("Transaction cancled", 4000);
				else
				{
					if (book != null)
					{
						book.CanBuy = false;
						book.CanAddWishList = false;
					}
					notification.Show("Transaction cannot be cancled since you have paid", 4000);
				}
			}
		}

		internal static async void OpenTerminal()
		{
			var terminal = new CustomControls.TerminalControl();
			ContentDialog dialog = new ContentDialog()
			{
				Content = terminal,
				Title = "Perform SQL Statements",
				IsSecondaryButtonEnabled = true,
				PrimaryButtonText = "Confirm",
				SecondaryButtonText = "Cancel"
			};
			if (await dialog.ShowAsync() == ContentDialogResult.Primary)
			{
				var recv = await NetworkAdmin.PerformSQL(terminal.SQLCode);
				if (recv.Contains("<html>"))
				{
					var web = new WebView(WebViewExecutionMode.SeparateThread)
					{
						Width = 800,
						Height = 600
					};
					web.NavigateToString(recv);
					dialog = new ContentDialog()
					{
						Content = web,
						Title = "Execute Result",
						IsSecondaryButtonEnabled = false,
						PrimaryButtonText = "OK"
					};
					dialog.Width = 850;
					await dialog.ShowAsync();
				}
				else
				{
					dialog = new ContentDialog()
					{
						Content = recv,
						Title = "Execute Result",
						IsSecondaryButtonEnabled = false,
						PrimaryButtonText = "OK"
					};
					await dialog.ShowAsync();
				}
			}
		}

		internal static async Task<BitmapImage> ToQRCode(this string str)
		{
			QRCodeGenerator qrGenerator = new QRCodeGenerator();
			QRCodeData qrCodeData = qrGenerator.CreateQrCode(str, QRCodeGenerator.ECCLevel.Q);

			//Create byte/raw bitmap qr code
			BitmapByteQRCode qrCodeBmp = new BitmapByteQRCode(qrCodeData);
			byte[] qrCodeImageBmp = qrCodeBmp.GetGraphic(12);
			InMemoryRandomAccessStream stream = new InMemoryRandomAccessStream();
			DataWriter writer = new DataWriter(stream.GetOutputStreamAt(0));
			writer.WriteBytes(qrCodeImageBmp);
			await writer.StoreAsync();
			var image = new BitmapImage();
			await image.SetSourceAsync(stream);
			return image;
		}

		internal static bool IsWebUri(this Uri uri)
		{
			if (uri != null)
			{
				var str = uri.ToString().ToLower();
				return str.StartsWith("http://") || str.StartsWith("https://");
			}
			return false;
		}

		private static readonly Dictionary<string, object> lockDict = new Dictionary<string, object>();
		private static readonly Random random = new Random();

		/// <summary>
		/// Global lock of any method
		/// </summary>
		/// <typeparam name="TIn">input type</typeparam>
		/// <typeparam name="TOut">output type</typeparam>
		/// <param name="func">an async method that actually dose computing</param>
		/// <param name="parameter">the parameter need to pass to the async method</param>
		/// <returns></returns>
		internal static async Task<TOut> GlobalLock<TIn, TOut>(this Func<TIn, Task<TOut>> func, TIn parameter)
		{
			await Task.Delay(random.Next(REFRESH_RATE / 5, REFRESH_RATE / 2));

			if (!lockDict.ContainsKey(func.Method.Name))
				lockDict.Add(func.Method.Name, null);

			while (lockDict[func.Method.Name] != null)
			{
				await Task.Delay(REFRESH_RATE / 2);
			}
			System.Diagnostics.Debug.WriteLine($"GL of {func.Method.Name} start:" +
												$"\t{DateTime.Now.Second}:{DateTime.Now.Millisecond}");
			lockDict[func.Method.Name] = new object();
			var result = await func(parameter);
			lockDict[func.Method.Name] = null;
			System.Diagnostics.Debug.WriteLine($"GL of {func.Method.Name} end:" +
												$"\t{DateTime.Now.Second}:{DateTime.Now.Millisecond}");
			return result;
		}

		/// <summary>
		/// time stap to date time
		/// </summary>
		/// <param name="TimeStamp">time stamp</param>
		/// <returns>DateTime</returns>
		internal static DateTime GetTime(this long TimeStamp)
		{
			DateTime startTime = new DateTime(1970, 1, 1); // 当地时区
			return startTime.AddSeconds(TimeStamp);
		}

		internal static string CutString(this string str, int maxLen = 38)
		{
			if (Encoding.UTF8.GetBytes(str).Length > maxLen)
			{
				double ratio = (double)Encoding.UTF8.GetBytes(str).Length / str.Length;
				int len = ratio > 1 ? (int)(maxLen / ratio) : maxLen;
				int pos = str.LastIndexOf(" ", len);
				if (pos < 0.8 * len)
				{
					pos = len;
				}
				return str.Substring(0, pos) + "…";
			}
			else
			{
				return str;
			}
		}
	}

	internal interface IRefreshAdminInterface
	{
		void RefreshButtonPressed();

		void AdminButtonPressed(bool isChecked);
	}

	internal interface ISendDanmuInterface
	{
		void SendDanmuPressed();
	}

	/// <summary>The Range class. From drharris on Stackoverflow</summary>
	/// <typeparam name="T">Generic parameter.</typeparam>
	internal class Range<T> where T : IComparable<T>
	{
		private readonly T inf = default;
		private readonly T minusInf = default;

		internal Range(T min, T max, T valueAsInf)
		{
			if (max.CompareTo(min) < 0)
				return;
			Minimum = min;
			Maximum = max;
			if (valueAsInf.CompareTo(max) >= 0)
			{
				this.inf = valueAsInf;
			}
			else if (valueAsInf.CompareTo(min) <= 0)
			{
				this.minusInf = valueAsInf;
			}
		}

		/// <summary>Minimum value of the range.</summary>
		internal T Minimum { get; set; }

		/// <summary>Maximum value of the range.</summary>
		internal T Maximum { get; set; }

		internal T[] ToArray()
		{
			if (this.Maximum.CompareTo(this.inf) == 0 && this.inf.CompareTo(default) > 0)
				return new T[] { this.Minimum, (T)typeof(T).GetField("MaxValue").GetValue(null) };
			else if (this.Minimum.CompareTo(this.minusInf) == 0 && this.minusInf.CompareTo(default) < 0)
				return new T[] { (T)typeof(T).GetField("MinValue").GetValue(null), this.Maximum };
			else
				return new T[] { this.Minimum, this.Maximum };
		}


		/// <summary>Presents the Range in readable format.</summary>
		/// <returns>String representation of the Range</returns>
		public override string ToString()
		{
			if (this.Maximum.CompareTo(this.inf) == 0 && this.inf.CompareTo(default) > 0)
				return string.Format("[{0} ~ ∞]", this.Minimum);
			else if (this.Minimum.CompareTo(this.minusInf) == 0 && this.minusInf.CompareTo(default) < 0)
				return string.Format("[-∞ ~ {0}]", this.Maximum);
			else
				return string.Format("[{0} ~ {1}]", this.Minimum, this.Maximum);
		}

		internal string ToFormalString()
		{
			if (this.Maximum.CompareTo(this.inf) == 0 && this.inf.CompareTo(default) > 0)
				return string.Format("({0},inf)", this.Minimum);
			else if (this.Minimum.CompareTo(this.minusInf) == 0 && this.minusInf.CompareTo(default) < 0)
				return string.Format("(-inf,{0})", this.Maximum);
			else
				return string.Format("({0},{1})", this.Minimum, this.Maximum);
		}

		/// <summary>Determines if the range is valid.</summary>
		/// <returns>True if range is valid, else false</returns>
		internal bool IsValid()
		{
			return this.Minimum.CompareTo(this.Maximum) <= 0;
		}

		/// <summary>Determines if the provided value is inside the range.</summary>
		/// <param name="value">The value to test</param>
		/// <returns>True if the value is inside Range, else false</returns>
		internal bool ContainsValue(T value)
		{
			return (this.Minimum.CompareTo(value) <= 0) && (value.CompareTo(this.Maximum) <= 0);
		}

		/// <summary>Determines if this Range is inside the bounds of another range.</summary>
		/// <param name="Range">The parent range to test on</param>
		/// <returns>True if range is inclusive, else false</returns>
		internal bool IsInsideRange(Range<T> range)
		{
			return this.IsValid() && range.IsValid() && range.ContainsValue(this.Minimum)
				&& range.ContainsValue(this.Maximum);
		}

		/// <summary>Determines if another range is inside the bounds of this range.</summary>
		/// <param name="Range">The child range to test</param>
		/// <returns>True if range is inside, else false</returns>
		internal bool ContainsRange(Range<T> range)
		{
			return this.IsValid() && range.IsValid() && this.ContainsValue(range.Minimum)
				&& this.ContainsValue(range.Maximum);
		}
	}
}
