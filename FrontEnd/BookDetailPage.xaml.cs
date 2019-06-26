using System;
using System.Collections.Generic;
using System.ComponentModel;
using Windows.UI.Xaml;
using Windows.UI.Xaml.Controls;
using Windows.UI.Xaml.Input;
using Windows.UI.Xaml.Media;
using Windows.UI.Xaml.Media.Animation;
using Windows.UI.Xaml.Navigation;

// https://go.microsoft.com/fwlink/?LinkId=234238 上介绍了“空白页”项模板

namespace Frontend
{
	/// <summary>
	/// 可用于自身或导航至 Frame 内部的空白页。
	/// </summary>
	public sealed partial class BookDetailPage : Page, INotifyPropertyChanged, IRefreshAdminInterface
	{
		public BookDetailPage()
		{
			this.InitializeComponent();
			//this.NavigationCacheMode = NavigationCacheMode.Enabled;
		}

		/// <summary>
		/// Navigate from list pages, etc.
		/// </summary>
		protected override void OnNavigatedTo(NavigationEventArgs e)
		{
			base.OnNavigatedTo(e);
			// load details first
			var bookSummary = (BookSummary)e.Parameter;
			detail = new BookDetail(bookSummary);
			Refresh();
			// start animations
			var animationService = ConnectedAnimationService.GetForCurrentView();
			var animation = animationService.GetAnimation(Util.TO_BOOK_DETAIL);
			if (animation != null)
			{
				animation.TryStart(bookCover, new UIElement[] { anchorGrid });
			}
		}

		/// <summary>
		/// Navigate back to list pages, etc.
		/// </summary>
		protected override void OnNavigatingFrom(NavigatingCancelEventArgs e)
		{
			if (e.NavigationMode == NavigationMode.Back)
			{
				var service = ConnectedAnimationService.GetForCurrentView();
				service.PrepareToAnimate(Util.FROM_BOOK_DETAIL, bookCover);
				// Use the recommended configuration for back animation.
				service.GetAnimation(Util.FROM_BOOK_DETAIL).Configuration =
					new DirectConnectedAnimationConfiguration();
			}
		}

		/// <summary>
		/// Navigate to another detail page
		/// </summary>
		private void StackPanel_PointerReleased(object sender, PointerRoutedEventArgs e)
		{
			var dataToPass = (BookSummary)((StackPanel)sender).DataContext;
			if (NetworkGet.IsValidID(dataToPass.ID))
			{
				relatedBookGrid.PrepareConnectedAnimation(Util.TO_BOOK_DETAIL, dataToPass, "relateBookImage");
				this._navigateItem = dataToPass;
				Util.MainElem.NavigateToBookDetail(dataToPass, typeof(BookDetailPage));
			}
		}

		private BookSummary _navigateItem = null;

		/// <summary>
		/// Navigate back from another detail page
		/// </summary>
		private async void RelatedBookGrid_Loaded(object sender, RoutedEventArgs e)
		{
			if (this._navigateItem == null)
				return;
			var animation = ConnectedAnimationService.GetForCurrentView().GetAnimation(Util.FROM_BOOK_DETAIL);
			if (animation != null)
			{
				animation.Configuration = new DirectConnectedAnimationConfiguration();
				relatedBookGrid.ScrollIntoView(this._navigateItem);
				await relatedBookGrid.TryStartConnectedAnimationAsync(animation,
																	  this._navigateItem, "relateBookImage");
			}
		}

		private BookDetail detail;
		private BookDetail Detail {
			get { return detail; }
			set { detail = value; OnPropertyChanged(); }
		}

		public event PropertyChangedEventHandler PropertyChanged;

		private void OnPropertyChanged()
		{
			PropertyChanged?.Invoke(this, new PropertyChangedEventArgs("Detail"));
		}

		internal static string PreviewOrRead(BookDetail detail)
		{
			return (!detail.CanBuy && Storage.UserId > 0) ? "Read" : "Preview";
		}

		internal static string PublishInfoAndPage(BookDetail detail)
		{
			return string.Format("{0} / {1} pages", detail.PublishInfo, detail.PageCount);
		}

		internal static string RatingReviewCount(BookDetail detail)
		{
			return string.Format("({0:N1}, {1} reviews)", detail.OverallRating, detail.ReviewAmount);
		}

		internal static string PriceDiscount(BookDetail detail)
		{
			if (detail.Discount == 100)
			{
				return string.Format("{0:C2} ({1} buyers till now)", detail.Price, detail.BuyAmount);
			}
			else
			{
				return string.Format("{0:C2} ({1}% OFF) ({2} buyers till now)",
									 detail.Price, 100 - detail.Discount, detail.BuyAmount);
			}
		}

		internal static string GetAllAuthors(BookDetail detail)
		{
			var str = "";
			if (detail.OtherAuthors == null || detail.OtherAuthors.Trim().Length == 0)
			{
				str += detail.AuthorName;
			}
			else
			{
				str += detail.AuthorName + "; " + detail.OtherAuthors;
			}
			return str;
		}

		internal static string OtherStatistic(BookDetail detail)
		{
			return string.Format("{0} bullet-screen comments & {1} previews",
								 detail.DanmuAmount, detail.PreviewAmount);
		}

		private async void Refresh()
		{
			await System.Threading.Tasks.Task.Delay(Util.REFRESH_RATE);
			while (!detail.Finished)
			{
				await System.Threading.Tasks.Task.Delay(100);
				Detail = detail;
				detail.RelatedBooks?.OnPropertyChanged();
			}
			await System.Threading.Tasks.Task.Delay(Util.REFRESH_RATE);
			Detail = detail;
			detail.RelatedBooks?.OnPropertyChanged();
		}

		private void RefreshRequested(RefreshContainer sender, RefreshRequestedEventArgs args)
		{
			this.detail.GetMoreReview();
			Refresh();
		}

		public void RefreshButtonPressed()
		{
			detail = new BookDetail(detail as BookSummary);
			Refresh();
		}

		public void AdminButtonPressed(bool isChecked)
		{
			if (isChecked)
				Util.OpenTerminal();
		}

		private async void Button_Click(object sender, RoutedEventArgs e)
		{
			var bookId = this.detail.ID;
			switch ((sender as Button).Tag as string)
			{
				case "buy":
					await Util.BuyBookAsync(bookId, this.detail, null, this.notification);
					this.Detail = detail;
					break;
				case "readlist":
					var ids = await NetworkGet.GetMyReadListsWithout(bookId);
					List<string> titles = new List<string>(ids.Length);
					foreach (int id in ids)
					{
						var readlist = new BookDetailCollection();
						await NetworkGet.GetTitleDescription(readlist, false, id);
						titles.Add(readlist.Title);
					}
					if (titles.Count <= 0)
						break;
					var combo = new ComboBox()
					{
						FontSize = 16,
						ItemsSource = titles,
						SelectedIndex = 0
					};
					var dialog = new ContentDialog()
					{
						Content = combo,
						Title = "Add Book to Read List",
						IsSecondaryButtonEnabled = true,
						PrimaryButtonText = "Confirm",
						SecondaryButtonText = "Cancel"
					};
					if (await dialog.ShowAsync() == ContentDialogResult.Primary)
					{
						var successful = await NetworkSet.ChangeReadList(ids[combo.SelectedIndex],
														BookListChangeType.AddBook,
														bookId, null);
						if (successful)
						{
							this.detail.CanAddReadList = titles.Count - 1 > 0;
							this.Detail = detail;
							notification.Show("Success in adding book to your read list" +
												$"\"{combo.SelectedItem as string}\"", 4000);
						}
						else
						{
							notification.Show("Something wrong in adding book to your read list" +
												$"\"{combo.SelectedItem as string}\". " + 
												"Please try again later.", 4000);
						}
					}
					break;
				case "wishlist":
					var success = await NetworkSet.ChangeWishlist(bookId, true);
					if (success)
					{
						this.detail.CanAddWishList = false;
						this.Detail = detail;
						notification.Show("Success in adding book to your wish list", 4000);
					}
					else
					{
						notification.Show("Something wrong in adding book to your wish list. " +
											"Please try again later.", 4000);
					}
					break;
				case "preview":
					var content = (sender as Button).Content as string;
					if (content == "Read")
					{
						var pdfUrl = await NetworkGet.DownloadBook(bookId);
						var pass = await NetworkGet.GetBookKey(bookId);
						if (pass == null || pass.Length == 0)
						{
							notification.Show("It seems that you haven't bought the book. " +
											"Please try again later.", 4000);
							return;
						}
						Util.MainElem.NavigateToReadBook(bookId, pdfUrl, pass);
					}
					else
					{
						var pdfUrl = await NetworkGet.GetBookPreview(bookId);
						Util.MainElem.NavigateToReadBook(bookId, pdfUrl);
					}
					break;
				default:
					return;
			}
		}
	}
}
