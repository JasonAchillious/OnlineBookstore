package Dao.impl;

import Dao.LabelDao;

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
    public List GetMainLabels() throws SQLException {
        List<String> labelList = new LinkedList<String>();
        getConnection();

        String sql = "select main from label l";
        pstmt = conn.prepareStatement(sql);
        rs = pstmt.executeQuery();

        while (rs.next()){
            labelList.add(rs.getString("main"));
        }

        closeAll();
        return labelList;
    }

    /**
     * There exists some problem need to discuss with partner;
     * The problem is no such main label and sublabel in database. All the labels are in the same levels.
     * @param MainLabels
     * @return
     * @throws SQLException
     */
    @Override
    public List GetSubLabels(String MainLabels) throws SQLException {
        return null;
    }
}
