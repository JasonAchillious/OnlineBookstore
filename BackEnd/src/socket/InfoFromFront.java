package socket;

public class InfoFromFront {
	/**
	 * QueryObject
	 */
	private int UserId = -1;
	private String Type;

	private String UserName;
	private String EncodedPassword;

	private String MainLabel;

	private Integer BookId;
	private Boolean IsBillboard;
	private Integer BookListId;
	private Integer ReviewId;
	private Integer DanmuId;
	private Integer From;
	private Integer Count;

	private Integer SearchType;
	private String QueryText;
	private Boolean OrderDescend;
	private Integer Order;
	private Integer TimeRangeType;
	private int[] TimeRange;
	private int[] PageRange;
	private String[] LabelFilters;
	private Boolean IncludeFreeBooks;

	private Integer Page;

	/**
	 * ChangeObject
	 */

	// private int UserId = -1;
	// private String Type;

	// private String UserName;
	private String Email;
	// private String EncodedPassword;

	// private Integer DanmuId;
	private Boolean IsDeleteAction;
	private String NewContent;

	// private Integer ReviewId;
	private String NewTitle;
	private Integer NewRating;

	private Integer ReadListId;
	private Integer ChangeType;
	private Integer AlteredBookId;
	private String AlteredText;

	// private Integer BookId;
	private Boolean IsAddAction;

	private String Description;

	private String Content;
	private Integer PageNum;

	private String Title;

	private Integer Rating;

	private Boolean IsFollowAction;

	public int getUserId() {
		return UserId;
	}

	public void setUserId(int userId) {
		UserId = userId;
	}

	public String getType() {
		return Type;
	}

	public void setType(String type) {
		Type = type;
	}

	public String getUserName() {
		return UserName;
	}

	public void setUserName(String userName) {
		UserName = userName;
	}

	public String getEncodedPassword() {
		return EncodedPassword;
	}

	public void setEncodedPassword(String encodedPassword) {
		EncodedPassword = encodedPassword;
	}

	public String getMainLabel() {
		return MainLabel;
	}

	public void setMainLabel(String mainLabel) {
		MainLabel = mainLabel;
	}

	public Integer getBookId() {
		return BookId;
	}

	public void setBookId(Integer bookId) {
		BookId = bookId;
	}

	public Boolean getIsBillboard() {
		return IsBillboard;
	}

	public void setIsBillboard(Boolean billboard) {
		IsBillboard = billboard;
	}

	public Integer getBookListID() {
		return BookListId;
	}

	public void setBookListID(Integer bookListID) {
		BookListId = bookListID;
	}

	public Integer getReviewId() {
		return ReviewId;
	}

	public void setReviewId(Integer reviewId) {
		ReviewId = reviewId;
	}

	public Integer getDanmuId() {
		return DanmuId;
	}

	public void setDanmuId(Integer danmuId) {
		DanmuId = danmuId;
	}

	public Integer getFrom() {
		return From;
	}

	public void setFrom(Integer from) {
		From = from;
	}

	public Integer getCount() {
		return Count;
	}

	public void setCount(Integer count) {
		Count = count;
	}

	public Integer getSearchType() {
		return SearchType;
	}

	public void setSearchType(Integer searchType) {
		SearchType = searchType;
	}

	public String getQueryText() {
		return QueryText;
	}

	public void setQueryText(String queryText) {
		QueryText = queryText;
	}

	public Boolean getOrderDescend() {
		return OrderDescend;
	}

	public void setOrderDescend(Boolean orderDescend) {
		OrderDescend = orderDescend;
	}

	public Integer getOrder() {
		return Order;
	}

	public void setOrder(Integer order) {
		Order = order;
	}

	public Integer getTimeRangeType() {
		return TimeRangeType;
	}

	public void setTimeRangeType(Integer timeRangeType) {
		TimeRangeType = timeRangeType;
	}

	public int[] getTimeRange() {
		return TimeRange;
	}

	public void setTimeRange(int[] timeRange) {
		TimeRange = timeRange;
	}

	public int[] getPageRange() {
		return PageRange;
	}

	public void setPageRange(int[] pageRange) {
		PageRange = pageRange;
	}

	public String[] getLabelFilters() {
		return LabelFilters;
	}

	public void setLabelFilters(String[] labelFilters) {
		LabelFilters = labelFilters;
	}

	public Boolean getIncludeFreeBooks() {
		return IncludeFreeBooks;
	}

	public void setIncludeFreeBooks(Boolean includeFreeBooks) {
		this.IncludeFreeBooks = includeFreeBooks;
	}

	public Integer getPage() {
		return Page;
	}

	public void setPage(Integer page) {
		Page = page;
	}

	public Boolean getDeleteAction() {
		return IsDeleteAction;
	}

	public void setDeleteAction(Boolean deleteAction) {
		IsDeleteAction = deleteAction;
	}

	public String getNewContent() {
		return NewContent;
	}

	public void setNewContent(String newContent) {
		NewContent = newContent;
	}

	public Integer getReadListId() {
		return ReadListId;
	}

	public void setReadListId(Integer readListId) {
		ReadListId = readListId;
	}

	public Integer getChangeType() {
		return ChangeType;
	}

	public void setChangeType(Integer changeType) {
		ChangeType = changeType;
	}

	public Integer getAlteredBookId() {
		return AlteredBookId;
	}

	public void setAlteredBookId(Integer alteredBookId) {
		AlteredBookId = alteredBookId;
	}

	public String getAlteredText() {
		return AlteredText;
	}

	public void setAlteredText(String alteredText) {
		AlteredText = alteredText;
	}

	public Boolean getAddAction() {
		return IsAddAction;
	}

	public void setAddAction(Boolean addAction) {
		IsAddAction = addAction;
	}

	public String getDescription() {
		return Description;
	}

	public void setDescription(String description) {
		Description = description;
	}

	public String getContent() {
		return Content;
	}

	public void setContent(String content) {
		Content = content;
	}

	public Integer getPageNum() {
		return PageNum;
	}

	public void setPageNum(Integer pageNum) {
		PageNum = pageNum;
	}

	public String getTitle() {
		return Title;
	}

	public void setTitle(String title) {
		Title = title;
	}

	public Integer getRating() {
		return Rating;
	}

	public void setRating(Integer rating) {
		Rating = rating;
	}

	public String getEmail() {
		return Email;
	}

	public void setEmail(String email) {
		Email = email;
	}

	public String getNewTitle() {
		return NewTitle;
	}

	public void setNewTitle(String newTitle) {
		NewTitle = newTitle;
	}

	public Integer getNewRating() {
		return NewRating;
	}

	public void setNewRating(Integer newRating) {
		NewRating = newRating;
	}

	public Boolean getIsFollowAction() {
		return IsFollowAction;
	}

	public void setIsFollowAction(Boolean isFollowAction) {
		IsFollowAction = isFollowAction;
	}
}
