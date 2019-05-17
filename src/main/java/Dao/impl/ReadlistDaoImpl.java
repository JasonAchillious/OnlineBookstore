package Dao.impl;

import Dao.ReadlistDao;
import Socket.InfoToFront;

public class ReadlistDaoImpl extends BaseDao implements ReadlistDao {
    @Override
    public int[] GetMyReadlist(int userId, int from, int count) {
        return new int[0];
    }

    @Override
    public InfoToFront GetTitleDescription(int readlistId) {
        return null;
    }
}
