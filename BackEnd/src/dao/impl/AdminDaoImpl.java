package dao.impl;

import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import dao.AdminDao;
import entities.ResultHtml;
import socket.InfoFromAdmin;
import socket.InfoToFront;
import socket.frontEnum.ReadListChangeType;

/**
 * @author Kevin Sun
 */
public class AdminDaoImpl extends BaseDao implements AdminDao {

	public AdminDaoImpl() {
		super();
		this.userName = NAME_ADMIN;
		this.userPassword = PASSWORD_ADMIN;
	}

	private static String SQLExceptionToString(SQLException e) {
		String result = String.format("Error code: %d\nSQL state: %s\nMessage: %s\n", e.getErrorCode(),
				e.getSQLState(), e.getMessage());
		while ((e = e.getNextException()) != null)
			result += String.format("Error code: %d\nSQL state: %s\nMessage: %s\n", e.getErrorCode(),
					e.getSQLState(), e.getMessage());
		return result;
	}

	@Override
	public String PerformSQL(InfoFromAdmin infoFromAdmin) {
		// int userId = infoFromAdmin.getUserId();
		String sql = infoFromAdmin.getSQL();
		if (sql == null || sql.trim().length() == 0)
			return "Error: Empty SQL String!";

		String result = null;
		try {
			getConnection();
			pstmt = conn.prepareStatement(sql);
			boolean isQuery = pstmt.execute();
			if (isQuery) {
				rs = pstmt.getResultSet();
				int nColumn = rs.getMetaData().getColumnCount();
				String[] nameColumn = new String[nColumn];
				for (int i = 0; i < nColumn; i++) {
					nameColumn[i] = rs.getMetaData().getColumnName(i + 1);
				}
				List<ResultHtml> rows = new LinkedList<ResultHtml>();
				rows.add(new ResultHtml(true, nameColumn));
				while (rs.next()) {
					String[] rowNow = new String[nColumn];
					for (int i = 0; i < nColumn; i++) {
						rowNow[i] = rs.getString(i + 1);
					}
					rows.add(new ResultHtml(rowNow));
				}
				StringBuilder sb = new StringBuilder();
				sb.append("<html>\n<body>\n<table border=\"1\">\n");
				for (ResultHtml resultHtml : rows) {
					sb.append(resultHtml.toHtmlTableRow());
				}
				sb.append("</table>\n</body>\n</html>");
				result = sb.toString();
			}
			else {
				result = "Execute success.";
			}
			closeAll();
		} catch (SQLException e) {
			result = SQLExceptionToString(e);
		}

		return result;
	}

	@Override
	public InfoToFront ChangeBillboard(InfoFromAdmin infoFromAdmin) {
		int adminId = infoFromAdmin.getUserId();
		int readListId, alteredBookId = -1;
		ReadListChangeType changeType = ReadListChangeType.values()[infoFromAdmin.getChangeType()];
		String alteredText = null;
		readListId = infoFromAdmin.getBillboardId();
		if (infoFromAdmin.getAlteredBookId() != null)
			alteredBookId = infoFromAdmin.getAlteredBookId();
		if (infoFromAdmin.getAlteredText() != null)
			alteredText = infoFromAdmin.getAlteredText();

		boolean success = false;
		String result = null;
		try {
			String sql = "";
			getConnection();
			switch (changeType) {
			case AddBook:
				sql += "insert into billboard_book(book_id, billboard_id)" + " values (?,?);";
				pstmt = conn.prepareStatement(sql);
				pstmt.setInt(1, alteredBookId);
				pstmt.setInt(2, readListId);
				break;
			case RemoveList:
				sql += "delete from billboard where id = ? and create_admin = ?;";
				pstmt = conn.prepareStatement(sql);
				pstmt.setInt(1, readListId);
				pstmt.setInt(2, adminId);
				break;
			case DeleteBook:
				sql += "delete from billboard_book where billboard_id = ? and book_id = ?;";
				pstmt = conn.prepareStatement(sql);
				pstmt.setInt(1, readListId);
				pstmt.setInt(2, alteredBookId);
				break;
			case ChangeDescription:
				sql += "UPDATE billboard " + "SET description = ? where id = ? and create_admin = ?;";
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, alteredText);
				pstmt.setInt(2, readListId);
				pstmt.setInt(3, adminId);
				break;
			case ChangeTitle:
				sql += "UPDATE billboard " + "SET title = ? where id = ? and create_admin = ?;";
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, alteredText);
				pstmt.setInt(2, readListId);
				pstmt.setInt(3, adminId);
				break;
			default:
				break;
			}
			success = pstmt.executeUpdate() == 1;
			closeAll();
		} catch (SQLException e) {
			success = false;
			result = SQLExceptionToString(e);
		}

		InfoToFront info = new InfoToFront();
		info.setSuccess(success);
		info.setMessage(result);

		return info;
	}

}
