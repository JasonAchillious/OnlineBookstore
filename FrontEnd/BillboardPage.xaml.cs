using Windows.UI.Xaml.Controls;
using Windows.UI.Xaml.Navigation;

// https://go.microsoft.com/fwlink/?LinkId=234238 上介绍了“空白页”项模板

namespace Frontend
{
	/// <summary>
	/// 可用于自身或导航至 Frame 内部的空白页。
	/// </summary>
	public sealed partial class BillboardPage : Page, IRefreshAdminInterface
	{
		public BillboardPage()
		{
			this.InitializeComponent();
			this.NavigationCacheMode = NavigationCacheMode.Enabled;

			listControl.Booklist = new BooklistCollection(true);
			listControl.WaitLoading();
		}

		public void RefreshButtonPressed()
		{
			listControl.Refresh(false);
		}

		public void AdminButtonPressed(bool isChecked)
		{
			if (isChecked)
			{
				listControl.CanEdit = true;
				listControl.ShowLeftSwipe = true;
				listControl.ShowLeftSwipe = true;
				listControl.UpdateSwipes();
			}
			else
			{
				listControl.CanEdit = false;
				listControl.ShowLeftSwipe = false;
				listControl.ShowLeftSwipe = false;
				listControl.UpdateSwipes();
			}
		}
	}
}
