package Dao;

import Socket.DataToFront;
import Socket.InfoFromFront;

import java.sql.SQLException;

public interface UserDao {

    // Check the user's info and return
    DataToFront Login(InfoFromFront infoFromFront) throws SQLException;

    // get the readlists' Id (created or followed) by a user.
    DataToFront GetMyReadList(InfoFromFront infoFromFront) throws SQLException;

    // user register.
    DataToFront SignUp(InfoFromFront infoFromFront) throws SQLException;
}
