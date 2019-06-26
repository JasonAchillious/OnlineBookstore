package dao;

import java.sql.SQLException;

import socket.InfoFromFront;
import socket.InfoToFront;

public interface WishlistDao {

	/**
	 * get the books' id from a user's wish list
	 * @author Niu Lie
	 * @param infoFromFront
	 * @return
	 * @throws SQLException
	 */
	InfoToFront GetMyWishlist(InfoFromFront infoFromFront) throws SQLException;

	/**
	 * Edit user's wish list
	 * @author NiuLie
	 * @param infoFromFront
	 * @return
	 * @throws SQLException
	 */
	InfoToFront ChangeWishlist(InfoFromFront infoFromFront) throws SQLException;

}
