package Dao;

import Socket.InfoToFront;

public interface ReadlistDao {
    // get the book's id from a user's readlist
    int[] GetMyReadlist(int userId, int from, int count);

    // get the details of this readlist.
    InfoToFront GetTitleDescription(int readlistId);
}
