package Dao.impl;
import java.sql.SQLException;

import Dao.DanmuDao;
import Socket.InfoToFront;

public class DanmuDaoImpl extends BaseDao implements DanmuDao {

    public InfoToFront GetDanmuContent(int danmuId) throws SQLException{
        String content = null;
        getConnection();

        String sql = "select content from danmu where danmu.id = ? ";
        pstmt = conn.prepareStatement(sql);
        pstmt.setInt(1,danmuId);

        rs = pstmt.executeQuery();

        while (rs.next()){
            content = rs.getString("content");
        }

        closeAll();

        return null;
    }

    @Override
    public InfoToFront GetMyDanmus(int userId, int from, int count) throws SQLException {
        return null;
    }

    @Override
    public InfoToFront GetDanmuOfBook(int bookId, int page, int limit) throws SQLException {
        return null;
    }

    @Override
    public InfoToFront GetFullDanmuContent(int danmuId) throws SQLException {
        return null;
    }

    /**
     *
     * @param danmuId
     * @param isDeleteAction
     * @param newContent
     */
    @Override
    public InfoToFront ChangeDanmu(int danmuId, boolean isDeleteAction, String newContent) throws SQLException {
        InfoToFront infoToFront = new InfoToFront();
        infoToFront.setType("ChangeDanmu");
        getConnection();
        String sql = null;
        if(isDeleteAction) {
            sql = "DELETE FROM danmu d WHERE d.id = ?";
        } else {
            sql = "set sql_safe_updates = 1" +
                    "UPDATE danmu d" +
                    "SET content = ?" +
                    "WHERE d.id = ?";
        }

        pstmt = conn.prepareStatement(sql);
        int rows = pstmt.executeUpdate();
        if (rows == 1){
            infoToFront.setSuccess(true);
        }else {
            infoToFront.setSuccess(false);
        }

        closeAll();
        return infoToFront;
    }

    @Override
    public InfoToFront CreateDanmu(String content, int bookId, int userId, int PageNum) throws SQLException {
        return null;
    }
}
