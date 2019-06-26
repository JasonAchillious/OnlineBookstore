package service;

import java.sql.SQLException;

import socket.InfoFromFront;
import socket.InfoToFront;

public interface ComplexQuery {

	InfoToFront GetFromQuery(InfoFromFront infoFromFront) throws SQLException;
}
