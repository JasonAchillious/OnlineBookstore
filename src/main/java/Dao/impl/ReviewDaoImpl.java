package Dao.impl;

import Dao.ReviewDao;
import Socket.InfoToFront;

import java.sql.SQLException;

public class ReviewDaoImpl extends BaseDao implements ReviewDao {

    @Override
    public int[] GetBookReviews(int bookId, int from, int count) throws SQLException {
        return new int[0];
    }

    @Override
    public InfoToFront GetBookReviews(int reviewId) throws SQLException {
        return null;
    }
}
