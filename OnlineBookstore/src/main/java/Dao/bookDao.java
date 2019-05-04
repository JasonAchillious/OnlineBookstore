package Dao;
import com.google.gson.*;
import entities.Book;

public interface bookDao {
    // return the brief summary of a book: bookCoverUrl, bookName, bookFullname, AuthorName
    Gson GetBookSummary(int bookId);

    // return a part of the detail of a book:
    Gson getBookQuasiDetail(int bookId);

    // get all the bookIds on a user's shelf
    int[] getShelfBooks(int userId);


}
