package Dao.impl;

import Dao.LabelDao;
import Socket.DataToFront;
import Socket.InfoFromFront;

import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

public class LabelDaoImpl extends BaseDao implements LabelDao {

    /**
     * Get all the main label
     * Database do not have responding part
     * Now the version is to get all the Label
     */
    @Override
    public DataToFront GetMainLabels(InfoFromFront infoFromFront) throws SQLException {
        List<String> labelList = new LinkedList<String>();
        getConnection();

        String sql = "select name from label l";
        pstmt = conn.prepareStatement(sql);
        rs = pstmt.executeQuery();

        while (rs.next()){
            labelList.add(rs.getString("name"));
        }

        closeAll();
        return null;
    }

    /**
     *
     *  hot_spot（热度） algorithm: For all the books in each sublabel
     *  Caculate the wighting average of bought(0.8),review(0.1), danmu(0.1) amounts.
     *
     * param MainLabels
     * @return List of sublabel order by hot_spot.
     * @throws SQLException
     */
    @Override
    public DataToFront GetSubLabels(InfoFromFront infoFromFront) throws SQLException {
        String mainLabels = infoFromFront.getMainLabel();
        List<String> labelList = new LinkedList<String>();
        DataToFront dataToFront = new DataToFront();
        getConnection();

        String sql =
                "selct sl.name" +
                "( avg (bs.bus)*0.8 + avg(reviews)*0.1 + avg(danmus)*0.1 ) as hot_spot " +
                "from sub_label sl" +
                "join book b on b.id = sl.id" +
                "join book_stat bs on bs.id = b,id" +
                "join label l on sl.id = l.main_id" +
                "group by l.id" +
                "order by hot_spot desc" +
                        "where l.name = ?";

        pstmt.setString(1,mainLabels);
        pstmt = conn.prepareStatement(sql);
        rs = pstmt.executeQuery();

        while (rs.next()){
            labelList.add(rs.getString("sl.name"));
        }

        closeAll();
        return null;
    }
}
