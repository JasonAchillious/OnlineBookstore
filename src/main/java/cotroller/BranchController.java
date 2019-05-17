package cotroller;

import Dao.impl.BookDaoImpl;
import Dao.impl.LabelDaoImpl;
import Dao.impl.UserDaoImpl;
import Socket.InfoFromFront;
import Socket.InfoToFront;
import com.google.gson.Gson;

import java.util.List;

public class BranchController {

    public static String branchCtrl(String FrontInfo)
    {
        String str = null;

        Gson gson = new Gson();
        InfoFromFront infoFromFront = gson.fromJson(FrontInfo, InfoFromFront.class);

        String type = infoFromFront.getType();

        InfoToFront infoToFront;
        List list;
        int[] ints;
        try {
            switch (type) {
                case "Login":
                    infoToFront = new UserDaoImpl().Login(infoFromFront.getUserName(), infoFromFront.getEncodedPassword());
                    str = gson.toJson(infoToFront);
                    break;
                case "GetBookSummary":
                    infoToFront = new BookDaoImpl().GetBookSummary(infoFromFront.getBookId());
                    str = gson.toJson(infoFromFront);
                    break;
                case "GetMainLabels":
                    list = new LabelDaoImpl().GetMainLabels();
                    str = gson.toJson(list);
                    break;
                case "GetShelfBooks":
                    ints = new BookDaoImpl().GetRelatedBooks(infoFromFront.getBookId(), infoFromFront.getFrom(), infoFromFront.getCount());
                    str = gson.toJson(ints);
                    break;
                    //继续扩展：将Dao中的所有接口中的所有方法都 以如上形式来写:
                    // case 方法名：
                    // 返回类型 = new impl类名().调用方法（传递参数）
                    // str = gson.toJson(返回对象）;
                    // break;
                default:
                    str = "请求错误";
            }

            return str;
        }catch (Exception e){
            e.printStackTrace();
        }
        return str;
    }
}