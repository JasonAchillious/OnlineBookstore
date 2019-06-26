using System;
using System.Collections.ObjectModel;
using Windows.UI.Xaml.Controls;
using Windows.UI.Xaml.Navigation;

// https://go.microsoft.com/fwlink/?LinkId=234238 上介绍了“空白页”项模板

namespace Frontend
{
	/// <summary>
	/// 可用于自身或导航至 Frame 内部的空白页。
	/// </summary>
	public sealed partial class MyDanmuPage : Page, IRefreshAdminInterface
	{
		public MyDanmuPage()
		{
			this.InitializeComponent();
			this.NavigationCacheMode = NavigationCacheMode.Enabled;

			this.Refresh();
		}

		private ObservableCollection<FullDanmu> Danmus { set; get; } =
			 new ObservableCollection<FullDanmu>();

		internal static string NamePageToString(string bookName, int page)
		{
			return string.Format("At Book “{0}” of page {1}", bookName, page);
		}

		internal static string TimeToString(DateTime time)
		{
			return string.Format("Last edit {0}", time.ToString());
		}

		private async void Refresh()
		{
			this.loadingControl.IsLoading = true;
			this.Danmus.Clear();
			var ids = await NetworkGet.GetMyDanmus();
			foreach (int id in ids)
			{
				var dan = new FullDanmu(id);
				await NetworkGet.GetFullDanmuContent(dan);
				this.Danmus.Add(dan);
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

		private async void Edit_Invoked(SwipeItem sender, SwipeItemInvokedEventArgs args)
		{
			var danmu = args.SwipeControl.DataContext as FullDanmu;
			var orgContent = danmu.Content;
			var newContent = await Util.InputTextDialogAsync("Editing Bullet Screen",
															 "Please input the content of your bullet screen",
															 orgContent);
			if (newContent != null && newContent.Length > 0 && newContent != orgContent)
			{
				var success = await NetworkSet.ChangeDanmu(danmu.ID, false, newContent);
				if (!success)
					return;
				danmu.Content = newContent;
				danmu.EditTime = DateTime.Now;
				danmu.OnPropertyChanged("Content");
				danmu.OnPropertyChanged("EditTime");
			}
		}

		private async void Delete_Invoked(SwipeItem sender, SwipeItemInvokedEventArgs args)
		{
			var danmu = args.SwipeControl.DataContext as FullDanmu;
			var success = await NetworkSet.ChangeDanmu(danmu.ID, true);
			if (!success)
				return;
			this.Danmus.Remove(args.SwipeControl.DataContext as FullDanmu);
		}
	}
}
