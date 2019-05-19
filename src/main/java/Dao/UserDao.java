package Dao;

import Socket.InfoFromFront;
import Socket.InfoToFront;

import java.sql.SQLException;

public interface UserDao {
    // Check the user's info and return
    InfoToFront Login(String userName, String encodedPassword) throws SQLException;

    // get the readlists' Id (created or followed) by a user
    InfoToFront GetMyReadList(int id, int from , int count) throws SQLException;

    InfoToFront SignUp(String userName, String mailAddr, String encodedPassword) throws SQLException;
}
