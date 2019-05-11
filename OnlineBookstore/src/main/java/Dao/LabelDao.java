package Dao;

import java.sql.SQLException;

public interface LabelDao {
    String[] getMainLabels() throws SQLException;

    String[] getSubLabels(String mainLabels) throws SQLException;
}
