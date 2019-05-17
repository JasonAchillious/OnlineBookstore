import Dao.impl.BaseDao;
import Dao.impl.BillboardDaoImpl;
import Dao.impl.BookDaoImpl;
import Dao.impl.UserDaoImpl;

import java.lang.reflect.Method;

public class TestBaseDaoReflection {
    public static void main(String[] args)
    {
        BaseDao UserDao = new UserDaoImpl();
        System.out.println(UserDao instanceof BookDaoImpl);
        Method[] methods = UserDao.getClass().getDeclaredMethods();
        for (Method m: methods)
        {
            System.out.println(m.getName());
        }
        try {
            Method m = BillboardDaoImpl.class.getMethod("GetBillboardList", int.class, int.class, int.class);
            Class[] aclass = m.getParameterTypes();
            System.out.println(aclass[1].getName());
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
