package Dao;

import Socket.DataToFront;
import Socket.InfoFromFront;

import java.sql.SQLException;

public interface WishlistDao {

    // get the books' id from a user's wishlist
    DataToFront GetMyWishlist(InfoFromFront infoFromFront) throws SQLException;

}
