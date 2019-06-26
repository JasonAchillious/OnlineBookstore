using System;
using System.Collections.Generic;
using System.Collections.ObjectModel;
using System.IO;
using System.Linq;
using System.Runtime.InteropServices.WindowsRuntime;
using System.Threading.Tasks;
using Windows.Foundation;
using Windows.Foundation.Collections;
using Windows.UI.Xaml;
using Windows.UI.Xaml.Controls;
using Windows.UI.Xaml.Controls.Primitives;
using Windows.UI.Xaml.Data;
using Windows.UI.Xaml.Input;
using Windows.UI.Xaml.Media;
using Windows.UI.Xaml.Navigation;

// https://go.microsoft.com/fwlink/?LinkId=234238 上介绍了“空白页”项模板

namespace Frontend
{
	/// <summary>
	/// 可用于自身或导航至 Frame 内部的空白页。
	/// </summary>
	public sealed partial class MyReivewPage : Page, IRefreshAdminInterface
	{
		public MyReivewPage()
		{
			this.InitializeComponent();
			this.NavigationCacheMode = NavigationCacheMode.Enabled;

			Refresh();
		}

		private ObservableCollection<Review> MyReviews { set; get; } = new ObservableCollection<Review>();

		private async void Refresh()
		{
			this.MyReviews.Clear();
			loadingControl.IsLoading = true;
			var ids = await NetworkGet.GetMyReviews();
			foreach (int id in ids)
			{
				var review = new Review(id);
				await NetworkGet.GetReview(review);
				this.MyReviews.Add(review);
			}
			loadingControl.IsLoading = false;
		}

		private static async Task<bool> EditReview((int, bool, string, string, int) change)
		{
			return await NetworkSet.ChangeReview
				(change.Item1, change.Item2, change.Item5, change.Item3, change.Item4);
		}

		private static readonly Func<(int, bool, string, string, int), Task<bool>> editFunc = EditReview;

		private async void TextBox_KeyUp(object sender, KeyRoutedEventArgs e)
		{
			if (e.Key == Windows.System.VirtualKey.Enter)
			{
				var review = (sender as TextBox)?.DataContext as Review;
				var parent = (sender as TextBox)?.GetParentUpto() as StackPanel;
				var titleBox = parent.Children[0] as TextBox;
				var contentBox = parent.Children[1] as TextBox;
				var rate = ((parent.GetParentUpto() as Grid).Children[0] as StackPanel).Children[0] as RatingControl;

				var para = (review.ID, false, titleBox.Text, contentBox.Text, Convert.ToInt32(rate.Value));
				bool success = await editFunc.GlobalLock(para);
				if (success)
				{
					review.PublishDate = DateTime.Now;
					notification.Show("Success in editing your review for book" +
										$" \"{review.BookName}\"", 4000);
				}
				else
				{
					titleBox.Text = review.Title;
					contentBox.Text = review.Content;
					rate.Value = review.Rating;
					notification.Show("Something wrong in editing your review for book" +
										$" \"{review.BookName}\". " +
										"Please try again later.", 4000);
				}
				e.Handled = true;
			}
		}

		private async void RatingControl_ValueChanged(RatingControl sender, object args)
		{
			var review = sender.DataContext as Review;
			var parent = (sender.GetParentUpto(2) as Grid).Children[1] as StackPanel;
			var titleBox = parent.Children[0] as TextBox;
			var contentBox = parent.Children[1] as TextBox;
			var para = (review.ID, false, titleBox.Text, contentBox.Text, Convert.ToInt32(sender.Value));
			bool success = await editFunc.GlobalLock(para);
			if (success)
			{
				review.PublishDate = DateTime.Now;
				notification.Show("Success in editing your review for book" +
									$" \"{review.BookName}\"", 4000);
			}
			else
			{
				titleBox.Text = review.Title;
				contentBox.Text = review.Content;
				sender.Value = review.Rating;
				notification.Show("Something wrong in editing your review for book" +
									$" \"{review.BookName}\". " +
									"Please try again later.", 4000);
			}
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
		/// Delete review
		/// </summary>
		private async void SwipeItem_Invoked(SwipeItem sender, SwipeItemInvokedEventArgs args)
		{
			ContentDialog dialog = new ContentDialog
			{
				Content = "Are you sure about deleting the review? This process is irreversible.",
				Title = "Delete Review",
				IsSecondaryButtonEnabled = true,
				PrimaryButtonText = "Confirm",
				SecondaryButtonText = "Cancle"
			};
			if (await dialog.ShowAsync() == ContentDialogResult.Secondary)
			{
				return;
			}
			var review = args.SwipeControl.DataContext as Review;
			var para = (review.ID, true, "", "", 5);
			bool success = await editFunc.GlobalLock(para);
			if (success)
			{
				this.MyReviews.Remove(review);
				notification.Show("Success in deleting your review for book" +
									$" \"{review.BookName}\"", 4000);
			}
			else
			{
				notification.Show("Something wrong in deleting your review for book" +
									$" \"{review.BookName}\". " +
									"Please try again later.", 4000);
			}
		}

		private async void Button_Click(object sender, RoutedEventArgs e)
		{
			var bookIds = await NetworkGet.GetShelfBooks();
			if (bookIds.Length <= 0)
			{
				notification.Show("You cannot write any review since you own zero book", 4000);
				return;
			}
			var books = new List<BookSummary>(bookIds.Length);
			foreach (var id in bookIds)
			{
				var book = new BookSummary(id);
				await NetworkGet.GetBookSummary(book);
				if (!MyReviews.Select(r => r.BookName).Contains(book.BookFullName))
					books.Add(book);
			}

			var ratingCtrl = new RatingControl()
			{
				Value = 5
			};
			var combo = new ComboBox()
			{
				FontSize = 15,
				ItemsSource = books.Select(b => b.BookFullName).ToList(),
				SelectedIndex = 0,
				Header = "Please select the book you want to write review to"
			};
			var titleBox = new TextBox()
			{
				FontSize = 16,
				Header = "Please input the review title",
				PlaceholderText = "Review title",
				TextWrapping = TextWrapping.Wrap
			};
			var contentBox = new TextBox()
			{
				FontSize = 15,
				Header = "Please input the review content",
				PlaceholderText = "Review content",
				TextWrapping = TextWrapping.Wrap
			};
			var panel = new StackPanel()
			{
				Spacing = 10,
				MaxWidth = 500
			};
			panel.Children.Add(ratingCtrl);
			panel.Children.Add(combo);
			panel.Children.Add(titleBox);
			panel.Children.Add(contentBox);
			ContentDialog dialog = new ContentDialog
			{
				Content = panel,
				Title = "Write Review",
				IsSecondaryButtonEnabled = true,
				PrimaryButtonText = "Confirm",
				SecondaryButtonText = "Cancle"
			};
			if (await dialog.ShowAsync() == ContentDialogResult.Secondary
				|| titleBox.Text.Length <= 1 || contentBox.Text.Length <= 1)
			{
				return;
			}
			var success = await NetworkSet.CreateReview(
				books.Where(b => b.BookFullName == combo.SelectedItem as string).ToList()[0].ID,
				Convert.ToInt32(ratingCtrl.Value),
				titleBox.Text, contentBox.Text);
			if (success)
			{
				var review = new Review(-1)
				{
					Title = titleBox.Text,
					Content = contentBox.Text,
					Rating = Convert.ToInt32(ratingCtrl.Value),
					BookName = combo.SelectedItem as string,
					PublishDate = DateTime.Now
				};
				MyReviews.Add(review);
				notification.Show("Success in writing your review for book" +
									$" \"{combo.SelectedItem as string}\"", 4000);
			}
			else
			{
				notification.Show("Something wrong in writing your review for book" +
									$" \"{combo.SelectedItem as string}\". " +
									"Please try again later.", 4000);
			}
		}
	}
}
