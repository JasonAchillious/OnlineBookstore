
import Dao.impl.BaseDao;
import java.sql.*;

public class testDBConnection {
    public static void main(String[] args){

        BaseDao baseDao = new BaseDao();
        baseDao.getConnection();
        /*boolean book = baseDao.existTable("book");
        if (book){
            System.out.println("exist book table");
        }*/
    }
}
