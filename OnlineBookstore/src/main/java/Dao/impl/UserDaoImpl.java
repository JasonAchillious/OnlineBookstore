package Dao.impl;

import Dao.UserDao;
import cotroller.QueryReturn;
import cotroller.returnEnum.LoginStatus;

import java.sql.SQLException;

public class UserDaoImpl extends BaseDao implements UserDao {

    public QueryReturn userLogin(String UserName, String EncodedPassword) throws SQLException {

        QueryReturn qr = new QueryReturn();
        qr.setType("Login");
        getConnection();

        String sql = "select id, name, password_encode, authority from user u where u.name = ? ";
        pstmt = conn.prepareStatement(sql);
        pstmt.setString(1,UserName);
        rs = pstmt.executeQuery();
        if (rs.next()){

            String password = rs.getString("password_encode");
            int userId = rs.getInt("id");
            Boolean isAdmin = rs.getBoolean("authority");

            if(password == EncodedPassword){
                qr.setLoginStatus(LoginStatus.Success);
                qr.setUserId(userId);
                qr.setAdmin(isAdmin);
            }else {
                qr.setLoginStatus(LoginStatus.WrongPassword);
            }
        }else {
            qr.setLoginStatus(LoginStatus.NoSuchUser);
        }

        closeAll();
        return qr;
    }
}
