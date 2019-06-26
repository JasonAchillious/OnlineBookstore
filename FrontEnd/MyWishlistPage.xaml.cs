using System;
using System.Collections.ObjectModel;
using Windows.UI.Xaml;
using Windows.UI.Xaml.Controls;
using Windows.UI.Xaml.Media;
using Windows.UI.Xaml.Media.Animation;
using Windows.UI.Xaml.Navigation;

// https://go.microsoft.com/fwlink/?LinkId=234238 上介绍了“空白页”项模板

namespace Frontend
{
	/// <summary>
	/// 可用于自身或导航至 Frame 内部的空白页。
	/// </summary>
	public sealed partial class MyWishlistPage : Page, IRefreshAdminInterface
	{
		public MyWishlistPage()
		{
			this.InitializeComponent();
			this.NavigationCacheMode = NavigationCacheMode.Enabled;

			this.Refresh();
		}

		private ObservableCollection<BookDetail> WishBooks { set; get; } = new ObservableCollection<BookDetail>();

		private async void Refresh()
		{
			this.loadingControl.IsLoading = true;
			this.WishBooks.Clear();
			var ids = await NetworkGet.GetMyWishlist();
			foreach (int id in ids)
			{
				var book = new BookDetail(id);
				await NetworkGet.GetBookQuasiDetail(book);
				book.AddPrice = await NetworkGet.GetAddPrice(id);
				this.WishBooks.Add(book);
			}
			this.loadingControl.IsLoading = false;
		}

		private void RefreshRequested(RefreshContainer sender, RefreshRequestedEventArgs args)
		{
			this.Refresh();
		}

		public void RefreshButtonPressed()
		{
			this.Refresh();
		}

		public void AdminButtonPressed(bool isChecked)
		{
			if (isChecked)
				Util.OpenTerminal();
		}

		/// <summary>
		/// Navigate to detail page
		/// </summary>
		private void Book_Pointed(object sender, ItemClickEventArgs e)
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

		private async void Buy_Invoked(SwipeItem sender, SwipeItemInvokedEventArgs args)
		{
			var book = args.SwipeControl.DataContext as BookDetail;
			var bookId = book.ID;
			await Util.BuyBookAsync(bookId, book, this.WishBooks, this.notification);
		}

		private async void Delete_Invoked(SwipeItem sender, SwipeItemInvokedEventArgs args)
		{
			var book = args.SwipeControl.DataContext as BookDetail;
			var success = await NetworkSet.ChangeWishlist(book.ID, false);
			if (!success)
				return;
			this.WishBooks.Remove(book);
		}

		internal static string AuthorClass(string author, string category)
		{
			return string.Format("Author:\t{0}\t\tCategory: {1}", author, category);
		}

		internal static string AddPrice(double price, int discount, double addPrice)
		{
			if (addPrice <= price)
			{
				if (discount == 100)
					return string.Format("Price:\t{0:C2}", price);
				else
					return string.Format("Price:\t{0:C2} ({1}% OFF)", price, 100 - discount);
			}
			else
			{
				if (discount == 100)
					return string.Format("Price:\t{0:C2} ({1:N2}% cheaper)", price, 100 * (1 - price / addPrice));
				else
					return string.Format("Price:\t{0:C2} ({1}% OFF, {2:N2}% cheaper)",
										price, 100 - discount, 100 * (1 - price / addPrice));
			}
		}
	}
}
