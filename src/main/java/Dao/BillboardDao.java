package Dao;

import Socket.InfoFromFront;
import Socket.InfoToFront;

import java.sql.SQLException;

public interface BillboardDao {

    // Get the book's id of a billboard.
    InfoToFront GetBillboardList(InfoFromFront infoFromFront) throws SQLException;

    // get the details of this billboard.
    InfoToFront GetTitleDescription(InfoFromFront infoFromFront) throws SQLException;
}
