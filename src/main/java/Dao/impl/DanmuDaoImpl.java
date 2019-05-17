package Dao.impl;
import java.sql.SQLException;

import Dao.DanmuDao;
import Socket.InfoToFront;

public class DanmuDaoImpl extends BaseDao implements DanmuDao {

    public String GetDanmuContent(int danmuId) throws SQLException{
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

        return content;
    }

    @Override
    public int[] GetMyDanmus(int userId, int from, int count) throws SQLException {
        return new int[0];
    }

    @Override
    public int[] GetDanmuOfBook(int bookId, int page, int limit) throws SQLException {
        return new int[0];
    }

    @Override
    public InfoToFront GetFullDanmuContent(int danmuId) throws SQLException {
        return null;
    }
}
