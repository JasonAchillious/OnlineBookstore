package Dao.impl;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import Dao.DanmuDao;
public class DanmuDaoImpl extends BaseDao implements DanmuDao {

    public String getDanmuContent(int danmuId) throws SQLException{
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
}
