package cotroller;

public class QueryObject {
    private int UserId = -1;
    private String Type;

    private String UserName;
    private String EncodedPassword;

    private String MainLabel;

    private Integer BookId;
    private Boolean isBillboard;
    private Integer BookListID;
    private Integer ReviewId;
    private Integer DanmuId;
    private Integer From;
    private Integer Count;

    private Integer SearchType;
    private String DirectQuery;
    private String QueryText;
    private String OrderDescend;
    private Integer Order;
    private Integer TimeRangeType;
    private int[] TimeRange;
    private int[] PageRange;
    private String[] LabelFilters;
    private Boolean includeFreeBooks;

    private Integer Page;

    public QueryObject() {
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

    public Boolean getBillboard() {
        return isBillboard;
    }

    public void setBillboard(Boolean billboard) {
        isBillboard = billboard;
    }

    public Integer getBookListID() {
        return BookListID;
    }

    public void setBookListID(Integer bookListID) {
        BookListID = bookListID;
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

    public String getDirectQuery() {
        return DirectQuery;
    }

    public void setDirectQuery(String directQuery) {
        DirectQuery = directQuery;
    }

    public String getQueryText() {
        return QueryText;
    }

    public void setQueryText(String queryText) {
        QueryText = queryText;
    }

    public String getOrderDescend() {
        return OrderDescend;
    }

    public void setOrderDescend(String orderDescend) {
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
        return includeFreeBooks;
    }

    public void setIncludeFreeBooks(Boolean includeFreeBooks) {
        this.includeFreeBooks = includeFreeBooks;
    }

    public Integer getPage() {
        return Page;
    }

    public void setPage(Integer page) {
        Page = page;
    }
}
