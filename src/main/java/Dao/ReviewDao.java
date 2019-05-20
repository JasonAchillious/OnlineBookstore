package Dao;

import Socket.DataToFront;
import Socket.InfoFromFront;

import java.sql.SQLException;

public interface ReviewDao {
    // get reviews' ID and in the newest order.
    DataToFront GetBookReviews(InfoFromFront infoFromFront) throws SQLException;

    // get the detail of a review.
    DataToFront GetReviews(InfoFromFront infoFromFront) throws SQLException;

    // Change the review writed by a user with a specific reviewID.
    DataToFront ChangeReview(InfoFromFront infoFromFront) throws SQLException;

    // Create a review by a user.
    DataToFront CreateReview(InfoFromFront infoFromFront) throws SQLException;

    // Check whether the contraction was done.
    DataToFront CheckBuyComplete(InfoFromFront infoFromFront) throws SQLException;

}
