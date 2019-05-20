package controller;

import Socket.DataToFront;
import Socket.InfoFromFront;
import com.google.gson.Gson;

import java.util.List;

public class BranchController {

    public static String branchCtrl(String FrontInfo)
    {
        String str = null;

        Gson gson = new Gson();
        InfoFromFront infoFromFront = gson.fromJson(FrontInfo, InfoFromFront.class);

        String type = infoFromFront.getType();

        DataToFront dataToFront;
        List list;
        Integer[] ints;
        try {
            switch (type) {




                    //继续扩展：将Dao中的所有接口中的所有方法都 以如上形式来写:
                    // case 方法名：
                    // 返回类型 = new impl类名().调用方法（传递参数）
                    // str = gson.toJson(返回对象）;
                    // break;
                default:
                    str = "Illegal Request";
            }

            return str;
        }catch (Exception e){
            e.printStackTrace();
        }
        return str;
    }
}
