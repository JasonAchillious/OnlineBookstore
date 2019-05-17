package Dao;

import Socket.InfoToFront;

import java.sql.SQLException;

public interface ReviewDao {
    // get reviews' ID and in the newest order.
    Integer[] GetBookReviews(int bookId, int from, int count) throws SQLException;

    // get the detail of a review.
    InfoToFront GetReviews(int reviewId) throws SQLException;
}
