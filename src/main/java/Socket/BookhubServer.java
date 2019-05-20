package Socket;

import com.google.gson.Gson;
import controller.ReflectionController;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BookhubServer {


    /**
     * Turn on the Server.
     * Use socket server to build the listening port.
     * Get the infomation from the front.
     * Invoke the controller section to deal with the information and take action.
     * Get the infomation from the action that we have done.(from the database)
     * Return the info in the form of Json.
     *
     * Final edit on 2019.5.19 Jason Zhao.
     *
     * @throws Exception
     */
    public void turnOnServer()throws Exception
    {
        int port = 2307;
        ServerSocket server = new ServerSocket(port);
        System.out.println("Big brother is watching.");

        ExecutorService threadPool = Executors.newFixedThreadPool(100);

        while (true) {

            Socket socket = server.accept();

            Runnable runnable = () -> {
                try {
                    //建立缓冲流
                    InputStream is = socket.getInputStream();
                    InputStreamReader isr = new InputStreamReader(is);
                    BufferedReader br = new BufferedReader(isr);
                    StringBuilder contents = new StringBuilder();
                    Gson gson = new Gson();

                    //读取前端发送信息
                    String str;
                    String info;
                    while ((str = br.readLine()) != null) {
                        contents.append(str);
                    }
                    info = contents.toString();

                    //调用Controller模块
                    ReflectionController rc = new ReflectionController();
                    DataToFront dataToFront = rc.methodCtrl(info);

                    //建立输出流
                    OutputStream outputStream = socket.getOutputStream();
                    outputStream.write(gson.toJson(dataToFront).getBytes("UTF-8"));
                    outputStream.close();

                    br.close();
                    is.close();
                    isr.close();
                    socket.close();

                } catch (Exception e) {
                    e.printStackTrace();
                }

            };

            threadPool.submit(runnable);
        }
    }
}
