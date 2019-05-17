package Dao;

import java.sql.SQLException;

public interface WishlistDao {

    // get the books' id from a user's wishlist
    int[] GetMyWishlist(int userId) throws SQLException;

}
