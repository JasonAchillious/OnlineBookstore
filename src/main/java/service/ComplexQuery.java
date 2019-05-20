package service;

import Socket.DataToFront;

import java.sql.SQLException;

public interface ComplexQuery {

    DataToFront GetFromQuery() throws SQLException;
}
