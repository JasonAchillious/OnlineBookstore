package cotroller;

import Socket.InfoFromFront;
import Socket.InfoToFront;
import com.google.gson.Gson;
import Dao.impl.*;
import service.DAOFactory;
import service.impl.DAOFactoryImpl;

import java.lang.reflect.Method;
import java.sql.SQLException;
import java.util.List;

public class ReflectionController {

    public static Method methodCtrl(String infoFromFront) throws Exception{
        //String str = null;
        Gson gson = new Gson();
        Method method = null;

        DAOFactory factory = new DAOFactoryImpl();

        try {
            InfoFromFront frontInfo = gson.fromJson(infoFromFront, InfoFromFront.class);
            String type = frontInfo.getType();

            Object baseDao = factory.getBaseDao(type);

            //Method method = null;
            Method[] methods = baseDao.getClass().getMethods();
            for (int i = 0;  i < methods.length; i++){
                if( methods[i].getName().equalsIgnoreCase(type) ){
                    method = methods[i];
                }
            }
            // get the method parameter.

           // Object infoToFront =  method.invoke(baseDao, frontInfo.)
           // str = gson.toJson(infoToFront);
            return method;

        }catch (Exception e){
            e.printStackTrace();
        }

        return method;
    }


}
 /*    switch (type) {
                case "Login":
                    qr = new UserDaoImpl().userLogin(qb.getUserName(), qb.getEncodedPassword());
                    str = gson.toJson(qr);
                    break;
                case "GetBookSummary":
                    qr = new BookDaoImpl().getBookSummary(qb.getBookId());
                    str = gson.toJson(qr);
                    break;
                //case "GetBoo"
                default:
                    str = "请求错误";
            }
        */
/*  if (info instanceof InfoToFront)
            {
                InfoToFront infoToFront = (InfoToFront) info;
                str = gson.toJson(infoToFront, InfoToFront.class);
            }
            else if (info instanceof int[])
            {
                int[] infoToFront = (int[]) info;
                str = gson.toJson(infoToFront);
            }
            else if (info instanceof List)
            {
                List infoToFront = (List) info;
            }
            else if (info instanceof String)
            {

            }
            str = gson.fromJson()
*/
