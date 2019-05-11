package Dao;

import cotroller.QueryReturn;

import java.sql.SQLException;

public interface ReviewDao {
    // get reviews' ID and in the newest order.
    int[] getBookReviews(int bookId, int from, int count) throws SQLException;

    // get the detail of a review.
    QueryReturn GetBookReviews(int reviewId) throws SQLException;
}
