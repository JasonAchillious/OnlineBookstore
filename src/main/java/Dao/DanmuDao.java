package Dao;
import Socket.InfoToFront;

import java.sql.SQLException;

public interface DanmuDao {
    // get the content of a barrage by its id
    String GetDanmuContent(int danmuId) throws SQLException;

    // get the Danmus' id that a user edited.
    int[] GetMyDanmus(int userId, int from, int count) throws SQLException;

    // get the IDs of a book's Danmu
    int[] GetDanmuOfBook(int bookId, int page, int limit) throws SQLException;

    //get full content of a Danmu
    InfoToFront GetFullDanmuContent(int danmuId) throws SQLException;
}
