package Dao;

import Socket.InfoFromFront;
import Socket.InfoToFront;

import java.sql.SQLException;

public interface ReviewDao {
    // get reviews' ID and in the newest order.
    InfoToFront GetBookReviews(int bookId, int from, int count) throws SQLException;

    // get the detail of a review.
    InfoToFront GetReviews(int reviewId) throws SQLException;

    // Change the review writed by a user with a specific reviewID.
    InfoToFront ChangeReview(int userId, int reviewId, boolean isDeleteAction, String newTitle, String newContent, int newRating) throws SQLException;

    // Create a review by a user.
    InfoToFront CreateReview(InfoFromFront infoFromFront) throws SQLException;

    // Check whether the contraction was done.
    InfoToFront CheckBuyComplete(int userId, int bookId) throws SQLException;

    InfoToFront CancelTransaction(int userId, int bookId) throws SQLException;
}
