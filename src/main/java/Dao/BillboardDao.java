package Dao;

import Socket.InfoToFront;

import java.sql.SQLException;

public interface BillboardDao {

    // Get the book's id of a billboard.
    int[] GetBillboardList(int Id, int from, int count) throws SQLException;

    // get the details of this billboard.
    InfoToFront GetTitleDescription(int billboardId) throws SQLException;
}
