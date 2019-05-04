package Socket;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import com.google.gson.*;
import entities.Book;



public class SocketServer {
    public static void main(String[] args) throws Exception {
        // 监听指定的端口
        int port = 2307;
        ServerSocket server = new ServerSocket(port);

        System.out.println("Big brother is watching.");

        ExecutorService threadPool = Executors.newFixedThreadPool(100);

        while (true) {
            Socket socket = server.accept();

            Runnable runnable = ()->{
                /*try {
                    // 建立好连接后，从socket中获取输入流，并建立缓冲区进行读取
                    InputStream inputStream = socket.getInputStream();
                    byte[] bytes = new byte[1024];
                    int len;
                    StringBuilder sb = new StringBuilder();
                    while ((len = inputStream.read(bytes)) != -1) {
                        // 注意指定编码格式，发送方和接收方一定要统一，建议使用UTF-8
                        sb.append(new String(bytes, 0, len, "UTF-8"));
                    }
                    System.out.println("get message from client: " + sb);
                    inputStream.close();
                    socket.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }*/



                try {
                    getBookInfo(socket);
                } catch (Exception e){
                    e.printStackTrace();
                }
            };


            threadPool.submit(runnable);
        }

    }

    public static void getBookInfo(Socket socket) throws Exception {
        //Scanner in = new Scanner(System.in);
        InputStreamReader isr;
        BufferedReader br;
        //OutputStreamWriter osw;
        //BufferedWriter rw;

        try {

            //建立缓冲流
            InputStream is = socket.getInputStream();
            isr = new InputStreamReader(is);
            br = new BufferedReader(isr);

            // Gson 解析
            String str;
            String info;
            StringBuffer contents = new StringBuffer();
            while((str = br.readLine())!=null) {
                contents.append(str);
            }

            info = contents.toString();
            Gson gson = new Gson();
            Book book = gson.fromJson(info, Book.class);

            System.out.println("ID:" + book.getBookId());
            System.out.println("Name:" + book.getBookName());

            /*String info =null;
            while((info=br.readLine())!=null){
                System.out.println("我是服务器，客户端说："+info);
            }*/

            socket.shutdownInput();

            OutputStream outputStream = socket.getOutputStream();
            outputStream.write("Hello Client,I get the message.".getBytes("UTF-8"));
            outputStream.close();
            br.close();
            is.close();
            isr.close();
            socket.close();
            //serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
