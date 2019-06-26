namespace Frontend
{
	public enum LoginStatus
	{
		Success,
		NoSuchUser,
		WrongPassword
	}

	public enum BookListChangeType
	{
		AddBook,
		RemoveList,
		DeleteBook,
		ChangeTitle,
		ChangeDescription
	}

	public enum ContentType
	{
		Books,
		Billboards,
		ReadLists
	}

	public enum BooksOrderType
	{
		Recommend,
		Time,
		Rating,
		Price,
		Discount,
		ReviewAmount,
		BuyAmount,
		DanmuAmount,
		PreviewAmount,
		PageCount
	}

	public enum BillboardsOrderType
	{
		Recommend,
		Time
	}

	public enum ReadlistsOrderType
	{
		Recommend,
		Time,
		FollowAmount
	}

	public enum TimeSpanType
	{
		All,
		Year,
		Month,
		Week
	}
}
