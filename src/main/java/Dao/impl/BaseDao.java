package Dao.impl;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BaseDao {
    protected Connection conn;
    protected PreparedStatement pstmt;
    protected ResultSet rs;
    //protected int result;

    // JDBC driver and database URL
    static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://localhost:3306/bookstore";
    //user name and password.
    static final String USER = "team309_user1";
    static final String PASS = "team309";

    static {
        try {
            Class.forName(JDBC_DRIVER);
        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            System.out.println("Fail to load the database");
        }
    }

    /**
     * Build the connection with the database
     */
    public void getConnection() {

        try {
            conn = DriverManager.getConnection(DB_URL,USER,PASS);
            //System.out.println("Successfully connected with the database");
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            System.out.println("Fail to connect to the database, Please check the service, database status and user's account.");
        }
    }

    /**
     * close all the database connection
     */
    public void closeAll() {
        try {
            if (pstmt != null && !pstmt.isClosed()) {
                pstmt.close();
            }
            if (rs != null && !rs.isClosed()) {
                rs.close();
            }
            if (conn != null && !conn.isClosed()) {
                conn.close();
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            System.out.println("Exception occurs when closing the database connection.");
        }
    }

    /**
     * judge if there extsts such table.
     */
    /*
    public boolean existTable(String tableName){
        DatabaseMetaData meta;
        try {
            meta = (DatabaseMetaData) conn.getMetaData();
            ResultSet rs = meta.getTables(null, null, tableName, null);
            if (rs.next()) {
                return true;
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return false;
    }
    */
}
