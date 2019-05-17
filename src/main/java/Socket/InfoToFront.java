package Socket;

import Socket.frontEnum.LoginStatus;

public class InfoToFront {
    private int UserId = -1;
    private String Type;

    // Type: Login
    private Socket.frontEnum.LoginStatus LoginStatus;
    private Boolean IsAdmin;

    // Type:GetBookSummary
    private String BookCoverUrl;
    private String BookName;
    private String AuthorName;

    // Type: GetBookQuasiDetail: Followings + above "GetBookSummary"
    private String LabelAndSubLabel;
    private Double Price; // non-negative
    private Double DisCount; // (0 < DisCount < 100)
    private Double OverallRating;


    // Type: GetBookDetail: Doesn't need to return the info of "GetBookSummary".
    // But need to return the data of "GetBookQuasiDetail".
    private Integer BuyAmount;
    private Integer DanmuAmount;
    private Integer PreviewAmount;
    private Integer ReviewAmount;
    private Integer PageCount;
    private String ISBN;
    // Followings need to concern and discuss
    private String OtherAuthors;
    private String PublishInfo;
    private Boolean CanAddReadList;
    private Boolean CanAddWishList;
    private Boolean CanBuy;

    // Type: GetBookReviews (get reviews's ID of a book)
    //private Integer[] ReviewIDs;

    // Type: GetReview
    private String UserName;
    private Integer Rating; //(0-5)
    private Long TimeStap;
    private String Title;
    private String Content;

    // Type: GetShelfBooks
    //private Integer[] BookIDs;

    /**
     * GetFromQuery
     * @return
     */

    // GetTitleDescription
    //private String Title; // Overlap with the above one in GetReview
    private String CreateUser;
    //private String TimeStap;
    private String Description;
    private Integer FollowAmount;

    //GetFullDanmuContent:
    //private String Content;  //Overlap with the above one in GetReview
    //private String BookName; // repeat again with the one in GetBookSummary
    private Integer PageNum;

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

    public Socket.frontEnum.LoginStatus getLoginStatus() {
        return LoginStatus;
    }

    public void setLoginStatus(Socket.frontEnum.LoginStatus loginStatus) {
        LoginStatus = loginStatus;
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

    public String getLabelAndSubLabel() {
        return LabelAndSubLabel;
    }

    public void setLabelAndSubLabel(String labelAndSubLabel) {
        LabelAndSubLabel = labelAndSubLabel;
    }

    public Double getPrice() {
        return Price;
    }

    public void setPrice(Double price) {
        Price = price;
    }

    public Double getDisCount() {
        return DisCount;
    }

    public void setDisCount(Double disCount) {
        DisCount = disCount;
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

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
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

    public void setTimeStap(Long TimeStap) {
        TimeStap = TimeStap;
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

    /*public String getEditTimeStap() {
        return EditTimeStap;
    }*/

    /*public void setEditTimeStap(String editTimeStap) {
        EditTimeStap = editTimeStap;
    }*/

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
}
