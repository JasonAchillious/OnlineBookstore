package Dao.impl;

import Dao.BookDao;
import Socket.InfoFromFront;
import Socket.DataToFront;

import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

public class BookDaoImpl extends BaseDao implements BookDao {
    /**
     *  return the brief abstract of a book.
     *  final edit on 5.11.2019 (issues: The database still lacks a table book_author)
     * @param infoFromFront
     * @return bookCoverUrl, bookName, AuthorName
     * @throws SQLException
     */
    @Override
    public DataToFront GetBookSummary(InfoFromFront infoFromFront) throws SQLException {
        int bookId = infoFromFront.getBookId();

        DataToFront dataToFront = new DataToFront();
        dataToFront.setType("GetBookSummary");
        getConnection();

        String sql = " select b.name as book_name, book_cover_url, a.name as author_name" +
                "from book b " +
                "join book_author ba on b.id = ba.id " +
                "join author a on a.id = ba.id" +
                "where b.id = ? and a.is_main_author is true ";
        pstmt = conn.prepareStatement(sql);
        pstmt.setInt(1,bookId);
        rs = pstmt.executeQuery();
// if or while?
        if (rs.next()){
            dataToFront.setBookCoverUrl(rs.getString("book_cover_url"));
            dataToFront.setBookName(rs.getString("book_name"));
            dataToFront.setAuthorName(rs.getString("author_name"));
        }

        closeAll();
        return dataToFront;
    }

    /**
     * return a part of the detail of a book
     *
     * @param infoFromFront
     * @return infoToFront
     * @throws SQLException
     */
    @Override
    public DataToFront GetBookQuasiDetail(InfoFromFront infoFromFront) throws SQLException{
        DataToFront dataToFront = GetBookSummary(infoFromFront);
        int bookId = infoFromFront.getBookId();
        dataToFront.setType("GetBookQuasiDetail");
        try {
            getConnection();

            String sql = "select l.name as main,  sl.name as sub , b.original_price, bs.discount, bs.overall_rating"
                    + "from book b"
                    + "join book_stat bs on b.id = bs.book_id"
                    + "join sub_label sl on b.sublabel_id = sl.id"
                    + "Ajoin label l on sl.main_id = l.id"
                    + "where b.id = ? ";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, bookId);
            rs = pstmt.executeQuery();

            while (rs.next())
            {
                dataToFront.setLabelAndSubLabel(rs.getString("main") + "-" + rs.getString("sub"));
                dataToFront.setPrice(rs.getDouble("original_price"));
                dataToFront.setDisCount(rs.getInt("discount"));
                dataToFront.setOverallRating(rs.getDouble("overall_rating"));
            }

            closeAll();
        }catch (SQLException e){
            e.printStackTrace();
        }
        return dataToFront;
    }

    /**
     * Get all the books that a specific user bought.
     * Final edit in 2019.5.18. Jason Zhao.
     *
     * @param infoFromFront
     * @return  Integer[]
     * @throws SQLException
     */
    @Override
    public DataToFront GetShelfBooks(InfoFromFront infoFromFront) throws SQLException{
        int userId = infoFromFront.getUserId();
        List<Integer> shelf = new LinkedList<>();
        Integer[] shelfBooks = null;
        try {
            getConnection();

            String sql = "select book_id from transaction where user_id = ? and paied is true";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, userId);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                shelf.add(rs.getInt("book_id"));
            }
        }catch (SQLException e){
            e.printStackTrace();
        }

        shelfBooks = shelf.toArray(new Integer[shelf.size()]);
        return null;
    }

    /**
     * PublishInfo (String + String)
     * OtherAuthor (StringBuider)
     * 3 Booleans (Using Query in Database to check if there exists the same bookId and UserId
     *              in the tables of wishlist, readlist, trancsaction.
     *              if the result set is null then the user can add or buy this book.
     *
     * @param infoFromFront
     * @return DataToFront
     *          OtherAuthors (name1(isTranlator) name2....)
     *          Decription of this book
     *          PublishInfo "Press / publish_time / edition"
     *          ISBN
     *          BuyAmount, DanmuAmount, PreviewAmount, ReviewAmount, Pages of this book.
     *          3 Booleans (
     *          Can this user add this book to his readlist or wishlist.
     *          Can this user can buy this book.
     * @throws SQLException
     */
    @Override
    public DataToFront GetBookDetail(InfoFromFront infoFromFront) throws SQLException {
        DataToFront dataToFront = GetBookQuasiDetail(infoFromFront);

        int bookId = infoFromFront.getBookId();
        int userId = infoFromFront.getUserId();

        String otherAuthorSql = null;
        otherAuthorSql = "select a.name , is_translator" +
                "from author a" +
                "join book_author ba on a.id = ba.author_id" +
                "join book b on ba.book_id = b.id" +
                "where is_main_author is false and b.id = ?";

        pstmt = conn.prepareStatement(otherAuthorSql);
        pstmt.setInt(1, bookId);
        rs = pstmt.executeQuery();

        StringBuilder stringBuilder = new StringBuilder();
        while (rs.next())
        {
            Boolean isTranslator = rs.getBoolean("is_translator");
            String authorName = rs.getString("name");

            if(isTranslator)
                stringBuilder.append(authorName + "(Translator) ");
            else
                stringBuilder.append(authorName + " ");

        }

        dataToFront.setOtherAuthors(stringBuilder.toString().trim());

        String bookDetailSQL = "select b.description, b.publish_time, b.version, p.name, b.ISBN, b.pages, bs.buys, bs.danmus, bs.previews, bs.reviews" +
                "from book b join book_stat bs on b.id = bs.book_id join press p on b.press_id = p.id" +
                "where b.id = ?";

        pstmt = conn.prepareStatement(bookDetailSQL);
        pstmt.setInt(1, bookId);
        rs = pstmt.executeQuery();

        while (rs.next())
        {
            dataToFront.setDescription(rs.getString("description"));
            dataToFront.setISBN(rs.getString("ISBN"));
            dataToFront.setBuyAmount(rs.getInt("buys"));
            dataToFront.setDanmuAmount(rs.getInt("danmus"));
            dataToFront.setPreviewAmount(rs.getInt("previews"));
            dataToFront.setReviewAmount(rs.getInt("reviews"));
            dataToFront.setPageCount(rs.getInt("pages"));

            String pressName = rs.getString("name");
            String publish_Time = rs.getDate("publish_time").toString();
            String version = rs.getString("version");

            dataToFront.setPublishInfo(pressName + " / " + publish_Time + " / " + version);

        }

        if (userId != -1) {
            String canAddReadlistSQL = "select book_id\n" +
                    "from readlist r\n" +
                    "    join readlist_books rb on r.id = rb.readlist_id\n" +
                    "where r.create_user = ? and rb.book_id = ?";

            pstmt = conn.prepareStatement(canAddReadlistSQL);
            pstmt.setInt(1, userId);
            pstmt.setInt(2, bookId);
            rs = pstmt.executeQuery();

            if (rs.next() == false)
                dataToFront.setCanAddReadList(true);
            else
                dataToFront.setCanAddReadList(false);

            //
            String canAddWishlistSQL = "select w.user_id, w.book_id from wish_list w\n" +
                    "where user_id = ? and book_id = ?;";
            pstmt = conn.prepareStatement(canAddWishlistSQL);
            pstmt.setInt(1, userId);
            pstmt.setInt(2, bookId);
            rs = pstmt.executeQuery();

            if (rs.next() == false)
                dataToFront.setCanAddWishList(true);
            else
                dataToFront.setCanAddWishList(false);

            //
            String canBuySQL = "select user_id, book_id, paied from transaction\n" +
                    "where user_id = ? and book_id = ? and paied is true;";

            pstmt = conn.prepareStatement(canBuySQL);
            pstmt.setInt(1, userId);
            pstmt.setInt(2, bookId);
            rs = pstmt.executeQuery();

            if (rs.next() == false)
                dataToFront.setCanBuy(true);
            else
                dataToFront.setCanBuy(false);
        }
        return dataToFront;
    }

    @Override
    public DataToFront GetRelatedBooks(InfoFromFront infoFromFront) throws SQLException {
        int bookId = infoFromFront.getBookId();
        int from = infoFromFront.getFrom();
        int count = infoFromFront.getCount();
        return null;
    }

    @Override
    public DataToFront GetBookPreview(InfoFromFront infoFromFront) throws SQLException {
        int bookId = infoFromFront.getBookId();
        return null;
    }

    @Override
    public DataToFront DownloadBook(InfoFromFront infoFromFront) throws SQLException {
        int bookId = infoFromFront.getBookId();
        return null;
    }

    @Override
    public DataToFront GetBookKey(InfoFromFront infoFromFront) throws SQLException {
        int bookId = infoFromFront.getBookId();
        return null;
    }

    @Override
    public DataToFront BuyBook(InfoFromFront infoFromFront) throws SQLException {
        int userId = infoFromFront.getUserId();
        int bookId = infoFromFront.getBookId();
        return null;
    }

    @Override
    public DataToFront CheckBuyComplete(InfoFromFront infoFromFront) throws SQLException {
        int userId = infoFromFront.getUserId();
        int bookId = infoFromFront.getBookId();
        return null;
    }

    @Override
    public DataToFront CancelTransaction(InfoFromFront infoFromFront) throws SQLException {
        int userId = infoFromFront.getUserId();
        int bookId = infoFromFront.getBookId();
        return null;
    }


}
