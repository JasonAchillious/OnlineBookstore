package socket;

import java.util.List;

import socket.frontEnum.LoginStatus;

public class InfoToFront {

	private String Type;

	// Always return
	private boolean Success;

	// Type: Login
	private Integer LoginStatus;
	private Integer UserId;
	private Boolean IsAdmin;

	// Type: GetMainLabels
	private List<String> MainLabels;
	// Type: GetSubLabels
	private List<String> SubLabels;

	// Type: GetBookSummary
	private String BookCoverUrl;
	private String BookName;
	private String AuthorName;

	// Type: GetBookQuasiDetail: Followings + above "GetBookSummary"
	private String MainAndSubLabel;
	private Double Price; // non-negative
	private Integer Discount; // (0 < Discount < 100)
	private Double OverallRating;

	// Type: GetBookDetail: Doesn't need to return the info of "GetBookSummary".
	// But need to return the data of "GetBookQuasiDetail".
	private Integer BuyAmount;
	private Integer DanmuAmount;
	private Integer PreviewAmount;
	private Integer ReviewAmount;
	private Integer PageCount;
	private String ISBN;
	private String OtherAuthors;
	private String PublishInfo;
	private Boolean CanAddReadList;
	private Boolean CanAddWishList;
	private Boolean CanBuy;

	// Type: all sorts of return IDs types
	private List<Integer> IDs;

	// Type: GetReview
	private String CreateUser;
	private Integer Rating; // (0-5)
	private Long TimeStap;
	private String Title;
	private String Content;

	// GetTitleDescription
	// private String Title; // Overlap with the above one in GetReview
	// private String CreateUser;
	// private String TimeStap;
	private String Description;
	private Integer FollowAmount;
	private Boolean Followed;

	// GetFullDanmuContent:
	// private String Content; //Overlap with the above one in GetReview
	// private String BookName; // repeat again with the one in GetBookSummary
	private Integer PageNum;

	// Type: DownloadBook, GetBookPreview, BuyBook
	private String URL;

	// Type: DownloadBook
	private String PrivateKey;

	private String Message;

	public boolean isSuccess() {
		return Success;
	}

	public void setSuccess(boolean success) {
		Success = success;
	}

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

	public LoginStatus getLoginStatus() {
		return socket.frontEnum.LoginStatus.values()[LoginStatus];
	}

	public void setLoginStatus(LoginStatus loginStatus) {
		LoginStatus = loginStatus.ordinal();
	}

	public Boolean getAdmin() {
		return IsAdmin;
	}

	public void setAdmin(Boolean admin) {
		IsAdmin = admin;
	}

	public String getBookCoverUrl() {
		return BookCoverUrl;
	}

	public void setBookCoverUrl(String bookCoverUrl) {
		BookCoverUrl = bookCoverUrl;
	}

	public String getBookName() {
		return BookName;
	}

	public void setBookName(String bookName) {
		BookName = bookName;
	}

	public String getAuthorName() {
		return AuthorName;
	}

	public void setAuthorName(String authorName) {
		AuthorName = authorName;
	}

	public String getMainAndSubLabel() {
		return MainAndSubLabel;
	}

	public void setMainAndSubLabel(String labelAndSubLabel) {
		MainAndSubLabel = labelAndSubLabel;
	}

	public Double getPrice() {
		return Price;
	}

	public void setPrice(Double price) {
		Price = price;
	}

	public Integer getDiscount() {
		return Discount;
	}

	public void setDisCount(Integer discount) {
		Discount = discount;
	}

	public Double getOverallRating() {
		return OverallRating;
	}

	public void setOverallRating(Double overallRating) {
		OverallRating = overallRating;
	}

	public Integer getBuyAmount() {
		return BuyAmount;
	}

	public void setBuyAmount(Integer buyAmount) {
		BuyAmount = buyAmount;
	}

	public Integer getDanmuAmount() {
		return DanmuAmount;
	}

	public void setDanmuAmount(Integer danmuAmount) {
		DanmuAmount = danmuAmount;
	}

	public Integer getPreviewAmount() {
		return PreviewAmount;
	}

	public void setPreviewAmount(Integer previewAmount) {
		PreviewAmount = previewAmount;
	}

	public Integer getReviewAmount() {
		return ReviewAmount;
	}

	public void setReviewAmount(Integer reviewAmount) {
		ReviewAmount = reviewAmount;
	}

	public Integer getPageCount() {
		return PageCount;
	}

	public void setPageCount(Integer pageCount) {
		PageCount = pageCount;
	}

	public String getISBN() {
		return ISBN;
	}

	public void setISBN(String ISBN) {
		this.ISBN = ISBN;
	}

	public String getOtherAuthors() {
		return OtherAuthors;
	}

	public void setOtherAuthors(String otherAuthors) {
		OtherAuthors = otherAuthors;
	}

	public String getPublishInfo() {
		return PublishInfo;
	}

	public void setPublishInfo(String publishInfo) {
		PublishInfo = publishInfo;
	}

	public Boolean getCanAddReadList() {
		return CanAddReadList;
	}

	public void setCanAddReadList(Boolean canAddReadList) {
		CanAddReadList = canAddReadList;
	}

	public Boolean getCanAddWishList() {
		return CanAddWishList;
	}

	public void setCanAddWishList(Boolean canAddWishList) {
		CanAddWishList = canAddWishList;
	}

	public Boolean getCanBuy() {
		return CanBuy;
	}

	public void setCanBuy(Boolean canBuy) {
		CanBuy = canBuy;
	}

	public List<Integer> getIDs() {
		return IDs;
	}

	public void setIDs(List<Integer> iDs) {
		IDs = iDs;
	}

	public Integer getRating() {
		return Rating;
	}

	public void setRating(Integer rating) {
		Rating = rating;
	}

	public Long getTimeStap() {
		return TimeStap;
	}

	public void setTimeStap(Long timeStap) {
		TimeStap = timeStap;
	}

	public String getTitle() {
		return Title;
	}

	public void setTitle(String title) {
		Title = title;
	}

	public String getContent() {
		return Content;
	}

	public void setContent(String content) {
		Content = content;
	}

	public String getCreateUser() {
		return CreateUser;
	}

	public void setCreateUser(String createUser) {
		CreateUser = createUser;
	}

	public String getDescription() {
		return Description;
	}

	public void setDescription(String description) {
		Description = description;
	}

	public Integer getFollowAmount() {
		return FollowAmount;
	}

	public void setFollowAmount(Integer followAmount) {
		FollowAmount = followAmount;
	}

	public Integer getPageNum() {
		return PageNum;
	}

	public void setPageNum(Integer pageNum) {
		PageNum = pageNum;
	}

	public Boolean getFollowed() {
		return Followed;
	}

	public void setFollowed(Boolean followed) {
		Followed = followed;
	}

	public String getURL() {
		return URL;
	}

	public void setURL(String uRL) {
		URL = uRL;
	}

	public String getPrivateKey() {
		return PrivateKey;
	}

	public void setPrivateKey(String privateKey) {
		PrivateKey = privateKey;
	}

	public List<String> getMainLabels() {
		return MainLabels;
	}

	public void setMainLabels(List<String> mainLabels) {
		MainLabels = mainLabels;
	}

	public List<String> getSubLabels() {
		return SubLabels;
	}

	public void setSubLabels(List<String> subLabels) {
		SubLabels = subLabels;
	}

	public String getMessage() {
		return Message;
	}

	public void setMessage(String message) {
		Message = message;
	}
}
