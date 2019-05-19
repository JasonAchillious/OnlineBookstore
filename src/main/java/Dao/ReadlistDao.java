package Dao;

import Socket.InfoToFront;

import java.sql.SQLException;

public interface ReadlistDao {
    // get the book's id from a user's readlist
    InfoToFront GetMyReadlist(int userId, int from, int count);

    // get the details of this readlist.
    InfoToFront GetTitleDescription(int readlistId);

    // issues : need to dicuss with the front.
    InfoToFront ChangeReadList(int readListId, Enum ChangeType, int AlteredBookId, String AlteredText);

    // user can create there own readList.
    InfoToFront CreateReadList(int userId, String Title, String Description) throws SQLException;

    // follow or cancel following a readlist.
    InfoToFront FollowReadList(int userId, boolean isFollowAction, int readlistId) throws SQLException;
}
