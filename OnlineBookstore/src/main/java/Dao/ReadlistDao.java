package Dao;

import cotroller.QueryReturn;

public interface ReadlistDao {
    // get the book's id from a user's readlist
    int[] getMyReadlist(int userId, int from, int count);

    // get the details of this readlist.
    QueryReturn getTitleDescription(int readlistId);
}
