package dao;

import java.sql.SQLException;

import socket.InfoFromFront;
import socket.InfoToFront;

public interface UserDao {
	// Check the user's info and return
	InfoToFront Login(InfoFromFront infoFromFront) throws SQLException;

	InfoToFront Logout(InfoFromFront infoFromFront) throws SQLException;

	// user register.
	InfoToFront SignUp(InfoFromFront infoFromFront) throws SQLException;
}
