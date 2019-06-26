package dao;

import java.sql.SQLException;

import socket.InfoFromFront;
import socket.InfoToFront;

public interface LabelDao {

	/**
	 * Get all main labels
	 *
	 * @param infoFromFront
	 * @return
	 * @throws SQLException
	 */
	InfoToFront GetMainLabels(InfoFromFront infoFromFront) throws SQLException;

	/**
	 *  hot_spot algorithm: For all the books in each sub label. <br>
	 *  Calculate the weighting average of bought(0.8),review(0.1), danmu(0.1) amounts.
	 *
	 * @param MainLabels
	 * @return List of sub label order by hot_spot.
	 * @throws SQLException
	 */
	InfoToFront GetSubLabels(InfoFromFront infoFromFront) throws SQLException;
}
