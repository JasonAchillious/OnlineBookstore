using Windows.UI.Xaml.Controls;
using Windows.UI.Xaml.Navigation;


namespace Frontend
{
	/// <summary>
	/// 可用于自身或导航至 Frame 内部的空白页。
	/// </summary>
	public sealed partial class BooklistPage : Page, IRefreshAdminInterface
	{
		public BooklistPage()
		{
			this.InitializeComponent();
			this.NavigationCacheMode = NavigationCacheMode.Enabled;
		}

		private QueryObject query = new QueryObject();
		private string title = "";
		private string description = "";

		private bool IsBillboard { get => !query.IsBillboard.HasValue || query.IsBillboard.Value; }

		private CustomControls.BookCollectionControl bookCollection;

		protected override void OnNavigatedTo(NavigationEventArgs e)
		{
			base.OnNavigatedTo(e);
			var para = ((string title, string description, QueryObject query))e.Parameter;
			if (this.title == para.title && this.description == para.description && this.query == para.query)
				return;
			this.title = para.title;
			this.description = para.description;
			this.query = para.query;
			this.bookCollection = new CustomControls.BookCollectionControl(
				new BookDetailCollection(this.query, this.title, this.description),
				this.IsBillboard)
			{
				PaddingX = 135
			};
			grid.Children.Clear();
			grid.Children.Add(bookCollection);
		}

		public void RefreshButtonPressed()
		{
			this.bookCollection.RefreshPage();
		}

		public void AdminButtonPressed(bool isChecked)
		{
			if (isChecked)
				Util.OpenTerminal();
		}

		internal static string PriceDiscount(double price, int discount)
		{
			if (discount == 100)
			{
				return string.Format("Price:\t{0:C2}", price);
			}
			else
			{
				return string.Format("Price:\t{0:C2} ({1}% OFF)", price, 100 - discount);
			}
		}

		internal static string AuthorTrim(string author)
		{
			return string.Format("Author:\t{0}", author.CutString(19));
		}
	}
}
