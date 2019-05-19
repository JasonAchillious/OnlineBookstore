package Dao;
import Socket.InfoFromFront;
import Socket.InfoToFront;

import java.sql.SQLException;

public interface DanmuDao {
    // get the content of a barrage by its id
    InfoToFront GetDanmuContent(int danmuId) throws SQLException;

    // get the Danmus' id that a user edited.
    InfoToFront GetMyDanmus(int userId, int from, int count) throws SQLException;

    // get the IDs of a book's Danmu
    InfoToFront GetDanmuOfBook(int bookId, int page, int limit) throws SQLException;

    // get full content of a Danmu
    InfoToFront GetFullDanmuContent(int danmuId) throws SQLException;

    // delete the danmu or update danmu by danmuId.
    InfoToFront ChangeDanmu(int danmuId, boolean isDeleteAction, String newContent) throws SQLException;

    // Create or send danmu.
    InfoToFront CreateDanmu(String content, int bookId, int userId, int PageNum) throws SQLException;
}
