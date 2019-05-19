package Dao.impl;

import Dao.ReadlistDao;
import Socket.InfoToFront;

import java.sql.SQLException;

public class ReadlistDaoImpl extends BaseDao implements ReadlistDao {
    @Override
    public InfoToFront GetMyReadlist(int userId, int from, int count) {
        return null;
    }

    @Override
    public InfoToFront GetTitleDescription(int readlistId) {
        return null;
    }

    @Override
    public InfoToFront ChangeReadList(int readListId, Enum ChangeType, int AlteredBookId, String AlteredText) {
        return null;
    }

    @Override
    public InfoToFront CreateReadList(int userId, String Title, String Description) throws SQLException {
        return null;
    }

    @Override
    public InfoToFront FollowReadList(int userId, boolean isFollowAction, int readlistId) throws SQLException {
        return null;
    }


}
