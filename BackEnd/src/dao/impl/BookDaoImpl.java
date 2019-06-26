package dao.impl;

import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import dao.BookDao;
import socket.BookhubServer;
import socket.InfoFromFront;
import socket.InfoToFront;

public class BookDaoImpl extends BaseDao implements BookDao {

	public BookDaoImpl() {
		super();
	}

	@Override
	public InfoToFront GetBookSummary(InfoFromFront infoFromFront) throws SQLException {
		int bookId = infoFromFront.getBookId();

		InfoToFront info = new InfoToFront();

		getConnection();

		String sql = "select b.name as book_name," + " book_cover_url," + " a.name as author_name" + " from book b"
				+ " join book_author ba" + " on b.id = ba.book_id" + " join author a on a.id = ba.author_id"
				+ " where b.id = ?" + " and ba.is_main_author is true;";
		pstmt = conn.prepareStatement(sql);
		pstmt.setInt(1, bookId);
		rs = pstmt.executeQuery();
		// if or while?
		if (rs.next()) {
			info.setBookCoverUrl(rs.getString("book_cover_url"));
			info.setBookName(rs.getString("book_name"));
			info.setAuthorName(rs.getString("author_name"));
		}

		closeAll();
		return info;
	}

	@Override
	public InfoToFront GetBookQuasiDetail(InfoFromFront infoFromFront) throws SQLException {
		int bookId = infoFromFront.getBookId();

		InfoToFront info = GetBookSummary(infoFromFront);

		getConnection();

		String sql = "select l.name as main, sl.name as sub, price_now(b.id) as price, bs.discount, bs.overall_rating"
				+ " from book b" + " join book_stat bs on b.id = bs.book_id"
				+ " join sub_label sl on b.sublabel_id = sl.id" + " join label l on sl.main_id = l.id"
				+ " where b.id = ?;";
		pstmt = conn.prepareStatement(sql);
		pstmt.setInt(1, bookId);
		rs = pstmt.executeQuery();

		while (rs.next()) {
			info.setMainAndSubLabel(rs.getString("main") + "-" + rs.getString("sub"));
			info.setPrice(rs.getDouble("price"));
			info.setDisCount(rs.getInt("discount"));
			info.setOverallRating(rs.getDouble("overall_rating"));
		}

		closeAll();

		return info;
	}

	@Override
	public InfoToFront GetShelfBooks(InfoFromFront infoFromFront) throws SQLException {
		int userId = infoFromFront.getUserId();
		List<Integer> shelf = new LinkedList<>();

		getConnection();

		String sql = "select book_id from transaction where user_id = ? and paid is true;";
		pstmt = conn.prepareStatement(sql);
		pstmt.setInt(1, userId);
		rs = pstmt.executeQuery();

		while (rs.next()) {
			shelf.add(rs.getInt("book_id"));
		}

		InfoToFront info = new InfoToFront();
		info.setIDs(shelf);
		return info;
	}

	@Override
	public InfoToFront GetBookDetail(InfoFromFront infoFromFront) throws SQLException {
		InfoToFront info = GetBookQuasiDetail(infoFromFront);

		int bookId = infoFromFront.getBookId();
		int userId = infoFromFront.getUserId();

		String otherAuthorSql = null;
		otherAuthorSql = "select a.name, ba.is_translator" + " from author a"
				+ " join book_author ba on a.id = ba.author_id" + " join book b on ba.book_id = b.id"
				+ " where is_main_author is false" + " and b.id = ?;";

		getConnection();
		pstmt = conn.prepareStatement(otherAuthorSql);
		pstmt.setInt(1, bookId);
		rs = pstmt.executeQuery();

		StringBuilder stringBuilder = new StringBuilder();
		while (rs.next()) {
			boolean isTranslator = rs.getBoolean("is_translator");
			String authorName = rs.getString("name");

			if (isTranslator)
				stringBuilder.append(authorName + "(Translator) ");
			else
				stringBuilder.append(authorName + " ");

		}

		info.setOtherAuthors(stringBuilder.toString().trim());

		String bookDetailSQL = "select b.description, b.publish_time, b.version, p.name, "
				+ "b.ISBN, b.pages, bs.buys, bs.danmus, bs.previews, bs.reviews"
				+ " from book b join book_stat bs on b.id = bs.book_id" + " join press p on b.press_id = p.id"
				+ " where b.id = ?;";

		pstmt = conn.prepareStatement(bookDetailSQL);
		pstmt.setInt(1, bookId);
		rs = pstmt.executeQuery();

		while (rs.next()) {
			info.setDescription(rs.getString("description"));
			info.setISBN(rs.getString("ISBN"));
			info.setBuyAmount(rs.getInt("buys"));
			info.setDanmuAmount(rs.getInt("danmus"));
			info.setPreviewAmount(rs.getInt("previews"));
			info.setReviewAmount(rs.getInt("reviews"));
			info.setPageCount(rs.getInt("pages"));

			String pressName = rs.getString("name");
			String publish_Time = rs.getDate("publish_time").toString();
			String version = rs.getString("version");

			info.setPublishInfo(pressName + " / " + publish_Time + " / " + version + " Edition");
		}

		if (userId != -1) {
			String canAddReadlistSQL = "select count(r.id) as cnt" + " from readlist r"
					+ " left outer join readlist_book rb on r.id = rb.readlist_id" + " where r.create_user = ?"
					+ " and (rb.book_id != ? or rb.readlist_id is null);";

			pstmt = conn.prepareStatement(canAddReadlistSQL);
			pstmt.setInt(1, userId);
			pstmt.setInt(2, bookId);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				info.setCanAddReadList(rs.getInt("cnt") > 0);
			}

			String canAddWishlistSQL = "select *" + " from wish_list w" + " where user_id = ? and book_id = ?;";
			pstmt = conn.prepareStatement(canAddWishlistSQL);
			pstmt.setInt(1, userId);
			pstmt.setInt(2, bookId);
			rs = pstmt.executeQuery();

			if (rs.next() == false)
				info.setCanAddWishList(true);
			else
				info.setCanAddWishList(false);

			String canBuySQL = "select user_id, book_id, paid" + " from transaction"
					+ " where user_id = ? and book_id = ? and paid is true;";

			pstmt = conn.prepareStatement(canBuySQL);
			pstmt.setInt(1, userId);
			pstmt.setInt(2, bookId);
			rs = pstmt.executeQuery();

			if (rs.next() == false)
				info.setCanBuy(true);
			else {
				info.setCanBuy(false);
				info.setCanAddWishList(false);
			}
		}
		else {
			info.setCanAddReadList(false);
			info.setCanAddWishList(false);
			info.setCanBuy(false);
		}
		return info;
	}

	@Override
	public InfoToFront GetRelatedBooks(InfoFromFront infoFromFront) throws SQLException {
		int bookid = infoFromFront.getBookId();
		int from = infoFromFront.getFrom();
		int count = infoFromFront.getCount();

		List<Integer> relatedbooks = new LinkedList<Integer>();
		getConnection();

		String sql = "select id from book" + " where label_id in (select label_id from book where id = ?)"
				+ " order by id limit ? offset ?;";
		pstmt = conn.prepareStatement(sql);
		pstmt.setInt(1, bookid);
		pstmt.setInt(2, count + 1);
		pstmt.setInt(3, from);

		rs = pstmt.executeQuery();

		while (rs.next()) {
			relatedbooks.add(rs.getInt("id"));
		}
		relatedbooks.remove(Integer.valueOf(bookid));
		while (relatedbooks.size() > count) {
			relatedbooks.remove(relatedbooks.size() - 1);
		}

		closeAll();

		InfoToFront info = new InfoToFront();
		info.setIDs(relatedbooks);
		return info;
	}

	@Override
	public InfoToFront GetBookPreview(InfoFromFront infoFromFront) throws SQLException {
		int bookId = infoFromFront.getBookId();

		InfoToFront info = new InfoToFront();

		getConnection();

		String sql = "select preview_url from book where id = ?;";
		pstmt = conn.prepareStatement(sql);
		pstmt.setInt(1, bookId);
		rs = pstmt.executeQuery();
		while (rs.next()) {
			info.setURL(rs.getString("preview_url"));
		}

		sql = "update book_stat set previews = previews + 1 where book_id = ?;";
		pstmt = conn.prepareStatement(sql);
		pstmt.setInt(1, bookId);
		pstmt.executeUpdate();

		closeAll();

		return info;
	}

	@Override
	public InfoToFront DownloadBook(InfoFromFront infoFromFront) throws SQLException {
		int bookId = infoFromFront.getBookId();

		InfoToFront info = new InfoToFront();

		getConnection();

		String sql = "select download_url from book where id = ?";

		pstmt = conn.prepareStatement(sql);
		pstmt.setInt(1, bookId);

		rs = pstmt.executeQuery();

		while (rs.next()) {
			info.setURL(rs.getString("download_url"));
		}

		closeAll();

		return info;
	}

	@Override
	public InfoToFront GetBookKey(InfoFromFront infoFromFront) throws SQLException {
		int bookId = infoFromFront.getBookId();
		int userId = infoFromFront.getUserId();

		InfoToFront info = new InfoToFront();

		getConnection();

		String buySQL = "select * from transaction where user_id = ? and book_id = ?;";
		pstmt = conn.prepareStatement(buySQL);
		pstmt.setInt(1, userId);
		pstmt.setInt(2, bookId);
		rs = pstmt.executeQuery();
		if (!rs.next()) {
			info.setPrivateKey(null);
			closeAll();
			return info;
		}

		String sql = "select pdf_password from book where id = ?;";
		pstmt = conn.prepareStatement(sql);
		pstmt.setInt(1, bookId);
		rs = pstmt.executeQuery();
		while (rs.next()) {
			info.setPrivateKey(rs.getString("pdf_password"));
		}
		closeAll();

		return info;
	}

	@Override
	public InfoToFront BuyBook(InfoFromFront infoFromFront) throws SQLException {
		int userId = infoFromFront.getUserId();
		int bookId = infoFromFront.getBookId();

		String bookName = null;
		double price = 0;

		InfoToFront infoToFront = new InfoToFront();
		getConnection();

		String getSql = "select name, price_now(id) as price from book where book.id = ?";
		pstmt = conn.prepareStatement(getSql);
		pstmt.setInt(1, bookId);
		rs = pstmt.executeQuery();
		while (rs.next()) {
			bookName = rs.getString("name");
			price = rs.getDouble("price");
		}

		String checkSql = "select paid from transaction where user_id = ? and book_id = ?";
		pstmt = conn.prepareStatement(checkSql);
		pstmt.setInt(1, userId);
		pstmt.setInt(2, bookId);
		rs = pstmt.executeQuery();
		if (rs.next()) {
			if (rs.getBoolean("paid")) {
				infoToFront.setSuccess(false);
				return infoToFront;
			}
			else {
				closeAll();

				infoToFront.setURL(
						String.format("https://qr.alipay.com/?user=team309&name=%s&price=%f", bookName, price));
				BookhubServer.waitForPaying(userId, bookId);

				return infoToFront;
			}
		}
		else {
			String insertSql = "insert into transaction(user_id, book_id) values (?,?);";
			pstmt = conn.prepareStatement(insertSql);
			pstmt.setInt(1, userId);
			pstmt.setInt(2, bookId);
			int rows = pstmt.executeUpdate();
			if (rows == 1) {
				infoToFront.setSuccess(true);
			}
			else {
				closeAll();
				return infoToFront;
			}

			closeAll();

			infoToFront.setURL(
					String.format("https://qr.alipay.com/?user=team309&name=%s&price=%f", bookName, price));
			BookhubServer.waitForPaying(userId, bookId);

			return infoToFront;
		}
	}

	@Override
	public InfoToFront CheckBuyComplete(InfoFromFront infoFromFront) throws SQLException {
		int userId = infoFromFront.getUserId();
		int bookId = infoFromFront.getBookId();

		try {
			BookhubServer.ongoingTransactions.get(userId).interrupt();
		} catch (Exception e) {
			// do nothing
		}

		InfoToFront infoToFront = new InfoToFront();

		getConnection();
		String sql = "select paid from transaction where user_id = ? and book_id = ?";
		pstmt = conn.prepareStatement(sql);
		pstmt.setInt(1, userId);
		pstmt.setInt(2, bookId);

		rs = pstmt.executeQuery();

		while (rs.next()) {
			infoToFront.setSuccess(rs.getBoolean("paid"));
		}
		closeAll();
		return infoToFront;
	}

	@Override
	public InfoToFront CancelTransaction(InfoFromFront infoFromFront) throws SQLException {
		int userId = infoFromFront.getUserId();
		int bookId = infoFromFront.getBookId();

		try {
			BookhubServer.ongoingTransactions.get(userId).interrupt();
		} catch (Exception e) {
			// do nothing
		}

		InfoToFront infoToFront = new InfoToFront();

		getConnection();

		boolean paid = false;
		String sqlQuery = "select paid from transaction where transaction.user_id = ? and transaction.book_id = ?;";
		pstmt = conn.prepareStatement(sqlQuery);
		pstmt.setInt(1, userId);
		pstmt.setInt(2, bookId);

		rs = pstmt.executeQuery();
		while (rs.next()) {
			paid = rs.getBoolean("paid");
		}
		if (paid) {
			infoToFront.setSuccess(false);
			return infoToFront;
		}

		String sql = "delete from transaction where user_id = ? and book_id = ?";
		pstmt = conn.prepareStatement(sql);
		pstmt.setInt(1, userId);
		pstmt.setInt(2, bookId);

		int rows = pstmt.executeUpdate();
		if (rows == 1) {
			infoToFront.setSuccess(true);
		}
		else {
			infoToFront.setSuccess(false);
		}
		closeAll();

		return infoToFront;
	}

	@Override
	public InfoToFront GetAddPrice(InfoFromFront infoFromFront) throws SQLException {
		int userId = infoFromFront.getUserId();
		int bookId = infoFromFront.getBookId();

		getConnection();
		String sql = "select add_price from wish_list where user_id = ? and book_id = ?;";
		pstmt = conn.prepareStatement(sql);
		pstmt.setInt(1, userId);
		pstmt.setInt(2, bookId);
		rs = pstmt.executeQuery();

		InfoToFront info = new InfoToFront();
		while (rs.next()) {
			info.setPrice(rs.getDouble("add_price"));
		}

		closeAll();

		return info;
	}

}
