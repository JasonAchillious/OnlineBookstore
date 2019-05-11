package Dao;
import com.google.gson.*;
import cotroller.QueryReturn;
import entities.Book;

import java.sql.SQLException;

public interface BookDao {
    // return the brief abstract of a book: bookCoverUrl, bookName, bookFullname, AuthorName
    QueryReturn getBookSummary(int bookId) throws SQLException;

    // return a part of the details of a book:
    QueryReturn getBookQuasiDetail(int bookId) throws SQLException;

    // get all the bookIds on a user's shelf
    int[] getShelfBooks(int userId) throws SQLException;

    // get all the details of a book
    QueryReturn getBookDetail(int bookId) throws SQLException;

    // get the related books, recommended books.
    int[] getRelatedBooks(int bookId, int from, int count) throws SQLException;

    // preview the book, return URL
    String getBookPreview(int bookId) throws SQLException;

    // download the book, return URL.
    String downloadBook(int bookId) throws SQLException;

    // get the private key of a book for open the PDF.
    String getBookKey(int bookId) throws SQLException;
}
