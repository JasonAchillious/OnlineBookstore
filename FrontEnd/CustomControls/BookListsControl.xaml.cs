using System;
using System.ComponentModel;
using System.Threading.Tasks;
using Windows.UI.Xaml;
using Windows.UI.Xaml.Controls;
using Windows.UI.Xaml.Controls.Primitives;
using Windows.UI.Xaml.Media.Animation;

//https://go.microsoft.com/fwlink/?LinkId=234236 上介绍了“用户控件”项模板

namespace Frontend.CustomControls
{
	public sealed partial class BookListsControl : UserControl, INotifyPropertyChanged
	{
		public BookListsControl()
		{
			this.InitializeComponent();
		}

		public BooklistCollection Booklist { set; get; }

		public bool ShowAddButton { set; get; } = false;
		public bool ShowTopSwipe { set; get; } = false;
		public bool ShowLeftSwipe { set; get; } = false;
		public bool IsTopSwipeFollow { set; get; } = true;

		internal void UpdateSwipes()
		{
			foreach (var collection in this.Booklist?.Booklists)
			{
				if (collection != null)
				{
					collection.ShowFollowSwipe = this.IsTopSwipeFollow;
					collection.OnPropertyChanged("SwipeString");
					collection.OnPropertyChanged("SwipeIcon");
				}
			}
			this.OnPropertyChanged();
		}

		public event PropertyChangedEventHandler PropertyChanged;

		private void OnPropertyChanged(string name = null)
		{
			if (name == null)
			{
				PropertyChanged?.Invoke(this, new PropertyChangedEventArgs("ShowTopSwipe"));
				PropertyChanged?.Invoke(this, new PropertyChangedEventArgs("ShowLeftSwipe"));
				PropertyChanged?.Invoke(this, new PropertyChangedEventArgs("LeftSwipeText"));
				PropertyChanged?.Invoke(this, new PropertyChangedEventArgs("LeftIconSource"));
				PropertyChanged?.Invoke(this, new PropertyChangedEventArgs("TextBoxVisibility"));
				PropertyChanged?.Invoke(this, new PropertyChangedEventArgs("TextBlockVisibility"));
			}
			else
			{
				PropertyChanged?.Invoke(this, new PropertyChangedEventArgs(name));
			}
		}

		public bool CanEdit { set; get; } = false;

		public int PaddingX { set; get; }
		public Action RefreshOverride { set; get; } = null;
		public bool IsBillboard { set; get; }
		public bool ShowAllVisible { set; get; } = true;

		private Thickness OutPadding { get => new Thickness(this.PaddingX, 0, this.PaddingX, 0); }

		public Visibility TextBoxVisibility { get => CanEdit.ToVisibility(); }
		public Visibility ShowAllVisibility { get => ShowAllVisible.ToVisibility(); }
		public Visibility TextBlockVisibility { get => (!CanEdit).ToVisibility(); }
		public Visibility UserInfoVisibility { get => (!this.IsBillboard).ToVisibility(); }


		private void Parent_SizeChanged(object sender, SizeChangedEventArgs e)
		{
			this.OnPropertyChanged("DesireWidth");
		}

		public double DesireWidth {
			get {
				if (this.ActualWidth > 900)
					return this.ActualWidth / 2 - PaddingX;
				else if (this.ActualWidth > 400)
					return this.ActualWidth - 2 * PaddingX;
				else
					return 450;
			}
		}


		/// <summary>
		/// Show all button of read list, navigate to book list page
		/// </summary>
		private void Hyperlink_Click(object sender, RoutedEventArgs e)
		{
			var elem = sender as UIElement;
			var parent = elem.GetParentUpto(2);
			if (parent == null || !(parent is ContentPresenter))
				return;
			var collection = (parent as ContentPresenter).DataContext as BookDetailCollection;
			Util.MainElem.NavigateToBooklist(collection.Title, collection.Description, collection.query);
		}

		private (BookDetail item, BookDetailCollection parent) _nav;

		/// <summary>
		/// Navigate back from detail page
		/// </summary>
		private async void AllGrid_Loaded(object sender, RoutedEventArgs e)
		{
			if (this._nav.parent == null || this._nav.item == null)
				return;
			var animation = ConnectedAnimationService.GetForCurrentView().GetAnimation(Util.FROM_BOOK_DETAIL);
			if (animation == null)
				return;
			animation.Configuration = new DirectConnectedAnimationConfiguration();

			if (!(sender is ListViewBase allGrid))
			{
				animation.Cancel();
				return;
			}
			allGrid.ScrollIntoView(this._nav.parent);
			if (!(allGrid.ContainerFromItem(this._nav.parent) is GridViewItem container))
			{
				animation.Cancel();
				return;
			}
			if (!(((container.ContentTemplateRoot as SwipeControl).Content as Grid).Children
					is UIElementCollection child))
			{
				animation.Cancel();
				return;
			}
			if (!(child[child.Count - 1] is ListViewBase boardlist))
			{
				animation.Cancel();
				return;
			}
			await boardlist.TryStartConnectedAnimationAsync(animation, this._nav.item, "bookCover");

			this._nav.item = null;
			this._nav.parent = null;
		}

		/// <summary>
		/// Book in read list pressed, Navigate to detail page
		/// </summary>
		private void Book_ItemClick(object sender, ItemClickEventArgs e)
		{
			var listView = sender as ListViewBase;
			var dataToPass = e.ClickedItem as BookDetail;
			if (NetworkGet.IsValidID(dataToPass.ID))
			{
				listView.PrepareConnectedAnimation(Util.TO_BOOK_DETAIL, dataToPass, "bookCover");
				var service = ConnectedAnimationService.GetForCurrentView();
				service.DefaultDuration = TimeSpan.FromSeconds(0.45);

				this._nav.item = dataToPass;
				this._nav.parent = listView.DataContext as BookDetailCollection;
				Util.MainElem.NavigateToBookDetail(dataToPass, typeof(BookDetailPage));
			}
		}

		internal async void WaitLoading()
		{
			loadingControl.IsLoading = true;
			while (Booklist.Booklists.Count == 0)
				await Task.Delay(Util.REFRESH_RATE);

			while (true)
			{
				if (Booklist.Finished)
				{
					break;
				}
				else
				{
					await Task.Delay(Util.REFRESH_RATE);
					Booklist.OnPropertyChanged();
				}
			}
			await Task.Delay(Util.REFRESH_RATE * 2);
			Booklist.OnPropertyChanged();
			UpdateSwipes();
			loadingControl.IsLoading = false;
		}

		internal void Refresh(bool add = false)
		{
			if (this.RefreshOverride != null)
			{
				this.RefreshOverride.Invoke();
				return;
			}
			if (!loadingControl.IsLoading)
			{
				Booklist.Reload(add);
				WaitLoading();
			}
		}

		private void RefreshRequested(RefreshContainer sender, RefreshRequestedEventArgs args)
		{
			if (this.RefreshOverride == null)
				this.Refresh(true);
			else
				this.RefreshOverride.Invoke();
		}

		private async void Top_SwipeItem_Invoked(SwipeItem sender, SwipeItemInvokedEventArgs args)
		{
			if (!this.ShowTopSwipe)
			{
				notification.Show("The feature is not available for now", 5000);
				return;
			}

			if (!(args.SwipeControl.DataContext is BookDetailCollection collection) ||
				!collection.ID.HasValue)
			{
				System.Diagnostics.Debug.WriteLine("Top swipe item error!");
				return;
			}
			var id = collection.ID.Value;
			if (this.IsTopSwipeFollow)
			{
				bool success = await NetworkSet.FollowReadList(id, !collection.Followed);
				if (success)
				{
					collection.Followed = !collection.Followed;
					collection.OnPropertyChanged("SwipeString");
					collection.OnPropertyChanged("SwipeIcon");
				}
			}
			else
			{
				ContentDialog dialog = new ContentDialog()
				{
					Content = "Are you sure to delete the whole read lists?" +
								"\r\nThis operation is irrevesable.",
					Title = "Deleting Read List",
					IsSecondaryButtonEnabled = true,
					PrimaryButtonText = "Confirm",
					SecondaryButtonText = "Cancel"
				};
				if (await dialog.ShowAsync() == ContentDialogResult.Primary)
				{
					bool success = await NetworkSet.ChangeReadList(id, BookListChangeType.RemoveList);
					if (success)
					{
						this.Booklist.Booklists.Remove(collection);
						notification.Show($"Success in deleting your read list \"{collection.Title}\"", 4000);
					}
					else
					{
						notification.Show("Something wrong in deleting your read list" +
											$" \"{collection.Title}\". " +
											"Please try again later.", 4000);
					}
				}
			}
		}

		private async void Left_SwipeItem_Invoked(SwipeItem sender, SwipeItemInvokedEventArgs args)
		{
			if (!this.ShowLeftSwipe)
			{
				notification.Show("The feature is not available for now", 5000);
				return;
			}

			if (!(args.SwipeControl.DataContext is BookDetail book))
			{
				System.Diagnostics.Debug.WriteLine("Left swipe item error!");
				return;
			}
			var parent = args.SwipeControl.GetParentUpto(Util.LEVEL_DataTemplate + 5) as SwipeControl;
			var collection = parent?.DataContext as BookDetailCollection;
			if (collection == null || !collection.ID.HasValue)
			{
				System.Diagnostics.Debug.WriteLine("Left swipe item error!");
			}
			var id = collection.ID.Value;
			var success = await NetworkSet.ChangeReadList(id, BookListChangeType.DeleteBook, book.ID);
			if (success)
			{
				collection.Books.Remove(book);
				collection.EditTime = DateTime.Now;
				collection.OnPropertyChanged("EditTime");
				notification.Show($"Success in removing the book \"{book.BookFullName}\"" +
									" of your read list.", 4000);
			}
			else
			{
				notification.Show("Something wrong in removing the book" +
									$" \"{book.BookFullName}\" of your read list. " +
									"Please try again later.", 4000);
			}
		}

		private async void TextBox_KeyUp(object sender, Windows.UI.Xaml.Input.KeyRoutedEventArgs e)
		{
			if (e.Key == Windows.System.VirtualKey.Enter)
			{
				var box = sender as TextBox;
				var newText = box?.Text;
				var collection = (box.GetParentUpto(2) as FrameworkElement).DataContext as BookDetailCollection;
				var oldText = collection?.Title;
				if (newText == null || newText.Length <= 2 || newText == oldText)
					return;
				bool success;
				if ((sender as TextBox).Tag as string == "title")
				{
					success = await EditTitle_Invoked(newText, collection);
				}
				else
				{
					success = await EditDesc_Invoked(newText, collection);
				}
				if (success)
				{
					notification.Show($"Success in editing the {(sender as TextBox).Tag as string}" +
										" of your read list.", 4000);
				}
				else
				{
					notification.Show($"Something wrong in editing the {(sender as TextBox).Tag as string}" +
										"of your read list. Pleast try again later.", 4000);
					box.Text = oldText;
				}

				e.Handled = true;
				LoseFocus(box);
			}
		}

		/// <summary>
		/// Makes virtual keyboard disappear
		/// </summary>
		/// <param name="sender"></param>
		private void LoseFocus(object sender)
		{
			var control = sender as Control;
			var isTabStop = control.IsTabStop;
			control.IsTabStop = false;
			control.IsEnabled = false;
			control.IsEnabled = true;
			control.IsTabStop = isTabStop;
		}

		private async Task<bool> EditTitle_Invoked(string newTitle, BookDetailCollection collection)
		{
			var success = await NetworkSet.ChangeReadList(collection.ID.Value,
															BookListChangeType.ChangeTitle,
															null, newTitle);
			if (!success)
				return false;
			collection.Title = newTitle;
			collection.EditTime = DateTime.Now;
			collection.OnPropertyChanged("Title");
			collection.OnPropertyChanged("EditTime");
			return true;
		}

		private async Task<bool> EditDesc_Invoked(string newDesc, BookDetailCollection collection)
		{
			var success = await NetworkSet.ChangeReadList(collection.ID.Value,
															BookListChangeType.ChangeDescription,
															null, newDesc);
			if (!success)
				return false;
			collection.Description = newDesc;
			collection.EditTime = DateTime.Now;
			collection.OnPropertyChanged("Description");
			collection.OnPropertyChanged("EditTime");
			return true;
		}

		private async void Button_Click(object sender, RoutedEventArgs e)
		{
			if (!ShowAddButton)
				return;

			var titleBox = new TextBox()
			{
				Header = "Please input the title of your new read-list",
				PlaceholderText = "Title here",
				FontSize = 16
			};
			var descBox = new TextBox()
			{
				Header = "Please input the description of your new read-list",
				PlaceholderText = "Description here",
				FontSize = 16
			};
			var panel = new StackPanel()
			{
				Spacing = 10
			};
			panel.Children.Add(titleBox);
			panel.Children.Add(descBox);
			var dialog = new ContentDialog()
			{
				Content = panel,
				Title = "Create Read List",
				IsSecondaryButtonEnabled = true,
				PrimaryButtonText = "Confirm",
				SecondaryButtonText = "Cancle"
			};
			if (await dialog.ShowAsync() == ContentDialogResult.Primary &&
				titleBox.Text.Trim().Length > 1 && descBox.Text.Trim().Length > 1)
			{
				var success = await NetworkSet.CreateReadList(titleBox.Text.Trim(), descBox.Text.Trim());
				if (success)
				{
					var b = new BookDetailCollection()
					{
						Title = titleBox.Text.Trim(),
						Description = descBox.Text.Trim(),
						CreateUser = this.Booklist.Booklists.Count > 0 ?
										this.Booklist.Booklists[0].CreateUser : "MySelf",
						EditTime = DateTime.Now
					};
					this.Booklist.Booklists.Add(b);
				}
			}
		}
	}
}
