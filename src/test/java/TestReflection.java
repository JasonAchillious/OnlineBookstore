import Dao.impl.BillboardDaoImpl;
import Dao.impl.LabelDaoImpl;
import com.google.gson.Gson;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

public class TestReflection {
    public static void main(String[] args){
        LabelDaoImpl labelDao = new LabelDaoImpl();

        try {
            Method method = LabelDaoImpl.class.getDeclaredMethod("getMainLabels");
            List<String> strs = (List<String>) method.invoke( labelDao);

            Gson gson = new Gson();
            String str = gson.toJson(strs);
            System.out.println(str);

            Method m = BillboardDaoImpl.class.getMethod("GetBillboardList", int.class, int.class, int.class);
            Class[] aclass = m.getParameterTypes();
            System.out.println(aclass);
        }catch (NoSuchMethodException e){
            e.printStackTrace();
        }catch (IllegalAccessException e){
            e.printStackTrace();
        }catch (InvocationTargetException e){
            e.printStackTrace();
        }

    }
}
