package service;

import Dao.*;
import Dao.impl.BaseDao;
import entities.Label;

/**
 * Factory model
 * Produce the object from Dao package.
 */

public interface DAOFactory {

    BillboardDao getBillboardDao();

    BookDao getBookDao();

    DanmuDao getDanmuDao();

    LabelDao getLabelDao();

    ReviewDao getReviewDao();

    UserDao getUserDao();

    WishlistDao getWishlistDao();

    BaseDao getBaseDao(String methodName);
}
