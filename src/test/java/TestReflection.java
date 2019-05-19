import Dao.impl.BillboardDaoImpl;
import Dao.impl.BookDaoImpl;
import Dao.impl.LabelDaoImpl;
import com.google.gson.Gson;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.List;

public class TestReflection {
    public static void main(String[] args){
        LabelDaoImpl labelDao = new LabelDaoImpl();

        try {

            Method m = BookDaoImpl.class.getMethod("GetBookDetail", int.class, int.class);
            int a = m.getParameterCount();
            System.out.println(a);
        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
