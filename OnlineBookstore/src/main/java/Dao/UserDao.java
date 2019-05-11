package Dao;

import com.google.gson.Gson;
import cotroller.QueryReturn;

import java.sql.SQLException;

public interface UserDao {
    // Check the user's info and return
    QueryReturn userLogin(String userName, String encodedPassword) throws SQLException;

    // get the readlists' Id (created or followed) by a user
    int[] getMyReadList(int id, int from ,int count);
}
