package Dao;
import cotroller.QueryReturn;

import java.sql.*;
import java.sql.SQLException;

public interface DanmuDao {
    // get the content of a barrage by its id
    String getDanmuContent(int danmuId) throws SQLException;

    // get the Danmus' id that a user edited.
    int[] getMyDanmus(int userId, int from, int count) throws SQLException;

    // get the IDs of a book's Danmu
    int[] getDanmuOfBook(int bookId, int page, int limit) throws SQLException;

    //get full content of a Danmu
    QueryReturn getFullDanmuContent(int danmuId) throws SQLException;
}
