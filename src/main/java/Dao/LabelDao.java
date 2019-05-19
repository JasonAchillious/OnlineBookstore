package Dao;

import Socket.InfoToFront;

import java.sql.SQLException;
import java.util.List;

public interface LabelDao {
    InfoToFront GetMainLabels() throws SQLException;

    InfoToFront GetSubLabels(String mainLabels) throws SQLException;
}
