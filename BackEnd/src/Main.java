import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;

import dao.impl.BaseDao;
import socket.BookhubServer;

public class Main {

	public static void main(String[] args) throws Exception {

		Properties prop = new Properties();

		InputStream in = Main.class.getResourceAsStream("/res/load.cnf");
		if (in == null) {
			in = Main.class.getResourceAsStream("res/load.cnf");
		}
		try (BufferedReader reader = new BufferedReader(new InputStreamReader(in))) {
			prop.load(reader);
		} catch (Exception e) {
			System.err.println("No configuration file (load.cnf) found");
			System.exit(1);
		}

		BookhubServer.setPorts(Integer.parseInt(prop.getProperty("port_user", "2317")),
				Integer.parseInt(prop.getProperty("port_admin", "2318")));
		BaseDao.setUserInfo(prop.getProperty("user", "root"), prop.getProperty("password_user", "112233"));
		BaseDao.setAdminInfo(prop.getProperty("admin", "root"), prop.getProperty("password_admin", "112233"));
		BaseDao.setDatabaseName(prop.getProperty("database", "bookstore"));

		Thread userThread = new Thread(new Runnable() {
			@Override
			public void run() {
				BookhubServer userServer = new BookhubServer();
				try {
					userServer.turnOnServer(20);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

		Thread adminThread = new Thread(new Runnable() {
			@Override
			public void run() {
				BookhubServer adminServer = new BookhubServer(true);
				try {
					adminServer.turnOnServer(20);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

		// Scanner scanner = new Scanner(System.in);
		// scanner.next();
		// new BaseDao().getConnection();
		// scanner.close();

		userThread.start();
		adminThread.start();
		userThread.join();
		adminThread.join();
	}
}
