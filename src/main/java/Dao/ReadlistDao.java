package Dao;

import Socket.DataToFront;
import Socket.InfoFromFront;

import java.sql.SQLException;

public interface ReadlistDao {
    // get the book's id from a user's readlist
    DataToFront GetMyReadlist(InfoFromFront infoFromFront) throws SQLException;

    // get the details of this readlist.
    DataToFront GetTitleDescription(InfoFromFront infoFromFront) throws SQLException;

    // issues : need to dicuss with the front.
    DataToFront ChangeReadList(InfoFromFront infoFromFront) throws SQLException;

    // user can create there own readList.
    DataToFront CreateReadList(InfoFromFront infoFromFront) throws SQLException;

    // follow or cancel following a readlist.
    DataToFront FollowReadList(InfoFromFront infoFromFront) throws SQLException;
}
