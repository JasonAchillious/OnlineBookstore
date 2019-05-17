package Dao.impl;

import Dao.WishlistDao;

import java.sql.SQLException;

public class WishlistDaoImpl extends BaseDao implements WishlistDao {
    @Override
    public int[] GetMyWishlist(int userId) throws SQLException {
        return new int[0];
    }
}
