package Dao.impl;

import Dao.WishlistDao;
import Socket.DataToFront;
import Socket.InfoFromFront;

import java.sql.SQLException;

public class WishlistDaoImpl extends BaseDao implements WishlistDao {
    @Override
    public DataToFront GetMyWishlist(InfoFromFront infoFromFront) throws SQLException {
        int userId = infoFromFront.getUserId();
        return null;
    }
}
