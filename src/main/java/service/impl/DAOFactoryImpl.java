package service.impl;

import Dao.*;
import Dao.impl.*;
import entities.Book;
import entities.Label;
import javafx.scene.effect.Reflection;
import service.DAOFactory;

import java.lang.reflect.Method;
import java.util.List;

public class DAOFactoryImpl implements DAOFactory{
   /* enum isWhichDao{
        isBillboradDao,
        isBookDao,
        isDanmuDao,
        isLabelDao,
        isReadlistDao,
        isReviewDao,
        isUserDao,
        isWishlistDao
    }
   */
    boolean isBillboard(String method)
    {
        Method[] methods = BillboardDaoImpl.class.getDeclaredMethods();
        for (int i = 0; i < methods.length; i++)
        {
            if (method.equals(methods[i].getName()))
            {
                return true;
            }
        }
        return false;
    }

    boolean isBook(String method)
    {
        Method[] methods = BookDaoImpl.class.getDeclaredMethods();
        for (int i = 0; i < methods.length; i++)
        {
            if (method.equals(methods[i].getName()))
            {
                return true;
            }
        }
        return false;
    }

    boolean isDanmu(String method)
    {
        Method[] methods = DanmuDaoImpl.class.getDeclaredMethods();
        for (int i = 0; i < methods.length; i++)
        {
            if (method.equals(methods[i].getName()))
            {
                return true;
            }
        }
        return false;
    }

    boolean isLabel(String method)
    {
        Method[] methods = LabelDaoImpl.class.getDeclaredMethods();
        for (int i = 0; i < methods.length; i++)
        {
            if (method.equals(methods[i].getName()))
            {
                return true;
            }
        }
        return false;
    }

    boolean isReadlist(String method)
    {
        Method[] methods = ReadlistDaoImpl.class.getDeclaredMethods();
        for (int i = 0; i < methods.length; i++)
        {
            if (method.equals(methods[i].getName()))
            {
                return true;
            }
        }
        return false;
    }

    boolean isReview(String method){
        Method[] methods = ReviewDaoImpl.class.getDeclaredMethods();
        for (int i = 0; i < methods.length; i++)
        {
            if (method.equals(methods[i].getName()))
            {
                return true;
            }
        }
        return false;
    }

    boolean isUser(String method){
        Method[] methods = UserDaoImpl.class.getDeclaredMethods();
        for (int i = 0; i < methods.length; i++)
        {
            if (method.equals(methods[i].getName()))
            {
                return true;
            }
        }
        return false;
    }

    boolean isWishlist(String method){
         Method[] methods = WishlistDaoImpl.class.getDeclaredMethods();
        for (int i = 0; i < methods.length; i++)
        {
            if (method.equals(methods[i].getName()))
            {
                return true;
            }
        }
        return false;
    }

    @Override
    public BillboardDao getBillboardDao() {
       return new BillboardDaoImpl();
    }

    @Override
    public BookDao getBookDao() {
        return new BookDaoImpl();
    }

    @Override
    public DanmuDao getDanmuDao() {
        return new DanmuDaoImpl();
    }

    @Override
    public LabelDao getLabelDao() {
        return new LabelDaoImpl();
    }

    @Override
    public ReviewDao getReviewDao() {
        return new ReviewDaoImpl();
    }

    @Override
    public UserDao getUserDao() {
        return new UserDaoImpl();
    }

    @Override
    public WishlistDao getWishlistDao() {
        return new WishlistDaoImpl();
    }

    @Override
    public BaseDao getBaseDao(String methodName) {
        BaseDao baseDao = null;
        if(isBillboard(methodName))
        {
            return new BillboardDaoImpl();
        }
        else if(isBook(methodName))
        {
            return new BookDaoImpl();
        }
        else if(isDanmu(methodName))
        {
            return new DanmuDaoImpl();
        }
        else if(isLabel(methodName))
        {
            return new LabelDaoImpl();
        }
        else if(isReadlist(methodName))
        {
            return new ReadlistDaoImpl();
        }
        else if(isReview(methodName))
        {
            return new ReviewDaoImpl();
        }
        else if(isUser(methodName))
        {
            return new UserDaoImpl();
        }
        else if(isWishlist(methodName))
        {
            return new WishlistDaoImpl();
        }
        return baseDao;
    }


}
