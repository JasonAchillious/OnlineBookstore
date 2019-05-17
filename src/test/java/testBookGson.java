import com.google.gson.Gson;
import entities.Book;
import com.google.*;

public class testBookGson {
    public static void main(String[] args){
        Book book = new Book(123,"abcd");
        Gson gson = new Gson();
        String str =  gson.toJson(book);
        System.out.println(str);
    }
}
