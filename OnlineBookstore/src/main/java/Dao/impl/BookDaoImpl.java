package Dao.impl;

import Dao.BookDao;
import cotroller.QueryReturn;

import java.sql.SQLException;

public class BookDaoImpl extends BaseDao implements BookDao {
    /**
     *  return the brief abstract of a book.
     *  final edit on 5.11.2019 (issues: The database still lacks a table book_author)
     * @param bookId
     * @return bookCoverUrl, bookName, AuthorName
     * @throws SQLException
     */
    @Override
    public QueryReturn getBookSummary(int bookId) throws SQLException {
        QueryReturn qr = new QueryReturn();
        qr.setType("GetBookSummary");
        getConnection();

        String sql = " select b.name as book_name, book_cover_url, a.name as author_name"
                + "from book b join book_author ba on b.id = ba.id join author a on a.id = ba.id"
                + "where b.id = ?";
        pstmt = conn.prepareStatement(sql);
        pstmt.setInt(1,bookId);
        rs = pstmt.executeQuery();

        while (rs.next()){
            qr.setBookCoverUrl(rs.getString("book_cover_url"));
            qr.setBookName(rs.getString("book_name"));
            qr.setAuthorName(rs.getString("author_name"));
        }

        closeAll();
        return qr;
    }

    /**
     * return a part of the detail of a book
     *
     * @param bookId
     * @return
     * @throws SQLException
     */
    @Override
    public QueryReturn getBookQuasiDetail(int bookId) throws SQLException{
        return null;
    }

    @Override
    public int[] getShelfBooks(int userId) throws SQLException{
        return new int[0];
    }
}
