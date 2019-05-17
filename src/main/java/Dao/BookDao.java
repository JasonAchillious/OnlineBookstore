package Dao;
import Socket.InfoToFront;

import java.sql.SQLException;
import java.util.List;

public interface BookDao {
    // return the brief abstract of a book: bookCoverUrl, bookName, bookFullname, AuthorName
    InfoToFront GetBookSummary(int bookId) throws SQLException;

    // return a part of the details of a book:
    InfoToFront GetBookQuasiDetail(int bookId) throws SQLException;

    // get all the bookIds on a user's shelf
    Integer[] GetShelfBooks(int userId) throws SQLException;

    // get all the details of a book
    InfoToFront GetBookDetail(int bookId) throws SQLException;

    // get the related books, recommended books.
    Integer[] GetRelatedBooks(int bookId, int from, int count) throws SQLException;

    // preview the book, return URL
    String GetBookPreview(int bookId) throws SQLException;

    // download the book, return URL.
    String DownloadBook(int bookId) throws SQLException;

    // get the private key of a book for open the PDF.
    String GetBookKey(int bookId) throws SQLException;
}
