package Dao;
import Socket.InfoFromFront;
import Socket.InfoToFront;

import java.sql.SQLException;
import java.util.List;

public interface BookDao {
    // return the brief abstract of a book: bookCoverUrl, bookName, bookFullname, AuthorName
    InfoToFront GetBookSummary(InfoFromFront infoFromFront) throws SQLException;

    // return a part of the details of a book:
    InfoToFront GetBookQuasiDetail(InfoFromFront infoFromFront) throws SQLException;

    // get all the bookIds on a user's shelf
    InfoToFront GetShelfBooks(InfoFromFront infoFromFront) throws SQLException;

    // get all the details of a book
    InfoToFront GetBookDetail(InfoFromFront infoFromFront) throws SQLException;

    // get the related books, recommended books.
    InfoToFront GetRelatedBooks(InfoFromFront infoFromFront) throws SQLException;

    // preview the book, return URL
    InfoToFront GetBookPreview(InfoFromFront infoFromFront) throws SQLException;

    // download the book, return URL.
    InfoToFront DownloadBook(InfoFromFront infoFromFront) throws SQLException;

    // get the private key of a book for open the PDF.
    InfoToFront GetBookKey(InfoFromFront infoFromFront) throws SQLException;

    // issues: need to discuss with the front-end
    InfoToFront BuyBook(InfoFromFront infoFromFront) throws SQLException;

    // Check whether the contraction was done.
    InfoToFront CheckBuyComplete(InfoFromFront infoFromFront) throws SQLException;

    InfoToFront CancelTransaction(InfoFromFront infoFromFront) throws SQLException;
}
