package Dao.impl;

import Dao.UserDao;
import Socket.DataToFront;
import Socket.InfoFromFront;
import Socket.frontEnum.LoginStatus;

import java.sql.SQLException;

public class UserDaoImpl extends BaseDao implements UserDao {

    public DataToFront Login(InfoFromFront infoFromFront) throws SQLException {

        String userName = infoFromFront.getUserName();
        String encodedPassword = infoFromFront.getEncodedPassword();
        DataToFront dataToFront = new DataToFront();
        dataToFront.setType("Login");
        try {
            getConnection();

            String sql = "select id, name, password_encode, authority from user u where u.name = ? ";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, userName);
            rs = pstmt.executeQuery();
            if (rs.next()) {

                String password = rs.getString("password_encode").trim();
                int userId = rs.getInt("id");
                Boolean isAdmin = rs.getBoolean("authority");

                if (password.equals(encodedPassword)) {
                    dataToFront.setLoginStatus(LoginStatus.Success);
                    dataToFront.setUserId(userId);
                    dataToFront.setAdmin(isAdmin);
                } else {
                    dataToFront.setLoginStatus(LoginStatus.WrongPassword);
                }
            } else {
                dataToFront.setLoginStatus(LoginStatus.NoSuchUser);
            }

            closeAll();
        }catch (SQLException e){
            e.printStackTrace();
        }
        return dataToFront;
    }

    @Override
    public DataToFront GetMyReadList(InfoFromFront infoFromFront) throws SQLException
    {
        int userid = infoFromFront.getUserId();
        int from = infoFromFront.getFrom();
        int count= infoFromFront.getCount();
        return null;
    }

    @Override
    public DataToFront SignUp(InfoFromFront infoFromFront) throws SQLException
    {
        String userName = infoFromFront.getUserName();
        String mailAddr = infoFromFront.getMailAddr();
        String encodedPassword = infoFromFront.getEncodedPassword();
        DataToFront dataToFront = new DataToFront();
        dataToFront.setType("SignUp");

        String sql = "INSERT INTO user (name, email, password_encode) value(?, ?, ?) ";

        pstmt = conn.prepareStatement(sql);
        pstmt.setString(1,userName);
        pstmt.setString(2, mailAddr);
        pstmt.setString(3, encodedPassword);

        int rows = pstmt.executeUpdate();

        if (rows == 1) dataToFront.setSuccess(true);
        else dataToFront.setSuccess(false);

        return dataToFront;
    }
}
