package Dao;

public interface ReadlistDao {
    // get the book's id from a user's readlist
    int[] getMyReadlist(int userId, int from, int count);

}
