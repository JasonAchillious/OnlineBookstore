using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Runtime.InteropServices.WindowsRuntime;
using Windows.Foundation;
using Windows.Foundation.Collections;
using Windows.UI.Xaml;
using Windows.UI.Xaml.Controls;
using Windows.UI.Xaml.Controls.Primitives;
using Windows.UI.Xaml.Data;
using Windows.UI.Xaml.Input;
using Windows.UI.Xaml.Media;
using Windows.UI.Xaml.Navigation;

//https://go.microsoft.com/fwlink/?LinkId=234236 上介绍了“用户控件”项模板

namespace Frontend.CustomControls
{
	public sealed partial class TerminalControl : UserControl
	{
		public TerminalControl()
		{
			this.InitializeComponent();
		}

		public string SQLCode { get => input.Text; }

		private void Input_DoubleTapped(object sender, DoubleTappedRoutedEventArgs e)
		{
			display.Visibility = Visibility.Visible;
			input.Visibility = Visibility.Collapsed;
		}

		private void Input_LostFocus(object sender, RoutedEventArgs e)
		{
			display.Visibility = Visibility.Visible;
			input.Visibility = Visibility.Collapsed;
		}

		private void Display_GotFocus(object sender, RoutedEventArgs e)
		{
			display.Visibility = Visibility.Collapsed;
			input.Visibility = Visibility.Visible;
		}
	}
}
