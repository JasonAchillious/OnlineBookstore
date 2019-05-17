
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import controller.ReflectionController;

public class Main {

    public static void main(String[] args) throws Exception {
        int port = 2307;
        ServerSocket server = new ServerSocket(port);

        System.out.println("Big brother is watching.");

        ExecutorService threadPool = Executors.newFixedThreadPool(10);

        while (true) {

            Socket socket = server.accept();

            Runnable runnable = () -> {
                try {
                    //建立缓冲流
                    InputStream is = socket.getInputStream();
                    InputStreamReader isr = new InputStreamReader(is);
                    BufferedReader br = new BufferedReader(isr);

                    String str;
                    String info;
                    StringBuilder contents = new StringBuilder();

                    System.out.println(444);
                    while ((str = br.readLine()) != null) {
                        contents.append(str);
                    }

                    info = contents.toString();

                    /*Gson gson = new Gson();
                    QueryReturn qr = gson.fromJson(info, QueryReturn.class);
                     */
                    //建立输出流
                    OutputStream outputStream = socket.getOutputStream();
                    outputStream.write(ReflectionController.methodCtrl(info).getBytes("UTF-8"));
                    outputStream.close();

                    br.close();
                    is.close();
                    isr.close();
                    socket.close();

                }catch (Exception e){
                  e.printStackTrace();
                }

            };

            threadPool.submit(runnable);
        }
    }
}
