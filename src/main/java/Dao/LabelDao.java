package Dao;

import java.sql.SQLException;
import java.util.List;

public interface LabelDao {
    List GetMainLabels() throws SQLException;

    List GetSubLabels(String mainLabels) throws SQLException;
}
