package Dao;
import Socket.DataToFront;
import Socket.InfoFromFront;

import java.sql.SQLException;

public interface DanmuDao {
    // get the content of a barrage by its id
    DataToFront GetDanmuContent(InfoFromFront infoFromFront) throws SQLException;

    // get the Danmus' id that a user edited.
    DataToFront GetMyDanmus(InfoFromFront infoFromFront) throws SQLException;

    // get the IDs of a book's Danmu
    DataToFront GetDanmuOfBook(InfoFromFront infoFromFront) throws SQLException;

    // get full content of a Danmu
    DataToFront GetFullDanmuContent(InfoFromFront infoFromFront) throws SQLException;

    // delete the danmu or update danmu by danmuId.
    DataToFront ChangeDanmu(InfoFromFront infoFromFront) throws SQLException;

    // Create or send danmu.
    DataToFront CreateDanmu(InfoFromFront infoFromFront) throws SQLException;
}
