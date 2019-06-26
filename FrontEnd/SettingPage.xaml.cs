using System;
using Windows.UI;
using Windows.UI.Xaml;
using Windows.UI.Xaml.Controls;
using Windows.UI.Xaml.Navigation;

// https://go.microsoft.com/fwlink/?LinkId=234238 上介绍了“空白页”项模板

namespace Frontend
{
	/// <summary>
	/// 可用于自身或导航至 Frame 内部的空白页。
	/// </summary>
	public sealed partial class SettingPage : Page
	{
		public SettingPage()
		{
			this.InitializeComponent();
			this.NavigationCacheMode = NavigationCacheMode.Enabled;
		}

		private async void ComboBox_SelectionChanged(object sender, SelectionChangedEventArgs e)
		{
			var combo = sender as ComboBox;
			switch (combo.SelectedItem as string)
			{
				case "Black":
					Storage.DanmuColor = Colors.Black;
					break;
				case "Red":
					Storage.DanmuColor = Colors.MediumVioletRed;
					break;
				case "Green":
					Storage.DanmuColor = Colors.DarkOliveGreen;
					break;
				case "Blue":
					Storage.DanmuColor = Colors.DarkSlateBlue;
					break;
				case "Theme color":
					Storage.DanmuColor = (Color)Application.Current.Resources["SystemAccentColor"];
					break;
				case "Pick color":
					ColorPicker picker = new ColorPicker()
					{
						ColorSpectrumComponents = ColorSpectrumComponents.HueSaturation,
						ColorSpectrumShape = ColorSpectrumShape.Ring,
						IsColorPreviewVisible = true,
						IsColorChannelTextInputVisible = true,
						IsHexInputVisible = true,
						Color = Storage.DanmuColor
					};
					ContentDialog dialog = new ContentDialog
					{
						Content = picker,
						Title = "Pick A Color",
						IsSecondaryButtonEnabled = true,
						PrimaryButtonText = "Confirm",
						SecondaryButtonText = "Cancel"
					};
					if (await dialog.ShowAsync() == ContentDialogResult.Primary)
					{
						Storage.DanmuColor = picker.Color;
					}
					else
					{
						combo.SelectedIndex = 0;
						Storage.DanmuColor = Colors.Black;
					}
					break;
				default:
					return;
			}
		}

		private void ComboBox_TextSubmitted(ComboBox sender, ComboBoxTextSubmittedEventArgs args)
		{
			try
			{
				var size = int.Parse(sender.Text);
				if (!sender.Items.Contains(size))
					sender.Items[0] = sender.Text;
				Storage.DanmuSize = size;
			}
			catch (Exception) { }
		}
	}
}
