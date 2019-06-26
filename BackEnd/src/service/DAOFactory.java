package service;

import dao.AdminDao;
import dao.BillboardDao;
import dao.BookDao;
import dao.DanmuDao;
import dao.LabelDao;
import dao.ReviewDao;
import dao.UserDao;
import dao.WishlistDao;
import dao.impl.BaseDao;

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

	AdminDao getAdminDao();

	BaseDao getBaseDao(String methodName);
}
