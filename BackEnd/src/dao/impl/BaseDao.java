package dao.impl;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BaseDao {
	protected Connection conn;
	protected PreparedStatement pstmt;
	protected ResultSet rs;
	// protected int result;

	// JDBC driver and database URL
	protected static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
	protected static final String PREFIX_JDBC = "jdbc:mysql://localhost:3306/",
			POSTFIX_JDBC = "?serverTimezone=Asia/Shanghai&useSSL=false&allowPublicKeyRetrieval=true";

	protected static String DATABASE = "bookstore";
	protected static String NAME_USER = "root", PASSWORD_USER = "112233";
	protected static String NAME_ADMIN = "root", PASSWORD_ADMIN = "112233";

	public static void setDatabaseName(String name) {
		DATABASE = name;
	}

	public static void setUserInfo(String name, String pass) {
		NAME_USER = name;
		PASSWORD_USER = pass;
	}

	public static void setAdminInfo(String name, String pass) {
		NAME_ADMIN = name;
		PASSWORD_ADMIN = pass;
	}

	protected String userName;
	protected String userPassword;
	protected final String DB_URL;

	static {
		try {
			Class.forName(JDBC_DRIVER);
			new com.google.gson.Gson();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			System.err.println("Fail to load the database driver");
		}
	}

	public BaseDao() {
		DB_URL = PREFIX_JDBC + DATABASE + POSTFIX_JDBC;
		userName = NAME_USER;
		userPassword = PASSWORD_USER;
	}

	/**
	 * Build the connection with the database
	 */
	public void getConnection() {

		try {
			conn = DriverManager.getConnection(DB_URL, userName, userPassword);
			// Since the default of MySQL is repeatable-read, there is no need
			// pstmt = conn.prepareStatement("set session transaction isolation level repeatable read;");
			// pstmt.execute();
			// pstmt = conn.prepareStatement("set global sql_safe_updates = 1;");
			// pstmt.execute();
		} catch (SQLException e) {
			e.printStackTrace();
			System.err.println("Fail to connect to the database. "
					+ "Please check the service, database status and user's account.");
		}
	}

	/**
	 * close all the database connection
	 */
	public void closeAll() {
		try {
			if (getPstmt() != null && !getPstmt().isClosed()) {
				getPstmt().close();
			}
			if (rs != null && !rs.isClosed()) {
				rs.close();
			}
			if (getConn() != null && !getConn().isClosed()) {
				getConn().close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
			System.err.println("Exception occurs when closing the database connection.");
		}
	}

	public PreparedStatement getPstmt() {
		return pstmt;
	}

	public void setPstmt(PreparedStatement pstmt) {
		this.pstmt = pstmt;
	}

	public Connection getConn() {
		return conn;
	}

	public void setConn(Connection conn) {
		this.conn = conn;
	}
}
