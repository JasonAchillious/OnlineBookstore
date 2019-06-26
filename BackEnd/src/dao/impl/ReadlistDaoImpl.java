package dao.impl;

import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import dao.ReadlistDao;
import socket.InfoFromFront;
import socket.InfoToFront;
import socket.frontEnum.ReadListChangeType;

public class ReadlistDaoImpl extends BaseDao implements ReadlistDao {

	public ReadlistDaoImpl() {
		super();
	}

	@Override
	public InfoToFront GetMyReadListsWithout(InfoFromFront infoFromFront) throws SQLException {
		int userid = infoFromFront.getUserId();
		int bookid = infoFromFront.getBookId();
		List<Integer> readlist = new LinkedList<>();

		getConnection();
		String sql = "select readlist" + " from (select distinct id as readlist" + " from readlist"
				+ " where create_user = ?) a"
				+ " left join (select distinct readlist_id from readlist_book where book_id = ?) b"
				+ " on a.readlist = b.readlist_id" + " where readlist_id is null;";

		pstmt = conn.prepareStatement(sql);
		pstmt.setInt(1, userid);
		pstmt.setInt(2, bookid);

		rs = pstmt.executeQuery();

		while (rs.next()) {
			readlist.add(rs.getInt("readlist"));
		}

		closeAll();

		InfoToFront info = new InfoToFront();
		info.setIDs(readlist);
		return info;
	}

	@Override
	public InfoToFront GetMyCreatedReadLists(InfoFromFront infoFromFront) throws SQLException {
		int userId = infoFromFront.getUserId();
		List<Integer> myreadlist = new LinkedList<>();

		getConnection();

		String sql = "select id" + " from readlist" + " where create_user = ?;";
		pstmt = conn.prepareStatement(sql);

		pstmt.setInt(1, userId);

		rs = pstmt.executeQuery();

		while (rs.next()) {
			myreadlist.add(rs.getInt("id"));

		}

		closeAll();

		InfoToFront info = new InfoToFront();
		info.setIDs(myreadlist);
		return info;
	}

	@Override
	public InfoToFront GetMyFollowedReadLists(InfoFromFront infoFromFront) throws SQLException {
		int userId = infoFromFront.getUserId();
		List<Integer> myreadlist = new LinkedList<>();

		getConnection();

		String sql = "select r.id" + " from readlist r" + " join readlist_follow rf on r.id = rf.readlist_id"
				+ " where rf.user_id = ?;";
		pstmt = conn.prepareStatement(sql);
		pstmt.setInt(1, userId);

		rs = pstmt.executeQuery();

		while (rs.next()) {
			myreadlist.add(rs.getInt("id"));
		}

		closeAll();

		InfoToFront info = new InfoToFront();
		info.setIDs(myreadlist);
		return info;
	}

	@Override
	public InfoToFront ChangeReadList(InfoFromFront infoFromFront) throws SQLException {
		int readListId, alteredBookId = -1;
		ReadListChangeType changeType = ReadListChangeType.values()[infoFromFront.getChangeType()];
		String alteredText = null;
		readListId = infoFromFront.getReadListId();
		if (infoFromFront.getAlteredBookId() != null)
			alteredBookId = infoFromFront.getAlteredBookId();
		if (infoFromFront.getAlteredText() != null)
			alteredText = infoFromFront.getAlteredText();

		getConnection();

		String sql = "";
		switch (changeType) {
		case AddBook:
			sql += "insert into readlist_book(book_id, readlist_id)" + " values (?,?);";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, alteredBookId);
			pstmt.setInt(2, readListId);
			break;
		case RemoveList:
			sql += "delete from readlist where id = ?;";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, readListId);
			break;
		case DeleteBook:
			sql += "delete from readlist_book where readlist_id = ? and book_id = ?;";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, readListId);
			pstmt.setInt(2, alteredBookId);
			break;
		case ChangeDescription:
			sql += "UPDATE readlist " + "SET description = ? where id = ?;";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, alteredText);
			pstmt.setInt(2, readListId);
			break;
		case ChangeTitle:
			sql += "UPDATE readlist " + "SET title = ? where id = ?;";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, alteredText);
			pstmt.setInt(2, readListId);
			break;
		default:
			break;
		}

		int rows = pstmt.executeUpdate();
		InfoToFront info = new InfoToFront();
		info.setSuccess(rows == 1);

		closeAll();
		return info;
	}

	@Override
	public InfoToFront CreateReadList(InfoFromFront infoFromFront) throws SQLException {
		int userId = infoFromFront.getUserId();
		String title = infoFromFront.getTitle();
		String description = infoFromFront.getDescription();

		String sql = "insert into readlist (create_user, title, description) values (?,?,?);";
		getConnection();

		pstmt = conn.prepareStatement(sql);
		pstmt.setInt(1, userId);
		pstmt.setString(2, title);
		pstmt.setString(3, description);

		int rows = pstmt.executeUpdate();
		InfoToFront info = new InfoToFront();
		info.setSuccess(rows == 1);

		closeAll();
		return info;
	}

	@Override
	public InfoToFront FollowReadList(InfoFromFront infoFromFront) throws SQLException {
		int userId = infoFromFront.getUserId();
		boolean isFollowAction = infoFromFront.getIsFollowAction();
		int readlistId = infoFromFront.getReadListId();

		String sql = "";

		if (isFollowAction) {
			sql += "insert into readlist_follow(user_id, readlist_id) values (?,?);";
		}
		else {
			sql += "delete from readlist_follow where user_id = ? and readlist_id = ?;";
		}

		getConnection();
		pstmt = conn.prepareStatement(sql);
		pstmt.setInt(1, userId);
		pstmt.setInt(2, readlistId);

		int rows = pstmt.executeUpdate();
		InfoToFront info = new InfoToFront();
		info.setSuccess(rows == 1);

		closeAll();
		return info;
	}
}
