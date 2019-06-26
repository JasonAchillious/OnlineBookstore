package dao.impl;

import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import dao.BillboardDao;
import socket.InfoFromFront;
import socket.InfoToFront;

public class BillboardDaoImpl extends BaseDao implements BillboardDao {

	public BillboardDaoImpl() {
		super();
	}

	@Override
	public InfoToFront GetBookListBooks(InfoFromFront infoFromFront) throws SQLException {
		boolean isbillboard = infoFromFront.getIsBillboard();

		int booklistid = infoFromFront.getBookListID();
		int from = infoFromFront.getFrom();
		int count = infoFromFront.getCount();

		List<Integer> books = new LinkedList<Integer>();

		String sql = String.format("select bk.book_id as booklist" + " from %1$s l"
				+ " join %1$s_book bk on l.id = bk.%1$s_id" + " where l.id = ?" + " limit ? offset ?",
				isbillboard ? "billboard" : "readlist");

		getConnection();
		pstmt = conn.prepareStatement(sql);
		pstmt.setInt(1, booklistid);
		pstmt.setInt(2, count);
		pstmt.setInt(3, from);
		rs = pstmt.executeQuery();

		while (rs.next()) {
			books.add(rs.getInt("booklist"));
		}

		closeAll();

		InfoToFront info = new InfoToFront();
		info.setIDs(books);
		return info;
	}

	@Override
	public InfoToFront GetTitleDescription(InfoFromFront infoFromFront) throws SQLException {
		boolean isbillboard = infoFromFront.getIsBillboard();
		int booklistid = infoFromFront.getBookListID();
		int userid = infoFromFront.getUserId();

		InfoToFront info = new InfoToFront();

		getConnection();

		if (isbillboard == true) {
			String sql = "select title, description from billboard where id = ? ";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, booklistid);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				info.setTitle(rs.getString("title"));
				info.setDescription(rs.getString("description"));
			}
		}
		else {
			String sql = "select r.title, u.name, r.description, r.edit_time,"
					+ " follow_amount(r.id) as followamount";
			if (userid > 0) {
				sql += ", (rf.user_id = ? and rf.user_id is not null) as isfollowed" + " from readlist r"
						+ " left outer join readlist_follow rf on r.id = rf.readlist_id"
						+ " join user u on u.id = r.create_user" + " where r.id = ?" + " group by isfollowed"
						+ " order by isfollowed desc limit 1;";
				pstmt = conn.prepareStatement(sql);
				pstmt.setInt(1, userid);
				pstmt.setInt(2, booklistid);
			}
			else {
				sql += " from readlist r" + " join user u on u.id = r.create_user" + " where r.id = ?;";
				pstmt = conn.prepareStatement(sql);
				pstmt.setInt(1, booklistid);
			}
			rs = pstmt.executeQuery();

			while (rs.next()) {
				info.setTitle(rs.getString("title"));
				info.setDescription(rs.getString("description"));
				info.setCreateUser(rs.getString("name"));
				info.setTimeStap(rs.getTimestamp("edit_time").getTime() / 1000);
				info.setFollowAmount(rs.getInt("followamount"));
				if (userid > 0)
					info.setFollowed(rs.getBoolean("isfollowed"));
				else
					info.setFollowed(false);
			}
		}

		closeAll();

		return info;
	}
}
