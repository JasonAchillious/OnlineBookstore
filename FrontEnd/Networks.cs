using Newtonsoft.Json;
using System;
using System.Collections.Generic;
using System.Diagnostics;
using System.Net;
using System.Net.Sockets;
using System.Text;
using System.Threading.Tasks;

namespace Frontend
{
	public interface IJsonable
	{
		int UserId { set; get; }
		string Type { set; get; }
		string ToJson();
	}

	public class QueryObject : IJsonable
	{
		public int UserId { set; get; } = -1;
		public string Type { set; get; }

		public string UserName { set; get; }
		public string EncodedPassword { set; get; }
		public string MainLabel { set; get; }
		public int? BookId { set; get; }
		public bool? IsBillboard { set; get; }
		public int? BookListId { set; get; }
		public int? ReviewId { set; get; }
		public int? DanmuId { set; get; }
		public int? From { set; get; }
		public int? Count { set; get; }

		public int? SearchType { set; get; }
		public string QueryText { set; get; }
		public bool? OrderDescend { set; get; }
		public int? Order { set; get; }
		public int? TimeRangeType { set; get; }
		public int[] TimeRange { set; get; }
		public int[] PageRange { set; get; }
		public string[] LabelFilters { set; get; }
		public bool? IncludeFreeBooks { set; get; }

		public int? Page { set; get; }

		public QueryObject() { }

		public QueryObject(string type)
		{
			Type = type;
		}

		public string ToJson()
		{
			JsonSerializerSettings settings = new JsonSerializerSettings
			{
				//DefaultValueHandling = DefaultValueHandling.Ignore,
				Formatting = Formatting.Indented,
				NullValueHandling = NullValueHandling.Ignore
			};
			return JsonConvert.SerializeObject(this, settings);
		}
	}

	public class ChangeObject : IJsonable
	{
		public int UserId { set; get; } = -1;
		public string Type { set; get; }

		public string UserName { set; get; }
		public string Email { set; get; }
		public string EncodedPassword { set; get; }

		public int? DanmuId { set; get; }
		public bool? IsDeleteAction { set; get; }
		public string NewContent { set; get; }

		public int? ReviewId { set; get; }
		public string NewTitle { set; get; }
		public int? NewRating { set; get; }

		public int? ReadListId { set; get; }
		public int? ChangeType { set; get; }
		public int? AlteredBookId { set; get; }
		public string AlteredText { set; get; }

		public int? BookId { set; get; }
		public bool? IsAddAction { set; get; }

		public string Description { set; get; }

		public string Content { set; get; }
		public int? PageNum { set; get; }

		public string Title { set; get; }

		public int? Rating { set; get; }

		public bool? IsFollowAction { set; get; }

		public ChangeObject() { }

		public ChangeObject(string type)
		{
			Type = type;
		}

		public string ToJson()
		{
			JsonSerializerSettings settings = new JsonSerializerSettings
			{
				Formatting = Formatting.Indented,
				NullValueHandling = NullValueHandling.Ignore
			};
			return JsonConvert.SerializeObject(this, settings);
		}
	}

	public class AdminObject : IJsonable
	{
		public int UserId { set; get; } = -1;
		public string Type { set; get; }
		public string SQL { set; get; }
		public int? BillboardId { set; get; }
		public int? ChangeType { set; get; }
		public int? AlteredBookId { set; get; }
		public string AlteredText { set; get; }

		public AdminObject(string type)
		{
			this.Type = type;
		}

		public string ToJson()
		{
			JsonSerializerSettings settings = new JsonSerializerSettings
			{
				Formatting = Formatting.Indented,
				NullValueHandling = NullValueHandling.Ignore
			};
			return JsonConvert.SerializeObject(this, settings);
		}
	}

	public class ReceiveObject
	{
		public bool Success { set; get; } = false;

		public int? LoginStatus { set; get; }
		public int? UserId { set; get; }
		public bool? IsAdmin { set; get; }

		public string[] MainLabels { set; get; }
		public string[] SubLabels { set; get; }

		public string BookCoverUrl { set; get; }
		public string BookName { set; get; }
		public string AuthorName { set; get; }
		public string MainAndSubLabel { set; get; }
		public double? Price { set; get; }
		public int? Discount { set; get; }
		public double? OverallRating { set; get; }
		public string OtherAuthors { set; get; }
		public string PublishInfo { set; get; }
		public string ISBN { set; get; }
		public int? BuyAmount { set; get; }
		public int? DanmuAmount { set; get; }
		public int? PreviewAmount { set; get; }
		public int? ReviewAmount { set; get; }
		public int? PageCount { set; get; }
		public bool? CanAddReadList { set; get; }
		public bool? CanAddWishList { set; get; }
		public bool? CanBuy { set; get; }

		public int[] IDs { set; get; }

		public string CreateUser { set; get; }
		public int? Rating { set; get; }
		public long? TimeStap { set; get; }
		public string Title { set; get; }
		public string Content { set; get; }


		public string Description { set; get; }
		public int? FollowAmount { set; get; }
		public bool? Followed { set; get; }

		public int? PageNum { set; get; }

		public string URL { set; get; }

		public string PrivateKey { set; get; }

		public string Message { set; get; }

		public static ReceiveObject FromJson(string json)
		{
			try
			{
				return JsonConvert.DeserializeObject(json, typeof(ReceiveObject)) as ReceiveObject;
			}
			catch (Exception)
			{
				return null;
			}
		}
	}

	public class Connection
	{
		public static string REMOTE_IP = null;
		public static int REMOTE_PORT_USER, REMOTE_PORT_ADMIN;

		private readonly bool isAdmin;

		private Socket socket;

		private Connection(bool portAdmin = false)
		{
			this.isAdmin = portAdmin;
		}

		internal async Task Reconnect()
		{
			if (socket != null)
			{
				Close();
			}
			while (REMOTE_IP == null || REMOTE_PORT_USER == 0 || REMOTE_PORT_ADMIN == 0)
				await Task.Delay(Util.REFRESH_RATE);

			IPAddress ip = IPAddress.Parse(REMOTE_IP);
			IPEndPoint ipEnd = new IPEndPoint(ip, this.isAdmin ? REMOTE_PORT_ADMIN : REMOTE_PORT_USER);
			this.socket = new Socket(AddressFamily.InterNetwork, SocketType.Stream, ProtocolType.Tcp);
			try
			{
				Debug.WriteLine("Start connecting...");
				await socket.ConnectAsync(ipEnd);
			}
			catch (SocketException)
			{
				Debug.WriteLine("Fail to connect server, re-connecting...");
				await Task.Delay(Util.REFRESH_RATE);
				await this.Reconnect();
			}
		}

		private bool SocketConnected {
			get {
				try
				{
					return !(socket == null || socket.Poll(1000, SelectMode.SelectRead) && socket.Available == 0);
				}
				catch (Exception)
				{
					return false;
				}
			}
		}

		private async Task<int> Send(ArraySegment<byte> buffer)
		{
			int sendLen = await this.socket.SendAsync(buffer, SocketFlags.None);
			this.socket.Shutdown(SocketShutdown.Send);
			return sendLen;
		}

		private async Task<int> Receive(ArraySegment<byte> buffer)
		{
			int receiveLen = await this.socket.ReceiveAsync(buffer, SocketFlags.None);
			//this.socket.Shutdown(SocketShutdown.Receive);
			return receiveLen;
		}

		private void Close()
		{
			try
			{
				this.socket.Shutdown(SocketShutdown.Both);
			}
			catch (Exception)
			{
				//Debug.WriteLine("Socket shutdown error: " + e.Message);
			}
			this.socket.Close();
		}

		/// <summary>
		/// Singleton user
		/// </summary>
		private static Connection Instance { get; } = new Connection();

		/// <summary>
		/// Singleton admin
		/// </summary>
		private static Connection InstanceAdmin { get; } = new Connection(true);

		/// <summary>
		/// Send to remote host with user id
		/// </summary>
		/// <param name="query"> A copied <code>QueryObject</code> </param>
		private static async Task<ReceiveObject> SendWithUser(IJsonable query)
		{
			if (Storage.Test)
				return new ReceiveObject();

			if (!Instance.SocketConnected)
			{
				await Instance.Reconnect();
			}
			query.UserId = Storage.UserId;
			var send = Encoding.UTF8.GetBytes(query.ToJson());
			byte[] receive;
			try
			{
				int sendLen = await Instance.Send(send);
				Debug.WriteLine("Send of \"{0}\" finish, total {1} bytes.", query.Type, sendLen);

				receive = new byte[1 << 14];
				int recvLen = await Instance.Receive(receive);
				Debug.WriteLine("Receive finish, total {0} bytes.", recvLen);

				Instance.Close();
			}
			catch (Exception)
			{
				await Instance.Reconnect();
				return await SendWithUser(query); // recurrence
			}
			var json = Encoding.UTF8.GetString(receive);
			var receiveObj = ReceiveObject.FromJson(json);
			if (receiveObj == null)
			{
				await Instance.Reconnect();
				return await SendWithUser(query); // recurrence
			}
			return receiveObj;
		}

		/// <summary>
		/// Delegate of private static async send and receive message method
		/// </summary>
		internal static readonly Func<IJsonable, Task<ReceiveObject>> SendAndReceive = SendWithUser;


		/// <summary>
		/// Send to remote host with user id of admin
		/// </summary>
		/// <param name="query"> A copied <code>QueryObject</code> </param>
		private static async Task<string> SendAdminWithUser(IJsonable query)
		{
			if (Storage.Test)
				return "";

			if (!InstanceAdmin.SocketConnected)
			{
				await InstanceAdmin.Reconnect();
			}
			query.UserId = Storage.UserId;
			var send = Encoding.UTF8.GetBytes(query.ToJson());
			byte[] receive;
			try
			{
				int sendLen = await InstanceAdmin.Send(send);
				Debug.WriteLine("Send of \"{0}\" finish, total {1} bytes.", query.Type, sendLen);

				receive = new byte[1 << 14];
				int recvLen = await InstanceAdmin.Receive(receive);
				Debug.WriteLine("Receive finish, total {0} bytes.", recvLen);

				InstanceAdmin.Close();
			}
			catch (Exception)
			{
				await InstanceAdmin.Reconnect();
				return await SendAdminWithUser(query); // recurrence
			}
			var str = Encoding.UTF8.GetString(receive);
			if (str == null || str.Trim().Length == 0)
			{
				await InstanceAdmin.Reconnect();
				return await SendAdminWithUser(query); // recurrence
			}
			return str;
		}

		/// <summary>
		/// Delegate of private static async send and receive message method of admin
		/// </summary>
		internal static readonly Func<IJsonable, Task<string>> SendAdminAndReceive = SendAdminWithUser;

	}

	public static class NetworkGet
	{
		internal static bool IsValidID(int id)
		{
			return id > 0;
		}

		public static async Task<LoginStatus> Login(string UserName, string EncodedPassword)
		{
			var query = new QueryObject("Login")
			{
				UserName = UserName,
				EncodedPassword = EncodedPassword
			};
			if (Storage.Test)
			{
				Storage.UserId = 1;
				Storage.IsAdmin = true;
				return LoginStatus.Success;
			}

			var recv = await Connection.SendAndReceive.GlobalLock(query);
			Storage.UserId = recv.UserId ?? -1;
			Storage.IsAdmin = recv.IsAdmin ?? false;
			return (LoginStatus)recv.LoginStatus.Value;
		}

		public static async Task<string[]> GetMainLabels()
		{
			var query = new QueryObject("GetMainLabels")
			{
			};
			if (Storage.Test)
				return new string[]
				{
					"文学",
					"流行",
					"文化",
					"生活",
					"经管",
					"科技"
				};
			var recv = await Connection.SendAndReceive.GlobalLock(query);
			return recv.MainLabels;
		}

		public static async void GetSubLabels(Label label)
		{
			var query = new QueryObject("GetSubLabels")
			{
				MainLabel = label.Name
			};
			if (Storage.Test)
			{
				foreach (var sub in new string[] { "小说", "随笔", "散文", "诗歌", "童话", "名著", "科幻", "言情", "青春" })
				{
					label.AllSubs.Add(new SubLabel(sub, label));
					label.OnPropertyChanged("HotSubs");
				}
				return;
			}

			var recv = await Connection.SendAndReceive.GlobalLock(query);
			foreach (var sub in recv.SubLabels)
			{
				label.AllSubs.Add(new SubLabel(sub, label));
				label.OnPropertyChanged("HotSubs");
			}
		}

		private static string RandomName()
		{
			return new string[]
			{
				"松浦彌太郎說：假如我現在25歲，最想做的50件事",
				"The Road Less Traveled (少有人走的路)"
			}[new Random().Next(2)];
		}

		public static async Task GetBookSummary(BookSummary book)
		{
			var query = new QueryObject("GetBookSummary")
			{
				BookId = book.ID
			};
			if (Storage.Test)
			{
				book.BookCover = new Windows.UI.Xaml.Media.Imaging.BitmapImage(
				new Uri("https://gss0.baidu.com/7LsWdDW5_xN3otqbppnN2DJv/doc/pic/item/6159252dd42a283478df074e58b5c9ea15cebf7d.jpg"));
				book.BookFullName = RandomName();
				book.BookName = book.BookFullName.CutString();
				book.AuthorName = "松浦彌太郎";
				return;
			}

			var recv = await Connection.SendAndReceive.GlobalLock(query);
			book.BookCover = new Windows.UI.Xaml.Media.Imaging.BitmapImage(new Uri(recv.BookCoverUrl));
			book.BookFullName = recv.BookName;
			book.BookName = book.BookFullName.CutString();
			book.AuthorName = recv.AuthorName;
		}

		public static async Task GetBookQuasiDetail(BookDetail book)
		{
			var query = new QueryObject("GetBookQuasiDetail")
			{
				BookId = book.ID
			};
			if (Storage.Test)
			{
				book.BookCover = new Windows.UI.Xaml.Media.Imaging.BitmapImage(
				new Uri("https://gss0.baidu.com/7LsWdDW5_xN3otqbppnN2DJv/doc/pic/item/6159252dd42a283478df074e58b5c9ea15cebf7d.jpg"));
				book.BookFullName = RandomName();
				book.BookName = book.BookFullName.CutString();
				book.AuthorName = "松浦彌太郎";
				book.Labels = "生活 - 随笔";
				book.Price = 28;
				book.Discount = 95;
				book.OverallRating = 4.5;
				return;
			}

			var recv = await Connection.SendAndReceive.GlobalLock(query);
			book.BookCover = new Windows.UI.Xaml.Media.Imaging.BitmapImage(
									new Uri(recv.BookCoverUrl));
			book.BookFullName = recv.BookName;
			book.BookName = book.BookFullName.CutString();
			book.AuthorName = recv.AuthorName;
			book.Labels = recv.MainAndSubLabel;
			book.Price = recv.Price.Value;
			book.Discount = recv.Discount.Value;
			book.OverallRating = recv.OverallRating.Value;
		}

		public static async Task GetBookDetail(BookDetail book)
		{
			var query = new QueryObject("GetBookDetail")
			{
				BookId = book.ID
			};
			if (Storage.Test)
			{
				book.Labels = "生活 - 随笔";
				book.Price = 28;
				book.Discount = 95;
				book.OverallRating = 4.5;
				book.BookDescription = "九月一日瓦官教寺住持克勤載拜致書于延曆堂上座主大和尚侍者夫道盛" +
					"于得人而衰于失人事成于有為而敗于無為此古今之確論也自吾佛之教入中國中國之人莫不論者宗" +
					"論經者宗教而各有其師及我天台生陳隋之朝以一大藏教序以五時列為八教開闡大塗為一宗正" +
					"傳使海內外咸被佛之聲教於戲盛乎唐之大曆間至興道尊者為";
				book.OtherAuthors = "Daniel Kahneman (translator)";
				book.PublishInfo = "北京联合出版有限责任公司 / 2005-10-05 / 第一版";
				book.ISBN = "9787725426293";
				book.BuyAmount = 505;
				book.DanmuAmount = 1835;
				book.PreviewAmount = 2200;
				book.ReviewAmount = 112;
				book.PageCount = 280;
				book.CanAddReadList = IsValidID(Storage.UserId);
				book.CanAddWishList = IsValidID(Storage.UserId);
				book.CanBuy = IsValidID(Storage.UserId);
				await GetReviewContents(book);
				await book.RelatedBooks.Reload();
				book.Finished = true;
				return;
			}

			var recv = await Connection.SendAndReceive.GlobalLock(query);
			book.BookDescription = recv.Description;
			book.Labels = recv.MainAndSubLabel;
			book.OtherAuthors = recv.OtherAuthors;
			book.PublishInfo = recv.PublishInfo;
			book.ISBN = recv.ISBN;
			book.Price = recv.Price.Value;
			book.Discount = recv.Discount.Value;
			book.OverallRating = recv.OverallRating.Value;
			book.BuyAmount = recv.BuyAmount.Value;
			book.DanmuAmount = recv.DanmuAmount.Value;
			book.PreviewAmount = recv.PreviewAmount.Value;
			book.ReviewAmount = recv.ReviewAmount.Value;
			book.PageCount = recv.PageCount.Value;
			book.OverallRating = recv.OverallRating.Value;
			book.CanAddReadList = recv.CanAddReadList.Value;
			book.CanAddWishList = recv.CanAddWishList.Value;
			book.CanBuy = recv.CanBuy.Value;
			await GetReviewContents(book);
			await book.RelatedBooks.Reload();
			book.Finished = true;
		}

		internal static async Task GetReviewContents(BookDetail book, bool setFinish = false, int from = 0,
													 int count = Util.REVIEW_AMOUNT_ONE_TIME)
		{
			var reviewIds = await GetBookReviews(book.ID, from, count);
			foreach (var rid in reviewIds)
			{
				var review = new Review(rid);
				await GetReview(review);
				book.Reviews.Add(review);
			}
			if (setFinish)
				book.Finished = true;
		}

		private static int[] GenerateIDs(int count)
		{
			List<int> list = new List<int>(count);
			for (int i = 10; i < 10 + count; ++i)
			{
				list.Add(i);
			}
			return list.ToArray();
		}

		public static async Task<int[]> GetBookReviews(int bookId, int from, int count)
		{
			var query = new QueryObject("GetBookReviews")
			{
				BookId = bookId,
				From = from,
				Count = count
			};
			if (Storage.Test)
				return GenerateIDs(count);

			var recv = await Connection.SendAndReceive.GlobalLock(query);
			return recv.IDs;
		}

		public static async Task GetReview(Review review)
		{
			var query = new QueryObject("GetReview")
			{
				ReviewId = review.ID
			};
			if (Storage.Test)
			{
				review.UserName = "Rk9LX2kC";
				review.PublishDate = DateTime.Now;
				review.Rating = 5;
				review.Content = "Sometimes you need to show ratings of secondary content, " +
					"such as that displayed in recommended content or when displaying a list " +
					"of comments and their corresponding ratings. In this case, the user " +
					"shouldn’t be able to edit the rating, so you can make the control read-only.";
				review.Title = "asgase5g1";
				review.BookName = "The Road Less Traveled (少有人走的路)";
				return;
			}

			var recv = await Connection.SendAndReceive.GlobalLock(query);
			review.UserName = recv.CreateUser;
			review.PublishDate = recv.TimeStap.Value.GetTime();
			review.Rating = recv.Rating.Value;
			review.Content = recv.Content;
			review.Title = recv.Title;
			review.BookName = recv.BookName;
		}

		public static async Task<int[]> GetBookListBooks(bool isBillboard, int id, int from = 0,
														 int count = Util.PREVIEW_AMOUNT)
		{
			var query = new QueryObject("GetBookListBooks")
			{
				IsBillboard = isBillboard,
				BookListId = id,
				From = from,
				Count = count
			};
			if (Storage.Test)
				return GenerateIDs(count == int.MaxValue ? 8 : count);

			var recv = await Connection.SendAndReceive.GlobalLock(query);
			return recv.IDs;
		}

		public static async Task<int[]> GetShelfBooks()
		{
			var query = new QueryObject("GetShelfBooks")
			{
			};
			if (Storage.Test)
				return GenerateIDs(10);

			var recv = await Connection.SendAndReceive.GlobalLock(query);
			return recv.IDs;
		}

		public static async Task<int[]> GetFromQuery(QueryObject query, int from = 0, int count = int.MaxValue)
		{
			if (!query.SearchType.HasValue && query.IsBillboard.HasValue && query.BookListId.HasValue)
			{
				if (query.From.HasValue && query.Count.HasValue)
					return await GetBookListBooks(query.IsBillboard.Value, query.BookListId.Value,
												  query.From.Value, query.Count.Value);
				else
					return await GetBookListBooks(query.IsBillboard.Value, query.BookListId.Value);
			}
			else if (!query.SearchType.HasValue)
			{
				Debug.WriteLine("GetFromQuery has no SearchType");
				return null;
			}
			var newQuery = query.CloneThroughJson();
			newQuery.Type = "GetFromQuery";
			newQuery.From = from;
			newQuery.Count = count;

			if (Storage.Test)
				return GenerateIDs(count);

			var recv = await Connection.SendAndReceive.GlobalLock(newQuery);
			return recv.IDs;
		}

		internal readonly static QueryObject NewBooks =
			new QueryObject()
			{
				SearchType = 0,
				OrderDescend = true,
				Order = 1
			};

		internal readonly static QueryObject TopBooks =
			new QueryObject()
			{
				SearchType = 0,
				OrderDescend = true,
				Order = 6
			};

		internal readonly static QueryObject PersonalRecommend =
			new QueryObject()
			{
				SearchType = 0,
				OrderDescend = false,
				Order = 0
			};

		internal static QueryObject BillboardRecommend =
			new QueryObject()
			{
				SearchType = 1,
				OrderDescend = false,
				Order = 0
			};

		internal readonly static QueryObject ReadListRecommend =
			new QueryObject()
			{
				SearchType = 2,
				OrderDescend = false,
				Order = 0
			};

		internal static QueryObject BillboardTop =
			new QueryObject()
			{
				SearchType = 1,
				OrderDescend = true,
				Order = 1
			};

		internal readonly static QueryObject ReadListTop =
			new QueryObject()
			{
				SearchType = 2,
				OrderDescend = true,
				Order = 2
			};

		internal static async Task<bool> GetBookSummaryContents(BookSummaryCollection collection, int[] ids)
		{
			foreach (int id in ids)
			{
				var book = new BookSummary(id);
				await GetBookSummary(book);
				collection.Books.Add(book);
			}
			return true;
		}

		internal static async Task<bool> GetBookQuasiDetailContents(BookDetailCollection collection, int[] ids)
		{
			foreach (int id in ids)
			{
				var book = new BookDetail(id);
				await GetBookQuasiDetail(book);
				collection.Books.Add(book);
			}
			return true;
		}

		public static async Task<int[]> GetRelatedBooks(int BookId, int from = 0, int count = Util.RELATE_BOOK_AMOUNT)
		{
			var query = new QueryObject("GetRelatedBooks")
			{
				BookId = BookId,
				From = from,
				Count = count
			};
			if (Storage.Test)
				return GenerateIDs(count);

			var recv = await Connection.SendAndReceive.GlobalLock(query);
			return recv.IDs;
		}

		public static async Task GetTitleDescription(BookDetailCollection collection, bool isBillboard, int id)
		{
			var query = new QueryObject("GetTitleDescription")
			{
				IsBillboard = isBillboard,
				BookListId = id
			};
			if (Storage.Test)
			{
				collection.Title = "Bo4PSSm0";
				collection.Description = "Bo4PSSm0Po83pIiC";
				if (isBillboard)
					return;
				collection.CreateUser = "Aq4K9ycU";
				collection.EditTime = DateTime.Now;
				collection.FollowAmount = 123;
				collection.Followed = false;
				return;
			}

			var recv = await Connection.SendAndReceive.GlobalLock(query);
			collection.Title = recv.Title;
			collection.Description = recv.Description;
			collection.OnPropertyChanged("Title");
			collection.OnPropertyChanged("Description");
			if (isBillboard)
				return;
			collection.CreateUser = recv.CreateUser;
			collection.EditTime = recv.TimeStap.Value.GetTime();
			collection.FollowAmount = recv.FollowAmount.Value;
			collection.Followed = recv.Followed.Value;
			collection.OnPropertyChanged("CreateUser");
			collection.OnPropertyChanged("EditTime");
			collection.OnPropertyChanged("FollowAmount");
			collection.OnPropertyChanged("Followed");
		}

		public static async Task<int[]> GetMyWishlist()
		{
			var query = new QueryObject("GetMyWishlist")
			{
			};
			if (Storage.Test)
				return GenerateIDs(5);

			var recv = await Connection.SendAndReceive.GlobalLock(query);
			return recv.IDs;
		}

		public static async Task<int[]> GetMyDanmus()
		{
			var query = new QueryObject("GetMyDanmus")
			{
			};
			if (Storage.Test)
				return GenerateIDs(15);

			var recv = await Connection.SendAndReceive.GlobalLock(query);
			return recv.IDs;
		}

		public static async Task<int[]> GetMyReviews()
		{
			var query = new QueryObject("GetMyReviews")
			{
			};
			if (Storage.Test)
				return GenerateIDs(9);

			var recv = await Connection.SendAndReceive.GlobalLock(query);
			return recv.IDs;
		}

		public static async Task<int[]> GetDanmuOfBook(int bookId, uint page)
		{
			var query = new QueryObject("GetDanmuOfBook")
			{
				BookId = bookId,
				Page = (int)page
			};
			if (Storage.Test)
				return GenerateIDs(25);

			var recv = await Connection.SendAndReceive.GlobalLock(query);
			return recv.IDs;
		}

		public static async Task GetDanmuContent(Danmu danmu)
		{
			var query = new QueryObject("GetDanmuContent")
			{
				DanmuId = danmu.ID
			};
			if (Storage.Test)
			{
				danmu.Content = "6666666666666666";
				return;
			}

			var recv = await Connection.SendAndReceive.GlobalLock(query);
			danmu.Content = recv.Content;
		}

		public static async Task GetFullDanmuContent(FullDanmu danmu)
		{
			var query = new QueryObject("GetFullDanmuContent")
			{
				DanmuId = danmu.ID
			};
			if (Storage.Test)
			{
				danmu.Content = "6666666666666666";
				danmu.BookName = "松浦彌太郎說：假如我現在25歲，最想做的50件事";
				danmu.EditTime = DateTime.Now;
				danmu.PageNum = 102;
				return;
			}

			var recv = await Connection.SendAndReceive.GlobalLock(query);
			danmu.Content = recv.Content;
			danmu.BookName = recv.BookName;
			danmu.EditTime = recv.TimeStap.Value.GetTime();
			danmu.PageNum = recv.PageNum.Value;
		}

		public static async Task<int[]> GetMyCreatedReadLists()
		{
			var query = new QueryObject("GetMyCreatedReadLists")
			{
			};
			if (Storage.Test)
				return GenerateIDs(5);

			var recv = await Connection.SendAndReceive.GlobalLock(query);
			return recv.IDs;
		}

		public static async Task<int[]> GetMyFollowedReadLists()
		{
			var query = new QueryObject("GetMyFollowedReadLists")
			{
			};
			if (Storage.Test)
				return GenerateIDs(5);

			var recv = await Connection.SendAndReceive.GlobalLock(query);
			return recv.IDs;
		}

		public static async Task<int[]> GetMyReadListsWithout(int bookId)
		{
			var query = new QueryObject("GetMyReadListsWithout")
			{
				BookId = bookId
			};
			if (Storage.Test)
				return GenerateIDs(4);

			var recv = await Connection.SendAndReceive.GlobalLock(query);
			return recv.IDs;
		}

		public static async Task<string> GetBookPreview(int bookId)
		{
			var query = new QueryObject("GetBookPreview")
			{
				BookId = bookId
			};
			if (Storage.Test)
				return "http://www.africau.edu/images/default/sample.pdf";

			var recv = await Connection.SendAndReceive.GlobalLock(query);
			return recv.URL;
		}

		public static async Task<string> DownloadBook(int bookId)
		{
			var query = new QueryObject("DownloadBook")
			{
				BookId = bookId
			};
			if (Storage.Test)
				return "https://gahp.net/wp-content/uploads/2017/09/sample.pdf";

			var recv = await Connection.SendAndReceive.GlobalLock(query);
			return recv.URL;
		}

		public static async Task<string> GetBookKey(int bookId)
		{
			var query = new QueryObject("GetBookKey")
			{
				BookId = bookId
			};
			if (Storage.Test)
				return "as1dg56aseg";

			var recv = await Connection.SendAndReceive.GlobalLock(query);
			return recv.PrivateKey;
		}

		internal static async Task<double> GetAddPrice(int id)
		{
			var query = new QueryObject("GetAddPrice")
			{
				BookId = id
			};
			if (Storage.Test)
				return 10;

			var recv = await Connection.SendAndReceive.GlobalLock(query);
			return recv.Price.Value;
		}
	}


	public static class NetworkSet
	{
		public static async Task<bool> Logout()
		{
			var change = new ChangeObject("Logout")
			{
			};
			if (Storage.Test)
			{
				Storage.UserId = -1;
				Storage.IsAdmin = false;
				return true;
			}

			var recv = await Connection.SendAndReceive.GlobalLock(change);
			if (recv.Success)
			{
				Storage.UserId = -1;
				Storage.IsAdmin = false;
			}
			return recv.Success;
		}

		public static async Task<bool> SignUp(string userName, string email, string password)
		{
			var change = new ChangeObject("SignUp")
			{
				UserName = userName,
				Email = email,
				EncodedPassword = password
			};
			if (Storage.Test)
				return true;

			var recv = await Connection.SendAndReceive.GlobalLock(change);
			return recv.Success;
		}

		public static async Task<bool> ChangeDanmu(int danmuId, bool isDeleteAction,
												   string newContent = null)
		{
			var change = new ChangeObject("ChangeDanmu")
			{
				DanmuId = danmuId,
				IsDeleteAction = isDeleteAction,
				NewContent = newContent
			};
			if (Storage.Test)
				return true;

			var recv = await Connection.SendAndReceive.GlobalLock(change);
			return recv.Success;
		}

		public static async Task<bool> ChangeReadList(int readListId, BookListChangeType changeType,
													  int? alteredId = null, string alteredText = null)
		{
			if (Util.MainElem.CurrentPage == typeof(BillboardPage) && Storage.IsAdmin)
			{
				return await NetworkAdmin.ChangeBillboard(readListId, changeType, alteredId, alteredText);
			}

			var change = new ChangeObject("ChangeReadList")
			{
				ReadListId = readListId,
				ChangeType = (int)changeType,
				AlteredBookId = alteredId,
				AlteredText = alteredText
			};
			if (Storage.Test)
				return true;

			var recv = await Connection.SendAndReceive.GlobalLock(change);
			return recv.Success;
		}

		public static async Task<bool> ChangeWishlist(int bookId, bool isAddAction)
		{
			var change = new ChangeObject("ChangeWishlist")
			{
				BookId = bookId,
				IsAddAction = isAddAction
			};
			if (Storage.Test)
				return true;

			var recv = await Connection.SendAndReceive.GlobalLock(change);
			return recv.Success;
		}

		public static async Task<bool> CreateDanmu(string content, int bookId, uint pageNum)
		{
			var change = new ChangeObject("CreateDanmu")
			{
				Content = content,
				BookId = bookId,
				PageNum = (int)pageNum
			};
			if (Storage.Test)
				return true;

			var recv = await Connection.SendAndReceive.GlobalLock(change);
			return recv.Success;
		}

		public static async Task<bool> CreateReadList(string title, string desc)
		{
			var change = new ChangeObject("CreateReadList")
			{
				Description = desc,
				Title = title
			};
			if (Storage.Test)
				return true;

			var recv = await Connection.SendAndReceive.GlobalLock(change);
			return recv.Success;
		}

		public static async Task<string> BuyBook(int bookId)
		{
			var change = new ChangeObject("BuyBook")
			{
				BookId = bookId
			};
			if (Storage.Test)
				return "https://www.w3.org/WAI/ER/tests/xhtml/testfiles/resources/pdf/dummy.pdf";

			var recv = await Connection.SendAndReceive.GlobalLock(change);
			if (recv.Success && recv.URL != null)
			{
				return recv.URL;
			}
			else
			{
				return null;
			}
		}

		public static async Task<bool> ChangeReview(int reviewId, bool isDelete, int rating, string title, string content)
		{
			var change = new ChangeObject("ChangeReview")
			{
				ReviewId = reviewId,
				NewTitle = title,
				NewContent = content,
				NewRating = rating,
				IsDeleteAction = isDelete
			};
			if (Storage.Test)
				return true;

			var recv = await Connection.SendAndReceive.GlobalLock(change);
			return recv.Success;
		}

		public static async Task<bool> CreateReview(int bookId, int rating, string title, string content)
		{
			var change = new ChangeObject("CreateReview")
			{
				BookId = bookId,
				Rating = rating,
				Title = title,
				Content = content
			};
			if (Storage.Test)
				return true;

			var recv = await Connection.SendAndReceive.GlobalLock(change);
			return recv.Success;
		}

		public static async Task<bool> CheckBuyComplete(int bookId)
		{
			var change = new ChangeObject("CheckBuyComplete")
			{
				BookId = bookId,
			};
			if (Storage.Test)
				return true;

			var recv = await Connection.SendAndReceive.GlobalLock(change);
			return recv.Success;
		}

		public static async Task<bool> CancelTransaction(int bookId)
		{
			var change = new ChangeObject("CancelTransaction")
			{
				BookId = bookId,
			};
			if (Storage.Test)
				return true;

			var recv = await Connection.SendAndReceive.GlobalLock(change);
			return recv.Success;
		}

		internal static async Task<bool> FollowReadList(int readListId, bool isFollowAction)
		{
			var change = new ChangeObject("FollowReadList")
			{
				ReadListId = readListId,
				IsFollowAction = isFollowAction,
			};
			if (Storage.Test)
				return true;

			var recv = await Connection.SendAndReceive.GlobalLock(change);
			return recv.Success;
		}
	}

	public class NetworkAdmin
	{
		public static async Task<string> PerformSQL(string sql)
		{
			var admin = new AdminObject("PerformSQL")
			{
				SQL = sql
			};
			var recv = await Connection.SendAdminAndReceive.GlobalLock(admin);
			return recv;
		}

		public static async Task<bool> ChangeBillboard(int id, BookListChangeType changeType,
													  int? alteredId = null, string alteredText = null)
		{
			var admin = new AdminObject("ChangeBillboard")
			{
				BillboardId = id,
				ChangeType = (int)changeType,
				AlteredBookId = alteredId,
				AlteredText = alteredText
			};
			if (Storage.Test)
				return true;

			var recv = await Connection.SendAdminAndReceive.GlobalLock(admin);
			var obj = JsonConvert.DeserializeObject(recv, typeof(ReceiveObject)) as ReceiveObject;
			if (obj != null && obj.Message != null && obj.Message.Trim().Length > 0)
			{
				var dialog = new Windows.UI.Xaml.Controls.ContentDialog()
				{
					Content = obj.Message.Trim(),
					Title = "Edit Billboard Result",
					IsSecondaryButtonEnabled = false,
					PrimaryButtonText = "OK"
				};
				await dialog.ShowAsync();
			}
			return obj != null && obj.Success;
		}
	}
}
