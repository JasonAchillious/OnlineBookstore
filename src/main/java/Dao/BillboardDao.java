package Dao;

import Socket.InfoFromFront;
import Socket.DataToFront;

import java.sql.SQLException;

public interface BillboardDao {

    // Get the book's id of a billboard.
    DataToFront GetBillboardList(InfoFromFront infoFromFront) throws SQLException;

    // get the details of this billboard.
    DataToFront GetTitleDescription(InfoFromFront infoFromFront) throws SQLException;
}
