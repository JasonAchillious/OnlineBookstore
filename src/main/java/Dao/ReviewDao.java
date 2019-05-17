package Dao;

import Socket.InfoToFront;

import java.sql.SQLException;

public interface ReviewDao {
    // get reviews' ID and in the newest order.
    int[] GetBookReviews(int bookId, int from, int count) throws SQLException;

    // get the detail of a review.
    InfoToFront GetBookReviews(int reviewId) throws SQLException;
}
