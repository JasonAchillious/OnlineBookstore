package Dao;

import Socket.InfoToFront;

import java.sql.SQLException;

public interface WishlistDao {

    // get the books' id from a user's wishlist
    InfoToFront GetMyWishlist(int userId) throws SQLException;

}
