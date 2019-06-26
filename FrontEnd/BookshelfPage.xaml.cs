using Windows.UI.Xaml.Controls;
using Windows.UI.Xaml.Navigation;

// https://go.microsoft.com/fwlink/?LinkId=234238 上介绍了“空白页”项模板

namespace Frontend
{
	/// <summary>
	/// 可用于自身或导航至 Frame 内部的空白页。
	/// </summary>
	public sealed partial class BookshelfPage : Page, IRefreshAdminInterface
	{
		public BookshelfPage()
		{
			this.InitializeComponent();
			this.NavigationCacheMode = NavigationCacheMode.Enabled;

			_ = ShelfBooks.Reload();
			this.WaitLoading();
		}

		private BookSummaryCollection ShelfBooks { set; get; }
			= new BookSummaryCollection(BookSummaryCollection.OtherType.Bookshelf);

		private async void WaitLoading()
		{
			while (!ShelfBooks.Finished)
			{
				await System.Threading.Tasks.Task.Delay(Util.REFRESH_RATE);
			}
			loadingControl.IsLoading = false;
		}

		private void Refresh()
		{
			if (!loadingControl.IsLoading)
			{
				loadingControl.IsLoading = true;
				_ = ShelfBooks.Reload();
				WaitLoading();
			}
		}

		private void RefreshRequested(RefreshContainer sender, RefreshRequestedEventArgs args)
		{
			Refresh();
		}

		public void RefreshButtonPressed()
		{
			Refresh();
		}

		public void AdminButtonPressed(bool isChecked)
		{
			if (isChecked)
				Util.OpenTerminal();
		}

		/// <summary>
		/// Navigate to read book page
		/// </summary>
		private async void AdaptiveGridView_ItemClick(object sender, ItemClickEventArgs e)
		{
			var book = e.ClickedItem as BookSummary;
			var pdfUrl = await NetworkGet.DownloadBook(book.ID);
			var password = await NetworkGet.GetBookKey(book.ID);
			if (password == null || password.Length == 0)
			{
				notification.Show("It seems that you do not own the book, " +
								  "please try again", 4000);
				return;
			}
			Util.MainElem.NavigateToReadBook(book.ID, pdfUrl, password);
		}
	}
}
