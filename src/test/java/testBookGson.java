import com.google.gson.Gson;
import entities.Book;
import com.google.*;

import java.util.LinkedList;
import java.util.List;

public class testBookGson {
    public static void main(String[] args){
        int[] book = new int[]{1,2,3};
        List list = new LinkedList();
        list.add(1);
        list.add(2);
        list.add(3);
        Gson gson = new Gson();
        String str1 =  gson.toJson(book);
        String str2 = gson.toJson(list);
        System.out.println(str1);
        System.out.println(str2);
    }
}
