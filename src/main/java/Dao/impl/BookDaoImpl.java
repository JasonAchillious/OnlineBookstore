package Dao.impl;

import Dao.BookDao;
import Socket.InfoToFront;

import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

public class BookDaoImpl extends BaseDao implements BookDao {
    /**
     *  return the brief abstract of a book.
     *  final edit on 5.11.2019 (issues: The database still lacks a table book_author)
     * @param bookId
     * @return bookCoverUrl, bookName, AuthorName
     * @throws SQLException
     */
    @Override
    public InfoToFront GetBookSummary(int bookId) throws SQLException {
        InfoToFront infoToFront = new InfoToFront();
        infoToFront.setType("GetBookSummary");
        getConnection();

        String sql = " select b.name as book_name, book_cover_url, a.name as author_name"
                + "from book b join book_author ba on b.id = ba.id join author a on a.id = ba.id"
                + "where b.id = ?";
        pstmt = conn.prepareStatement(sql);
        pstmt.setInt(1,bookId);
        rs = pstmt.executeQuery();
// if or while?
        if (rs.next()){
            infoToFront.setBookCoverUrl(rs.getString("book_cover_url"));
            infoToFront.setBookName(rs.getString("book_name"));
            infoToFront.setAuthorName(rs.getString("author_name"));
        }

        closeAll();
        return infoToFront;
    }

    /**
     * return a part of the detail of a book
     *
     * @param bookId
     * @return infoToFront
     * @throws SQLException
     */
    @Override
    public InfoToFront GetBookQuasiDetail(int bookId) throws SQLException{
        InfoToFront infoToFront = GetBookSummary(bookId);
        infoToFront.setType("GetBookQuasiDetail");
        try {
            getConnection();

            String sql = "select l.name as main,  sl.name as sub , b.original_price, bs.discount, bs.overall_rating"
                    + "from book b"
                    + "join book_stat bs on b.id = bs.book_id"
                    + "join sub_label sl on b.sublabel_id = sl.id"
                    + "Ajoin label l on sl.main_id = l.id"
                    + "where b.id = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, bookId);
            rs = pstmt.executeQuery();

            while (rs.next())
            {
                infoToFront.setLabelAndSubLabel(rs.getString("main") + "-" + rs.getString("sub"));
                infoToFront.setPrice(rs.getDouble("original_price"));
                infoToFront.setDisCount(rs.getDouble("discount"));
                infoToFront.setOverallRating(rs.getDouble("overall_rating"));
            }

            closeAll();
        }catch (SQLException e){
            e.printStackTrace();
        }
        return infoToFront;
    }

    /**
     * Get all the books that a specific user bought.
     * Final edit in 2019.5.18. Jason Zhao.
     * @param userId
     * @return  Integer[]
     * @throws SQLException
     */
    @Override
    public Integer[] GetShelfBooks(int userId) throws SQLException{
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
        return shelfBooks;
    }

    @Override
    public InfoToFront GetBookDetail(int bookId) throws SQLException {
        return null;
    }

    @Override
    public Integer[] GetRelatedBooks(int bookId, int from, int count) throws SQLException {
        return null;
    }

    @Override
    public String GetBookPreview(int bookId) throws SQLException {
        return null;
    }

    @Override
    public String DownloadBook(int bookId) throws SQLException {
        return null;
    }

    @Override
    public String GetBookKey(int bookId) throws SQLException {
        return null;
    }
}
