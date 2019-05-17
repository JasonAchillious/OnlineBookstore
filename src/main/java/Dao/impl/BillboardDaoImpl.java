package Dao.impl;

import Dao.BillboardDao;
import Socket.InfoToFront;

import java.sql.SQLException;

public class BillboardDaoImpl extends BaseDao implements BillboardDao {
    @Override
    public int[] GetBillboardList(int Id, int from, int count) throws SQLException {
        return new int[0];
    }

    @Override
    public InfoToFront GetTitleDescription(int billboardId) throws SQLException {
        return null;
    }
}
