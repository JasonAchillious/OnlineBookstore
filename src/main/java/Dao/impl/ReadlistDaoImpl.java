package Dao.impl;

import Dao.ReadlistDao;
import Socket.DataToFront;
import Socket.InfoFromFront;
import Socket.frontEnum.ChangeType;

import java.sql.SQLException;

public class ReadlistDaoImpl extends BaseDao implements ReadlistDao {
    @Override
    public DataToFront GetMyReadlist(InfoFromFront infoFromFront) {
        int userId, from, count;
        userId = infoFromFront.getUserId();
        from = infoFromFront.getFrom();
        count = infoFromFront.getCount();
        return null;
    }

    @Override
    public DataToFront GetTitleDescription(InfoFromFront infoFromFront) {
        int readlistId  = infoFromFront.getReadListId();
        return null;
    }

    @Override
    public DataToFront ChangeReadList(InfoFromFront infoFromFront) {
        int readListId, alteredBookId;
        ChangeType changeType = ChangeType.values()[infoFromFront.getChangeType()];
        String alteredText;
        readListId = infoFromFront.getReadListId();
        alteredBookId = infoFromFront.getAlteredBookId();
        return null;
    }

    @Override
    public DataToFront CreateReadList(InfoFromFront infoFromFront) throws SQLException {
        int userId = infoFromFront.getUserId();
        String title = infoFromFront.getTitle();
        String description = infoFromFront.getDescription();

        return null;
    }

    @Override
    public DataToFront FollowReadList(InfoFromFront infoFromFront) throws SQLException {
        int UserId = infoFromFront.getUserId();
        boolean isFollowAction = infoFromFront.getFollowAction();
        int readlistId;
        return null;
    }


}
