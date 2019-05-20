package controller;

import Socket.DataToFront;
import Socket.InfoFromFront;
import com.google.gson.Gson;
import service.DAOFactory;
import service.impl.DAOFactoryImpl;

import java.lang.reflect.Method;

/**
 *
 */

public class ReflectionController {
    /**
     *
     * @param frontInfo
     * @return
     * @throws Exception
     */

    public DataToFront methodCtrl(String frontInfo) throws Exception
    {
        Gson gson = new Gson();
        InfoFromFront infoFromFront = gson.fromJson(frontInfo, InfoFromFront.class);

        DAOFactory factory = new DAOFactoryImpl();

        String type = infoFromFront.getType();

        // According to the method, find the corresponding object. baseDao <- son of BaseDao.
        Object baseDao = factory.getBaseDao(type);

        Method method = null;
        Method[] methods = baseDao.getClass().getMethods();
        for (int i = 0; i < methods.length; i++) {
            if (methods[i].getName().equalsIgnoreCase(type))
                    method = methods[i];
        }

        Object infoToFront = method.invoke(baseDao, infoFromFront);

        if (infoToFront instanceof DataToFront){
            return (DataToFront) infoToFront;
        }

        return null;

    }
/*
    /**
     *
     * @param frontInfo
     * @return
     */
/*
    private Object[] dataCtrl(String frontInfo)
    {
        Gson gson = new Gson();
        InfoFromFront iff = gson.fromJson(frontInfo, InfoFromFront.class);

        String type = iff.getType();
        Object[] parameters = null;

        if (type.equalsIgnoreCase("Login"))
            parameters = new Object[]{iff.getUserName(), iff.getEncodedPassword()};
        else if (type.equalsIgnoreCase("GetMainLabels"))
            parameters = null;
        else if (type.equalsIgnoreCase("GetSubLabels"))
            parameters = new Object[]{iff.getMainLabel()};
        else if (type.equalsIgnoreCase("GetBookSummary")
                || type.equalsIgnoreCase("GetBookQuasiDetail")
                || type.equalsIgnoreCase("GetBookPreview")
                || type.equalsIgnoreCase("DownloadBook")
                || type.equalsIgnoreCase("GetBookKey")
        )
            parameters = new Object[]{iff.getBookId()};
        else if (type.equalsIgnoreCase("GetBookDetail"))
            parameters = new Object[]{iff.getBookId(), iff.getUserId()};
        else if ( type.equalsIgnoreCase("GetMyWishlist")
                || type.equalsIgnoreCase("GetMyDanmu")
                || type.equalsIgnoreCase("GetMyReviews")
                || type.equalsIgnoreCase("GetMyFollowedReadlist")
                || type.equalsIgnoreCase("GetMyCreatedReadlist")
        )
            parameters = new Object[]{iff.getUserId()};





        return parameters;
    }

*/
}


