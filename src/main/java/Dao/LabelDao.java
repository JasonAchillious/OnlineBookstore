package Dao;

import Socket.DataToFront;
import Socket.InfoFromFront;

import java.sql.SQLException;

public interface LabelDao {
    DataToFront GetMainLabels(InfoFromFront infoFromFront) throws SQLException;

    DataToFront GetSubLabels(InfoFromFront infoFromFront) throws SQLException;
}
