using System;
using System.Collections.ObjectModel;
using System.Diagnostics;
using System.Threading.Tasks;
using Windows.UI.Xaml;
using Windows.UI.Xaml.Controls;
using Windows.UI.Xaml.Controls.Primitives;
using Windows.UI.Xaml.Media.Animation;
using Windows.UI.Xaml.Navigation;

// https://go.microsoft.com/fwlink/?LinkId=234238 上介绍了“空白页”项模板

namespace Frontend
{
	/// <summary>
	/// 可用于自身或导航至 Frame 内部的空白页。
	/// </summary>
	public sealed partial class MyReadlistPage : Page, IRefreshAdminInterface
	{
		public MyReadlistPage()
		{
			this.InitializeComponent();
			this.NavigationCacheMode = NavigationCacheMode.Enabled;

			listControl.Booklist = new BooklistCollection
			{
				Booklists = this.ReadLists
			};
			listControl.RefreshOverride = this.Refresh;
			listControl.WaitLoading();
			this.Refresh();
		}

		private ObservableCollection<BookDetailCollection> ReadLists { set; get; } =
			 new ObservableCollection<BookDetailCollection>();

		private async void Refresh()
		{
			// bypass the BooklistCollection.Reload
			listControl.WaitLoading();
			this.ReadLists.Clear();
			var ids = await NetworkGet.GetMyCreatedReadLists();
			foreach (int id in ids)
			{
				var read = new BookDetailCollection(false, id);
				await read.ReloadBooks(false, id, int.MaxValue); // get all books
				this.ReadLists.Add(read);
			}
			listControl.Booklist.Finished = true;
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
	}
}
