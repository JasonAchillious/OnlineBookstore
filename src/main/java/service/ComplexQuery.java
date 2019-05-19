package service;

import Socket.InfoToFront;

import java.sql.SQLException;

public interface ComplexQuery {

    InfoToFront GetFromQuery() throws SQLException;
}
