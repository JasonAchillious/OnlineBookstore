using Microsoft.Toolkit.Uwp.UI.Animations;
using System;
using System.Collections.Generic;
using System.Linq;
using Windows.Foundation;
using Windows.System;
using Windows.UI.ViewManagement;
using Windows.UI.Xaml;
using Windows.UI.Xaml.Controls;
using Windows.UI.Xaml.Input;
using Windows.UI.Xaml.Media.Animation;
using Windows.UI.Xaml.Navigation;

namespace Frontend
{
	/// <summary>
	/// 可用于自身或导航至 Frame 内部的空白页。
	/// </summary>
	public sealed partial class MainPage : Page
	{
		private async void ShowInput()
		{
			var ip = new TextBox()
			{
				Header = "Please input the remote IP",
				PlaceholderText = "114.116.100.205",//"127.0.0.1",
			};
			var userPort = new TextBox()
			{
				Header = "Please input the remote port of user",
				PlaceholderText = "2307"
			};
			var adminPort = new TextBox()
			{
				Header = "Please input the remote port of admin",
				PlaceholderText = "2308"
			};
			var panel = new StackPanel()
			{
				Spacing = 10
			};
			panel.Children.Add(ip);
			panel.Children.Add(userPort);
			panel.Children.Add(adminPort);
			ContentDialog dialog = new ContentDialog()
			{
				Content = panel,
				Title = "Internet Info",
				PrimaryButtonText = "Confirm"
			};
			await dialog.ShowAsync();
			try
			{
				System.Net.IPAddress.Parse(ip.Text);
				Connection.REMOTE_IP = ip.Text;
			}
			catch (Exception)
			{
				Connection.REMOTE_IP = ip.PlaceholderText;
			}
			try
			{
				Connection.REMOTE_PORT_USER = Convert.ToInt32(userPort.Text);
			}
			catch (Exception)
			{
				Connection.REMOTE_PORT_USER = Convert.ToInt32(userPort.PlaceholderText);
			}
			try
			{
				Connection.REMOTE_PORT_ADMIN = Convert.ToInt32(adminPort.Text);
			}
			catch (Exception)
			{
				Connection.REMOTE_PORT_ADMIN = Convert.ToInt32(adminPort.PlaceholderText);
			}
		}

		public MainPage()
		{
			this.InitializeComponent();

			ApplicationView.PreferredLaunchViewSize = new Size(1440, 900);
			ApplicationView.PreferredLaunchWindowingMode = ApplicationViewWindowingMode.PreferredLaunchViewSize;

			ShowInput();
			ShowSearch(false);
		}

		private void ContentFrame_NavigationFailed(object sender, NavigationFailedEventArgs e)
		{
			Console.Error.WriteLine("Failed to load Page " + e.SourcePageType.FullName);
		}

		// List of ValueTuple holding the Navigation Tag and the relative Navigation Page
		private Dictionary<string, Type> Pages { get; } = new Dictionary<string, Type>
		{
			{ "home", typeof(HomePage) },
			{ "search", typeof(SearchPage) },
			{ "readlist", typeof(ReadlistPage) },
			{ "billboard", typeof(BillboardPage) },
			{ "bookshelf", typeof(BookshelfPage) },
			{ "myreview", typeof(MyReivewPage) },
			{ "mydanmu", typeof(MyDanmuPage) },
			{ "myreadlist", typeof(MyReadlistPage) },
			{ "mywishlist", typeof(MyWishlistPage) },
			{ "myfollow", typeof(MyFollowedListPage) },
			{ "login", typeof(LoginPage) }
		};

		internal Type CurrentPage { get => ContentFrame.CurrentSourcePageType; }

		private void ShowSearch(bool visible)
		{
			SearchMain.Visibility = (!visible).ToVisibility();
			SearchBtn.Visibility = visible.ToVisibility();
		}

		private void ShowAdmin(bool visible)
		{
			var v = visible.ToVisibility();
			TopSeparator.Visibility = v;
			ToggleAdmin.Visibility = v;
		}

		private void ShowMyAccount(bool visible)
		{
			var v = visible.ToVisibility();
			BookshelfBtn.Visibility = v;
			MyDanmuBtn.Visibility = v;
			MyReadlistBtn.Visibility = v;
			MyFolloedlistBtn.Visibility = v;
			MyWishlistBtn.Visibility = v;
			MyReviewBtn.Visibility = v;
		}

		private void NavView_SelectionChanged(NavigationView sender, NavigationViewSelectionChangedEventArgs args)
		{
			if (args.IsSettingsSelected == true)
			{
				NavView_Navigate("settings", args.RecommendedNavigationTransitionInfo);
			}
			else if (args.SelectedItemContainer != null)
			{
				var navItemTag = args.SelectedItemContainer.Tag.ToString();
				NavView_Navigate(navItemTag, args.RecommendedNavigationTransitionInfo);
			}
		}

		private void NavView_Navigate(string navItemTag, NavigationTransitionInfo info,
									  bool Override = true, object pass = null)
		{
			// show admin toggle or not
			ShowAdmin(Storage.IsAdmin);

			Type _page = null;
			if (navItemTag == "settings")
			{
				_page = typeof(SettingPage);
			}
			else
			{
				var item = Pages.FirstOrDefault(p => p.Key.Equals(navItemTag));
				_page = item.Value;
			}
			// Get the page type before navigation so you can prevent duplicate entries in the backstack.
			var preNavPageType = ContentFrame.CurrentSourcePageType;

			// Only navigate if the selected page isn't currently loaded.
			if (!(_page is null) && !Equals(preNavPageType, _page))
			{
				//Continuum, Common, DrillIn for go inside, Entrance, Slide, Suppress
				if (Override) {
					info = new SlideNavigationTransitionInfo()
					{ Effect = SlideNavigationTransitionEffect.FromRight };
				}
				ContentFrame.Navigate(_page, pass, info);
			}
		}

		private void NavView_Navigate(Type toPage, NavigationTransitionInfo info,
									  bool Override = true, object pass = null)
		{
			// show admin toggle or not
			ShowAdmin(Storage.IsAdmin);

			// Get the page type before navigation so you can prevent duplicate entries in the backstack.
			var preNavPageType = ContentFrame.CurrentSourcePageType;

			// Only navigate if the selected page isn't currently loaded.
			if (!(toPage is null) && !Equals(preNavPageType, toPage))
			{
				//Continuum, Common, DrillIn for go inside, Entrance, Slide, Suppress
				if (Override)
				{
					info = new SlideNavigationTransitionInfo()
					{ Effect = SlideNavigationTransitionEffect.FromRight };
				}
				ContentFrame.Navigate(toPage, pass, info);
			}
		}

		private void NavView_Loaded(object sender, RoutedEventArgs e)
		{
			// Add keyboard accelerators for backwards navigation.
			var goBack = new KeyboardAccelerator { Key = VirtualKey.GoBack };
			goBack.Invoked += BackInvoked;
			this.KeyboardAccelerators.Add(goBack);
			// ALT routes here
			var altLeft = new KeyboardAccelerator
			{
				Key = VirtualKey.Left,
				Modifiers = VirtualKeyModifiers.Menu
			};
			altLeft.Invoked += BackInvoked;
			this.KeyboardAccelerators.Add(altLeft);

			Util.MainElem = this;

			NavView_Navigate("home", new EntranceNavigationTransitionInfo(), false);
			ShowAdmin(false);
			ShowMyAccount(false);
		}

		private void NavView_BackRequested(NavigationView sender, NavigationViewBackRequestedEventArgs args)
		{
			On_BackRequested();
		}

		private void BackInvoked(KeyboardAccelerator sender, KeyboardAcceleratorInvokedEventArgs args)
		{
			On_BackRequested();
			args.Handled = true;
		}

		private bool On_BackRequested()
		{
			if (!ContentFrame.CanGoBack)
				return false;

			// Don't go back if the nav pane is overlayed.
			if (NavView.IsPaneOpen &&
				(NavView.DisplayMode == NavigationViewDisplayMode.Compact ||
				 NavView.DisplayMode == NavigationViewDisplayMode.Minimal))
				return false;

			ContentFrame.GoBack();
			
			return true;
		}

		private void ContentFrame_Navigating(object sender, NavigatingCancelEventArgs e)
		{
			if (ContentFrame.SourcePageType == typeof(SearchPage))
			{
				ShowSearch(false);
			}
		}

		private void ContentFrame_Navigated(object sender, NavigationEventArgs e)
		{
			NavView.IsBackEnabled = ContentFrame.CanGoBack;

			danmuBtn.Visibility = (ContentFrame.SourcePageType == typeof(ReadPage)).ToVisibility();

			var item = Pages.FirstOrDefault(p => p.Value == e.SourcePageType);
			if (item.Key == null || item.Value == null)
			{
				if (ContentFrame.SourcePageType is Type page)
				{
					WelcomeLabel1.Text = page.Name.Replace("Page", "").
												   Replace("Danmu", "BulletComments").
												   Replace("list", "List").
												   WordSplit();
					WelcomeLabel2.Text = "";
				}
				return;
			}

			if (ContentFrame.SourcePageType == typeof(SettingPage))
			{
				// SettingsItem is not part of NavView.MenuItems, and doesn't have a Tag.
				NavView.SelectedItem = (NavigationViewItem)NavView.SettingsItem;
				WelcomeLabel1.Text = "Settings";
				WelcomeLabel2.Text = "";
				return;
			}
			else if (ContentFrame.SourcePageType == typeof(LoginPage) ||
					 ContentFrame.SourcePageType == typeof(HomePage))
			{
				WelcomeLabel1.Text = "Welcome to";
				WelcomeLabel2.Text = " BookHub";
			}
			else if (ContentFrame.SourcePageType == typeof(SearchPage))
			{
				ShowSearch(true);
				NavView.SelectedItem = NavView.MenuItems[2];
				WelcomeLabel1.Text = "Searching";
				WelcomeLabel2.Text = "";
			}
			else if (ContentFrame.SourcePageType != null)
			{
				WelcomeLabel1.Text = ((NavigationViewItem)NavView.SelectedItem)?.Content?.ToString();
				WelcomeLabel2.Text = "";
			}

			NavView.SelectedItem = NavView.MenuItems.OfType<NavigationViewItem>().First(n => n.Tag.Equals(item.Key));
		}

		internal void NavigateToHomeAndShowMine(bool show, bool navigate = true)
		{
			if (show)
			{
				if (navigate)
					NavView.SelectedItem = NavView.MenuItems[1];
				ShowMyAccount(true);
			}
			else
			{
				ShowMyAccount(false);
				if (navigate)
					NavView.SelectedItem = NavView.MenuItems[NavView.MenuItems.Count - 1];
			}
		}

		internal void NavigateToReadBook(int bookId, string uri, string password = "")
		{
			NavView_Navigate(typeof(ReadPage), null, true, (password, uri, bookId));
		}

		internal void NavigateToSignUp()
		{
			NavView_Navigate(typeof(SignUpPage), null, true, null);
		}

		internal void NavigateToBookDetail(BookSummary itemToPass, Type page)
		{
			ContentFrame.SetListDataItemForNextConnectedAnimation(itemToPass);
			ContentFrame.Navigate(page, itemToPass, new SuppressNavigationTransitionInfo());
		}

		internal void NavigateToBookDetail(BookSummary itemToPass, Type page, NavigationTransitionInfo info)
		{
			ContentFrame.SetListDataItemForNextConnectedAnimation(itemToPass);
			ContentFrame.Navigate(page, itemToPass, info);
		}

		internal void NavigateToBooklist(string title, string description, QueryObject query)
		{
			(string title, string description, QueryObject query) passObj;
			passObj.title = title;
			passObj.description = description;
			passObj.query = query;
			NavView_Navigate(typeof(BooklistPage), null, true, passObj);
		}

		private void SearchMain_QuerySubmitted(AutoSuggestBox sender, AutoSuggestBoxQuerySubmittedEventArgs args)
		{
			this.QuerySubmitted(args.QueryText);
		}

		internal void QuerySubmitted(string query)
		{
			if (query.Trim().Length == 0)
				return;
			ShowSearch(true);
			NavView_Navigate("search", null, true, new SearchInfo(query));
		}

		private void Refresh_Pressed(object sender, RoutedEventArgs e)
		{
			if (ContentFrame.SourcePageType != null)
			{
				(ContentFrame.Content as IRefreshAdminInterface)?.RefreshButtonPressed();
			}
		}

		private void Admin_Pressed(object sender, RoutedEventArgs e)
		{
			if (ContentFrame.SourcePageType != null)
			{
				var c = (sender as AppBarToggleButton)?.IsChecked;
				if (c != null && c.HasValue)
					(ContentFrame.Content as IRefreshAdminInterface).AdminButtonPressed(c.Value);
			}
		}

		private void SendDanmu_Pressed(object sender, RoutedEventArgs e)
		{
			if(ContentFrame.SourcePageType != null)
			{
				if (ContentFrame.Content is ISendDanmuInterface page)
					page.SendDanmuPressed();
			}
		}

		private void About_PointerReleased(object sender, PointerRoutedEventArgs e)
		{
			ContentDialog dialog = new ContentDialog()
			{
				Title = "About Us",
				Content = "© Kebin Sun, Zhixiang Zhao, Yanchao Miao, Lei Niu\r\n" +
						  "Group 309 of “CS307 Principle of Database System”\r\n" +
						  $"'{DateTime.Now.ToShortDateString()}'",
				PrimaryButtonText = "Close"
			};
			_ = dialog.ShowAsync();
		}
	}
}
