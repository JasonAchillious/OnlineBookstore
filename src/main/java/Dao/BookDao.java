package Dao;
import Socket.DataToFront;
import Socket.InfoFromFront;

import java.sql.SQLException;

public interface BookDao {
    // return the brief abstract of a book: bookCoverUrl, bookName, bookFullname, AuthorName
    DataToFront GetBookSummary(InfoFromFront infoFromFront) throws SQLException;

    // return a part of the details of a book:
    DataToFront GetBookQuasiDetail(InfoFromFront infoFromFront) throws SQLException;

    // get all the bookIds on a user's shelf
    DataToFront GetShelfBooks(InfoFromFront infoFromFront) throws SQLException;

    // get all the details of a book
    DataToFront GetBookDetail(InfoFromFront infoFromFront) throws SQLException;

    // get the related books, recommended books.
    DataToFront GetRelatedBooks(InfoFromFront infoFromFront) throws SQLException;

    // preview the book, return URL
    DataToFront GetBookPreview(InfoFromFront infoFromFront) throws SQLException;

    // download the book, return URL.
    DataToFront DownloadBook(InfoFromFront infoFromFront) throws SQLException;

    // get the private key of a book for open the PDF.
    DataToFront GetBookKey(InfoFromFront infoFromFront) throws SQLException;

    // issues: need to discuss with the front-end
    DataToFront BuyBook(InfoFromFront infoFromFront) throws SQLException;

    // Check whether the contraction was done.
    DataToFront CheckBuyComplete(InfoFromFront infoFromFront) throws SQLException;

    //
    DataToFront CancelTransaction(InfoFromFront infoFromFront) throws SQLException;
}
