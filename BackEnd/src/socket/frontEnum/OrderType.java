package socket.frontEnum;

public class OrderType {

	public enum BooksOrderType {
		Recommend, Time, Rating, Price, Discount, ReviewAmount, BuyAmount, DanmuAmount, PreviewAmount, PageCount
	}

	public enum BillboardsOrderType {
		Recommend, Time
	}

	public enum ReadlistsOrderType {
		Recommend, Time, FollowAmount
	}
}
