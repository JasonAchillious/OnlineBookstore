package dao.impl;

import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import dao.LabelDao;
import socket.InfoFromFront;
import socket.InfoToFront;

public class LabelDaoImpl extends BaseDao implements LabelDao {

	public LabelDaoImpl() {
		super();
	}

	@Override
	public InfoToFront GetMainLabels(InfoFromFront infoFromFront) throws SQLException {
		List<String> labelList = new LinkedList<String>();
		getConnection();

		String sql = "select name from label l";
		pstmt = conn.prepareStatement(sql);
		rs = pstmt.executeQuery();

		while (rs.next()) {
			labelList.add(rs.getString("name"));
		}

		closeAll();
		InfoToFront info = new InfoToFront();
		info.setMainLabels(labelList);
		return info;
	}

	@Override
	public InfoToFront GetSubLabels(InfoFromFront infoFromFront) throws SQLException {
		String mainLabels = infoFromFront.getMainLabel();
		List<String> labelList = new LinkedList<String>();

		getConnection();

		String sql = "select sl.name as sub_name,"
				+ " (avg(bs.buys) * 0.8 + avg(reviews) * 0.1 + avg(danmus) * 0.1) as hot_spot"
				+ " from sub_label sl" + " join label l on sl.main_id = l.id"
				+ " join book b on b.sublabel_id = sl.id" + " join book_stat bs on bs.book_id = b.id"
				+ " where l.name = ?" + " group by sl.id" + " order by hot_spot desc;";

		pstmt = conn.prepareStatement(sql);
		pstmt.setString(1, mainLabels);
		rs = pstmt.executeQuery();

		while (rs.next()) {
			labelList.add(rs.getString("sub_name"));
		}

		closeAll();

		InfoToFront info = new InfoToFront();
		info.setSubLabels(labelList);
		return info;
	}
}
