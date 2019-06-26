package dao.impl;

import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import dao.DanmuDao;
import socket.InfoFromFront;
import socket.InfoToFront;

public class DanmuDaoImpl extends BaseDao implements DanmuDao {

	public DanmuDaoImpl() {
		super();
	}

	@Override
	public InfoToFront GetDanmuContent(InfoFromFront infoFromFront) throws SQLException {
		int danmuId = infoFromFront.getDanmuId();

		InfoToFront info = new InfoToFront();

		getConnection();

		String sql = "select content from danmu where danmu.id = ? ";
		pstmt = conn.prepareStatement(sql);
		pstmt.setInt(1, danmuId);

		rs = pstmt.executeQuery();

		while (rs.next()) {
			info.setContent(rs.getString("content"));
		}

		closeAll();

		return info;
	}

	@Override
	public InfoToFront GetMyDanmus(InfoFromFront infoFromFront) throws SQLException {
		int userId = infoFromFront.getUserId();

		List<Integer> mydanmu = new LinkedList<>();
		getConnection();

		String sql = "select id from danmu where user_id = ?;";
		pstmt = conn.prepareStatement(sql);
		pstmt.setInt(1, userId);

		rs = pstmt.executeQuery();

		while (rs.next()) {
			mydanmu.add(rs.getInt("id"));
		}
		closeAll();

		InfoToFront info = new InfoToFront();
		info.setIDs(mydanmu);
		return info;

	}

	@Override
	public InfoToFront GetDanmuOfBook(InfoFromFront infoFromFront) throws SQLException {
		int bookId, page;
		bookId = infoFromFront.getBookId();
		page = infoFromFront.getPage();
		List<Integer> danmuofbook = new LinkedList<>();
		getConnection();

		String sql = "select id from danmu where book_id = ? and page_num = ?";
		pstmt = conn.prepareStatement(sql);
		pstmt.setInt(1, bookId);
		pstmt.setInt(2, page);

		rs = pstmt.executeQuery();

		while (rs.next()) {
			danmuofbook.add(rs.getInt("id"));
		}

		closeAll();

		InfoToFront info = new InfoToFront();
		info.setIDs(danmuofbook);
		return info;
	}

	@Override
	public InfoToFront GetFullDanmuContent(InfoFromFront infoFromFront) throws SQLException {
		int danmuId = infoFromFront.getDanmuId();
		InfoToFront info = new InfoToFront();

		getConnection();

		String sql = "select d.content, d.page_num, b.name, d.edit_time" + " from danmu d"
				+ " join book b on d.book_id = b.id" + " where d.id = ?";
		pstmt = conn.prepareStatement(sql);
		pstmt.setInt(1, danmuId);

		rs = pstmt.executeQuery();

		while (rs.next()) {
			info.setContent(rs.getString("content"));
			info.setBookName(rs.getString("name"));
			info.setPageNum(rs.getInt("page_num"));
			info.setTimeStap(rs.getTimestamp("edit_time").getTime() / 1000);
		}

		closeAll();

		return info;
	}

	@Override
	public InfoToFront ChangeDanmu(InfoFromFront infoFromFront) throws SQLException {
		int danmuId = infoFromFront.getDanmuId();
		boolean isDeleteAction = infoFromFront.getDeleteAction();
		String newContent = infoFromFront.getNewContent();

		InfoToFront info = new InfoToFront();

		getConnection();
		String sql = "";
		if (isDeleteAction) {
			sql += "DELETE FROM danmu WHERE id = ?;";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, danmuId);
		}
		else {
			sql += "UPDATE danmu" + " SET content = ?" + " WHERE id = ?;";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, newContent);
			pstmt.setInt(2, danmuId);
		}

		int rows = pstmt.executeUpdate();
		info.setSuccess(rows == 1);

		closeAll();

		return info;
	}

	@Override
	public InfoToFront CreateDanmu(InfoFromFront infoFromFront) throws SQLException {
		String content;
		int bookId, userId, pageNum;
		content = infoFromFront.getContent();
		bookId = infoFromFront.getBookId();
		userId = infoFromFront.getUserId();
		pageNum = infoFromFront.getPageNum();

		InfoToFront info = new InfoToFront();

		getConnection();
		String sql = null;

		sql = "insert into danmu(user_id, book_id, page_num, content) values(?,?,?,?)";

		pstmt = conn.prepareStatement(sql);
		if (userId > 0)
			pstmt.setInt(1, userId);
		else
			pstmt.setNull(1, java.sql.Types.INTEGER);
		pstmt.setInt(2, bookId);
		pstmt.setInt(3, pageNum);
		pstmt.setString(4, content);

		int rows = pstmt.executeUpdate();
		info.setSuccess(rows == 1);

		closeAll();
		return info;

	}
}
