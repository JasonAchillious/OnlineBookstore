package dao;

import java.sql.SQLException;

import socket.InfoFromFront;
import socket.InfoToFront;

public interface ReviewDao {
	/**
	 * Get the ReviewId from <code>n</code> to <code>n + count</code> order by recently edited time.
	 * Finally edited in 2019.5.18. Jason Zhao.
	 * @param bookId
	 * @param from
	 * @param count
	 * @return InfoToFront
	 * @throws SQLException
	 */
	InfoToFront GetBookReviews(InfoFromFront infoFromFront) throws SQLException;

	/**
	 * Get the details of a specific review.
	 * Finally edited in 2019.5.18. Jason Zhao.
	 * param reviewId
	 * @return InfoToFront
	 * @throws SQLException
	 */
	InfoToFront GetReview(InfoFromFront infoFromFront) throws SQLException;

	// Change the review writed by a user with a specific reviewID.
	InfoToFront ChangeReview(InfoFromFront infoFromFront) throws SQLException;

	/**
	 * Create review
	 * @author Miao Yanchao
	 * @param infoFromFront
	 * @return infoToFront
	 * @throws SQLException
	 */
	InfoToFront CreateReview(InfoFromFront infoFromFront) throws SQLException;

	/**
	 * Get the reviews' ids of user 
	 * @author Kevin Sun
	 * @param infoFromFront
	 * @return
	 * @throws SQLException
	 */
	InfoToFront GetMyReviews(InfoFromFront infoFromFront) throws SQLException;

}
