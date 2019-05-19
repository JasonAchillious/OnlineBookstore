package Dao.impl;

import Dao.UserDao;
import Socket.InfoToFront;
import Socket.frontEnum.LoginStatus;

import java.sql.SQLException;

public class UserDaoImpl extends BaseDao implements UserDao {

    public InfoToFront Login(String UserName, String EncodedPassword) throws SQLException {

        InfoToFront infoToFront = new InfoToFront();
        infoToFront.setType("Login");
        try {
            getConnection();

            String sql = "select id, name, password_encode, authority from user u where u.name = ? ";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, UserName);
            rs = pstmt.executeQuery();
            if (rs.next()) {

                String password = rs.getString("password_encode").trim();
                int userId = rs.getInt("id");
                Boolean isAdmin = rs.getBoolean("authority");

                if (password.equals(EncodedPassword)) {
                    infoToFront.setLoginStatus(LoginStatus.Success);
                    infoToFront.setUserId(userId);
                    infoToFront.setAdmin(isAdmin);
                } else {
                    infoToFront.setLoginStatus(LoginStatus.WrongPassword);
                }
            } else {
                infoToFront.setLoginStatus(LoginStatus.NoSuchUser);
            }

            closeAll();
        }catch (SQLException e){
            e.printStackTrace();
        }
        return infoToFront;
    }

    @Override
    public InfoToFront GetMyReadList(int id, int from, int count) throws SQLException
    {
        return null;
    }

    @Override
    public InfoToFront SignUp(String userName, String mailAddr, String encodedPassword) throws SQLException
    {
        InfoToFront infoToFront = new InfoToFront();
        infoToFront.setType("SignUp");

        String sql = "INSERT INTO user (name, email, password_encode) value(?, ?, ?) ";

        pstmt = conn.prepareStatement(sql);
        pstmt.setString(1,userName);
        pstmt.setString(2, mailAddr);
        pstmt.setString(3, encodedPassword);

        int rows = pstmt.executeUpdate();

        if (rows == 1) infoToFront.setSuccess(true);
        else infoToFront.setSuccess(false);

        return infoToFront;
    }
}
