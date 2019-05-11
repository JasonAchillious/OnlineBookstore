package cotroller;


public class ChangeObject {
    private int UserId = -1;
    private String Type;

    private String UserName;
    private String Email;
    private String EncodedPassword;

    private Integer DanmuId;
    private Boolean IsDeleteAction;
    private String NewContent;

    private Integer ReadListId;
    private Integer ChangeType;

    private Integer AlteredBookId;
    private String AlteredText;

    private Integer BookId;
    private Boolean IsAddAction;

    private String Description;

    private String Content;
    private Integer PageNum;

    private String Title;

    private Integer Rating;

    public ChangeObject(){

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

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getEncodedPassword() {
        return EncodedPassword;
    }

    public void setEncodedPassword(String encodedPassword) {
        EncodedPassword = encodedPassword;
    }

    public Integer getDanmuId() {
        return DanmuId;
    }

    public void setDanmuId(Integer danmuId) {
        DanmuId = danmuId;
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

    public Integer getBookId() {
        return BookId;
    }

    public void setBookId(Integer bookId) {
        BookId = bookId;
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
}
