import Socket.InfoFromFront;
import Socket.frontEnum.ChangeType;
import com.google.gson.Gson;

public class TestEnum {
    public static void main(String[] args){
        Gson gson = new Gson();
        InfoFromFront infoFromFront = new InfoFromFront();
        infoFromFront.setChangeType(1);
        ChangeType type = ChangeType.AddBook;
        System.out.println(type.ordinal());
    }
}
