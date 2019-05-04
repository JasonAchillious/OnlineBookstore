package Dao;

public interface WishlistDao {
    // get the books' id from a user's wishlist
    int[] getMyWishlist(int userId);

}
