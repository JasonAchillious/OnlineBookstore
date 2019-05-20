



public class Main {

    public static void main(String[] args)
    {
        Socket.BookhubServer bookhubServer = new Socket.BookhubServer();
        try
        {
            bookhubServer.turnOnServer();
        }catch (Exception e)
        {
            e.printStackTrace();
        }

    }
}
