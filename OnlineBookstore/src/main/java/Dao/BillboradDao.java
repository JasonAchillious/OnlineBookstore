package Dao;

import cotroller.QueryReturn;

import java.sql.SQLException;

public interface BillboradDao {

    // Get the book's id of a billboard.
    int[] getBillboardList(int Id, int from, int count) throws SQLException;

    // get the details of this billboard.
    QueryReturn getTitleDescription(int billboardId) throws SQLException;
}
