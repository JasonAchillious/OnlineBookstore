package socket;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import controller.AbstractController;
import controller.AdminReflectionController;
import controller.UserReflectionController;
import dao.impl.BaseDao;

public class BookhubServer {

	/**
	 * Used since we cannot actually implement paying progress
	 */
	public static final Map<Integer, Thread> ongoingTransactions = new HashMap<>();

	/**
	 * @author Kevin Sun
	 * @param userId
	 * @param bookId
	 * @throws SQLException 
	 */
	public static void waitForPaying(final int userId, final int bookId) throws SQLException {
		Thread t = new Thread(new Runnable() {
			public void run() {
				try {
					Thread.sleep(5000);

					BaseDao base = new BaseDao();
					base.getConnection();
					// directly set paid
					String sql = "update transaction " + "set paid = true " + "where user_id = ? and book_id = ?;";
					base.setPstmt(base.getConn().prepareStatement(sql));
					base.getPstmt().setInt(1, userId);
					base.getPstmt().setInt(2, bookId);
					base.getPstmt().executeUpdate();

					base.closeAll();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		ongoingTransactions.put(userId, t);
		t.start();
	}

	private static int USER_PORT = 2317, ADMIN_PROT = 2318;

	public static void setPorts(int user, int admin) {
		USER_PORT = user;
		ADMIN_PROT = admin;
	}

	private boolean isAdmin;

	private ServerSocket server;

	public BookhubServer() {
		this(false);
	}

	public BookhubServer(boolean isadmin) {
		this.isAdmin = isadmin;
	}

	/**
	 * Turn on the Server.<br>
	 * Use socket server to build the listening port.<br>
	 * Get the information from the front.<br>
	 * Invoke the controller section to deal with the information and take action.<br>
	 * Get the information from the action that we have done.(from the database)<br>
	 * Return the info in the form of Json.<br>
	 * <p>
	 * Edit on 2019.5.19 Jason Zhao.<br>
	 * Edit on 2019.5.23 Kevin Sun.<br>
	 *
	 * @throws Exception
	 */
	public void turnOnServer(final int N) throws Exception {
		// 监听指定的端口
		server = new ServerSocket(isAdmin ? ADMIN_PROT : USER_PORT);

		System.out.printf("Big brother is watching port %d.\n", server.getLocalPort());

		ExecutorService threadPool = Executors.newFixedThreadPool(N);

		while (true) {
			Socket socket = server.accept();

			Runnable runnable = () -> {
				try {
					// 建立好连接后，从socket中获取输入流，并建立缓冲区进行读取
					InputStream inputStream = socket.getInputStream();
					OutputStream outputStream = socket.getOutputStream();

					byte[] bytes = new byte[1 << 14];
					int len;
					StringBuilder sb = new StringBuilder();
					while ((len = inputStream.read(bytes)) != -1) {
						// 注意指定编码格式，发送方和接收方一定要统一，建议使用UTF-8
						sb.append(new String(bytes, 0, len, "UTF-8"));
					}
					if (sb.toString().trim().length() < 10)
						return;

					System.out.printf("Get message from %d client:\n%s\n", server.getLocalPort(), sb.toString());

					AbstractController controller = isAdmin ? new AdminReflectionController()
							: new UserReflectionController();
					String send = controller.methodController(sb.toString());
					System.out.println("Send message to client:\n" + send);

					outputStream.write(send.getBytes("UTF-8"));
					outputStream.flush();

					outputStream.close();
					inputStream.close();
					socket.close();

					System.out.println("Finish\n");
				} catch (Exception e) {
					e.printStackTrace();
				}
			};

			threadPool.submit(runnable);
		}
	}
}
