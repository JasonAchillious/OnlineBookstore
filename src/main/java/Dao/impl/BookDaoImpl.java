package Dao.impl;

import Dao.BookDao;
import Socket.InfoToFront;

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

        while (rs.next()){
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
     * @return
     * @throws SQLException
     */
    @Override
    public InfoToFront GetBookQuasiDetail(int bookId) throws SQLException{
        return null;
    }

    @Override
    public int[] GetShelfBooks(int userId) throws SQLException{
        return new int[0];
    }

    @Override
    public InfoToFront GetBookDetail(int bookId) throws SQLException {
        return null;
    }

    @Override
    public int[] GetRelatedBooks(int bookId, int from, int count) throws SQLException {
        return new int[0];
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
