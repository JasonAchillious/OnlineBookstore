package Dao.impl;
import java.sql.SQLException;

import Dao.DanmuDao;
import Socket.DataToFront;
import Socket.InfoFromFront;

public class DanmuDaoImpl extends BaseDao implements DanmuDao {

    public DataToFront GetDanmuContent(InfoFromFront infoFromFront) throws SQLException{
        int danmuId = infoFromFront.getDanmuId();
        DataToFront dataToFront = new DataToFront();
        String content = null;
        getConnection();

        String sql = "select content from danmu where danmu.id = ? ";
        pstmt = conn.prepareStatement(sql);
        pstmt.setInt(1,danmuId);

        rs = pstmt.executeQuery();

        while (rs.next()){
            content = rs.getString("content");
            dataToFront.setContent(content);
        }

        closeAll();

        return dataToFront;
    }

    @Override
    public DataToFront GetMyDanmus(InfoFromFront infoFromFront) throws SQLException {
        int userId, from, count;
        userId = infoFromFront.getUserId();
        from = infoFromFront.getFrom();
        count = infoFromFront.getCount();

        return null;
    }

    @Override
    public DataToFront GetDanmuOfBook(InfoFromFront infoFromFront) throws SQLException {
        int bookId, page, limit;
        bookId = infoFromFront.getBookId();
        page = infoFromFront.getPage();

        return null;
    }

    @Override
    public DataToFront GetFullDanmuContent(InfoFromFront infoFromFront) throws SQLException
    {
        int danmuId = infoFromFront.getDanmuId();
        return null;
    }

    /**
     * @param infoFromFront
     * param danmuId
     * param isDeleteAction
     * param newContent
     */
    @Override
    public DataToFront ChangeDanmu(InfoFromFront infoFromFront) throws SQLException {
        DataToFront dataToFront = new DataToFront();
        dataToFront.setType("ChangeDanmu");
        int danmuId = infoFromFront.getDanmuId();
        boolean isDeleteAction = infoFromFront.getDeleteAction();
        String newContent = infoFromFront.getNewContent();

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
            dataToFront.setSuccess(true);
        }else {
            dataToFront.setSuccess(false);
        }

        closeAll();
        return dataToFront;
    }

    @Override
    public DataToFront CreateDanmu(InfoFromFront infoFromFront) throws SQLException {
        String content;
        int bookId, userId, pageNum;
        content = infoFromFront.getContent();
        bookId = infoFromFront.getBookId();
        userId = infoFromFront.getUserId();
        pageNum = infoFromFront.getPageNum();
        return null;
    }
}
