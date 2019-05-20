package Dao.impl;

import Dao.BillboardDao;
import Socket.DataToFront;
import Socket.InfoFromFront;

import java.sql.SQLException;

public class BillboardDaoImpl extends BaseDao implements BillboardDao {
    @Override
    public DataToFront GetBillboardList(InfoFromFront infoFromFront) throws SQLException {
        return null;
    }

    @Override
    public DataToFront GetTitleDescription(InfoFromFront infoFromFront) throws SQLException {
        return null;
    }
}
