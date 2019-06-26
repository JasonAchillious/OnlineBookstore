using System;
using System.ComponentModel;
using System.Threading.Tasks;
using Windows.UI.Xaml;
using Windows.UI.Xaml.Controls;
using Windows.UI.Xaml.Media.Animation;


namespace Frontend.CustomControls
{
	public sealed partial class BookCollectionControl : UserControl, INotifyPropertyChanged
	{
		public BookCollectionControl(BookDetailCollection collection, bool isBillboard)
		{
			this.BookCollection = collection;
			this.IsBillboard = isBillboard;
			this.WaitLoading();
			this.InitializeComponent();
		}

		public event PropertyChangedEventHandler PropertyChanged;

		private void OnPropertyChanged()
		{
			PropertyChanged?.Invoke(this, new PropertyChangedEventArgs("UserInfoVisibility"));
			PropertyChanged?.Invoke(this, new PropertyChangedEventArgs("DescriptionVisibility"));
		}

		private async void WaitLoading()
		{
			while (BookCollection.Books.Count == 0)
				await Task.Delay(Util.REFRESH_RATE);

			while (true)
			{
				if (BookCollection.Finished)
				{
					break;
				}
				else
				{
					await Task.Delay(Util.REFRESH_RATE);
					this.OnPropertyChanged();
				}
			}
			await Task.Delay(Util.REFRESH_RATE * 2);
			this.OnPropertyChanged();
		}

		public BookDetailCollection BookCollection { set; get; }

		public Action RefreshOverride { set; get; } = null;

		public int PaddingX { set; get; }

		public bool IsBillboard { set; get; }

		private bool IsReadList { get => !this.IsBillboard && this.BookCollection != null &&
											this.BookCollection.FollowAmount > 0; }
		private Thickness OutPadding { get => new Thickness(this.PaddingX, 0, this.PaddingX, 0); }
		private Visibility UserInfoVisibility { get => this.IsReadList.ToVisibility(); }
		private Visibility DescriptionVisibility {
			get => (this.BookCollection.Description != null &&
					this.BookCollection.Description.Length > 1).ToVisibility();
		}

		/// <summary>
		/// Navigate to detail page
		/// </summary>
		private void BookGrid_ItemClick(object sender, ItemClickEventArgs e)
		{
			var dataToPass = e.ClickedItem as BookDetail;
			if (NetworkGet.IsValidID(dataToPass.ID))
			{
				bookGrid.PrepareConnectedAnimation(Util.TO_BOOK_DETAIL, dataToPass, "bookCover");
				this._navigateItem = dataToPass;
				Util.MainElem.NavigateToBookDetail(dataToPass, typeof(BookDetailPage));
			}
		}

		private BookDetail _navigateItem = null;

		/// <summary>
		/// Navigate back from detail page
		/// </summary>
		private async void BookGrid_Loaded(object sender, RoutedEventArgs e)
		{
			if (this._navigateItem == null)
				return;
			var animation = ConnectedAnimationService.GetForCurrentView().GetAnimation(Util.FROM_BOOK_DETAIL);
			if (animation != null)
			{
				animation.Configuration = new DirectConnectedAnimationConfiguration();
				bookGrid.ScrollIntoView(this._navigateItem);
				await bookGrid.TryStartConnectedAnimationAsync(animation, this._navigateItem, "bookCover");
			}
		}

		internal void RefreshPage(bool addBooks = false)
		{
			this.WaitLoading();
			if (addBooks)
			{
				this.BookCollection.AddBooks();
			}
			else
			{
				_ = this.BookCollection.ReloadBooks();
			}
		}

		private void RefreshRequested(RefreshContainer sender, RefreshRequestedEventArgs args)
		{
			if (this.RefreshOverride == null)
				RefreshPage(true);
			else
				this.RefreshOverride();
		}

		private async void UserControl_Loading(FrameworkElement sender, object args)
		{
			await System.Threading.Tasks.Task.Delay(500);
		}
	}
}
