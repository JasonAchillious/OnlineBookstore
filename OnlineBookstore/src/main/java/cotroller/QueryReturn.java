package cotroller;

import cotroller.returnEnum.LoginStatus;

public class QueryReturn {
    private int UserId = -1;
    private String Type;

    // Type: Login
    private LoginStatus LoginStatus;
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
    private Long PublishTimeStap;
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
    private String EditTimeStap;
    private String Description;
    private Integer FollowAmount;

    //GetFullDanmuContent:
    //private String Content;  //Overlap with the above one in GetReview
    //private String BookName; // repeat again with the one in GetBookSummary
    private Integer PageNum;


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
        return LoginStatus;
    }

    public void setLoginStatus(LoginStatus loginStatus) {
        this.LoginStatus = loginStatus;
    }

    public Boolean getAdmin() {
        return IsAdmin;
    }

    public void setAdmin(Boolean admin) {
        IsAdmin = admin;
    }
}
