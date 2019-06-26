package service.impl;

import java.sql.SQLException;
import java.util.ArrayList;

import dao.impl.BaseDao;
import service.ComplexQuery;
import socket.InfoFromFront;
import socket.InfoToFront;
import socket.frontEnum.ContentType;
import socket.frontEnum.OrderType.BillboardsOrderType;
import socket.frontEnum.OrderType.BooksOrderType;
import socket.frontEnum.OrderType.ReadlistsOrderType;
import socket.frontEnum.TimeSpanType;

/**
 * @author Kevin Sun
 */
public class ComplexQueryImpl extends BaseDao implements ComplexQuery {

	public ComplexQueryImpl() {
		super();
	}

	@Override
	public InfoToFront GetFromQuery(InfoFromFront infoFromFront) throws SQLException {
		// the info from front must have following
		ContentType type;
		boolean orderDesend;
		int orderOrdinal;
		// get previous results
		if (infoFromFront.getSearchType() == null) {
			System.err.println("infoFromFront SearchType null error");
			return null;
		}
		type = ContentType.values()[infoFromFront.getSearchType().intValue()];
		if (infoFromFront.getOrderDescend() == null) {
			System.err.println("infoFromFront OrderDescend null error");
			return null;
		}
		orderDesend = infoFromFront.getOrderDescend().booleanValue();
		if (infoFromFront.getOrder() == null) {
			System.err.println("infoFromFront Order null error");
			return null;
		}
		orderOrdinal = infoFromFront.getOrder().intValue();

		// From and Count must not be null
		int from, count;
		if (infoFromFront.getFrom() == null) {
			System.err.println("infoFromFront From null error");
			return null;
		}
		if (infoFromFront.getCount() == null) {
			System.err.println("infoFromFront Count null error");
			return null;
		}
		from = infoFromFront.getFrom().intValue();
		count = infoFromFront.getCount().intValue();

		// get true order
		switch (type) {
		case Books:
			if (orderOrdinal < 0 || orderOrdinal >= BooksOrderType.values().length) {
				System.err.println("infoFromFront Order value error");
				return null;
			}
			return GetBooksByQuery(BooksOrderType.values()[orderOrdinal], orderDesend, infoFromFront, from, count);
		case Billboards:
			if (orderOrdinal < 0 || orderOrdinal >= BillboardsOrderType.values().length) {
				System.err.println("infoFromFront Order value error");
				return null;
			}
			return GetBillboardsByQuery(BillboardsOrderType.values()[orderOrdinal], orderDesend, infoFromFront,
					from, count);
		case ReadLists:
			if (orderOrdinal < 0 || orderOrdinal >= ReadlistsOrderType.values().length) {
				System.err.println("infoFromFront Order value error");
				return null;
			}
			return GetReadlistsByQuery(ReadlistsOrderType.values()[orderOrdinal], orderDesend, infoFromFront, from,
					count);
		default:
			return null;
		}
	}

	private static final int MAX_COUNT = 20;

	private static String createArrayPlaceholder(int size) {
		StringBuilder sb = new StringBuilder();
		sb.append("(");
		for (int i = 0; i < size; i++) {
			sb.append("?,");
		}
		sb.deleteCharAt(sb.length() - 1);
		sb.append(")");
		return sb.toString();
	}

	private static String createArrayPlaceholder(String strToCopy, int size, int removeFirstN) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < size; i++) {
			sb.append(strToCopy);
		}
		sb.delete(0, removeFirstN);
		return sb.toString();
	}

	private static Object[] unpackInfoByDefault(InfoFromFront info) {
		boolean includeFree;
		if (info.getIncludeFreeBooks() == null)
			includeFree = true;
		else
			includeFree = info.getIncludeFreeBooks();

		TimeSpanType timeSpan;
		if (info.getTimeRangeType() == null)
			timeSpan = TimeSpanType.All;
		else
			timeSpan = TimeSpanType.values()[info.getTimeRangeType().intValue()];

		int timeRangeMin, timeRangeMax;
		if (info.getTimeRange() == null) {
			timeRangeMin = Integer.MIN_VALUE;
			timeRangeMax = 0;
		}
		else {
			timeRangeMin = info.getTimeRange()[0];
			timeRangeMax = info.getTimeRange()[1];
		}

		int pageRangeMin, pageRangeMax;
		if (info.getPageRange() == null) {
			pageRangeMin = 0;
			pageRangeMax = Integer.MAX_VALUE;
		}
		else {
			pageRangeMin = info.getPageRange()[0];
			pageRangeMax = info.getPageRange()[1];
		}

		String[] tempLabels = info.getLabelFilters();
		ArrayList<String> subLabels = new ArrayList<String>();
		ArrayList<String> mainLabels = new ArrayList<String>();
		if (tempLabels != null) {
			for (String l : tempLabels) {
				if (l.contains("-All"))
					mainLabels.add(l.replaceAll("-All", ""));
				else {
					String[] ss = l.split("-", 2);
					subLabels.add(ss[1]);
				}
			}
		}

		return new Object[] {includeFree, timeSpan, timeRangeMin, timeRangeMax, pageRangeMin, pageRangeMax,
				subLabels, mainLabels};
	}

	@SuppressWarnings("unchecked")
	private InfoToFront GetBooksByQuery(BooksOrderType order, boolean orderDesend, InfoFromFront info, int from,
			int count) throws SQLException {
		if (info.getQueryText() == null || info.getQueryText().trim().length() == 0) {
			// Every thing except for SearchType, OrderDescend, Order are actually default value

			String queryBookIdSQL = "select b.id from book b ";
			switch (order) {
			case Recommend:
				queryBookIdSQL += "order by b.id";
				break;
			case Time:
				queryBookIdSQL += "order by b.publish_time";
				break;
			case Rating:
				queryBookIdSQL += "join book_stat s on b.id = s.book_id order by s.overall_rating";
				break;
			case Price:
				queryBookIdSQL += "order by price_now(b.id)";
				break;
			case Discount:
				queryBookIdSQL += "join book_stat s on b.id = s.book_id order by s.discount";
				break;
			case ReviewAmount:
				queryBookIdSQL += "join book_stat s on b.id = s.book_id order by s.reviews";
				break;
			case BuyAmount:
				queryBookIdSQL += "join book_stat s on b.id = s.book_id order by s.buys";
				break;
			case PreviewAmount:
				queryBookIdSQL += "join book_stat s on b.id = s.book_id order by s.previews";
				break;
			case PageCount:
				queryBookIdSQL += "order by b.pages";
				break;
			default:
				break;
			}

			queryBookIdSQL += String.format(" %s limit ? offset ?;", orderDesend ? "desc" : "asc");

			getConnection();
			pstmt = conn.prepareStatement(queryBookIdSQL);
			pstmt.setInt(1, count > MAX_COUNT ? MAX_COUNT : count);
			pstmt.setInt(2, from);
			rs = pstmt.executeQuery();

			ArrayList<Integer> ids = new ArrayList<Integer>(count);
			while (rs.next()) {
				ids.add(rs.getInt(1));
			}

			closeAll();

			InfoToFront infoToFront = new InfoToFront();
			infoToFront.setIDs(ids);
			return infoToFront;
		}
		else {
			// Full query with default values
			String[] queryText = info.getQueryText().split(" ");

			boolean includeFree;
			TimeSpanType timeSpan;
			int timeRangeMin, timeRangeMax;
			int pageRangeMin, pageRangeMax;
			ArrayList<String> subLabels, mainLabels;
			Object[] result = unpackInfoByDefault(info);
			includeFree = (boolean) result[0];
			timeSpan = (TimeSpanType) result[1];
			timeRangeMin = (int) result[2];
			timeRangeMax = (int) result[3];
			pageRangeMin = (int) result[4];
			pageRangeMax = (int) result[5];
			subLabels = (ArrayList<String>) result[6];
			mainLabels = (ArrayList<String>) result[7];

			String timeRangeString = timeRangeMin == Integer.MIN_VALUE
					? String.format("<= date_add(now(), interval %d %s)", timeRangeMax, timeSpan.toString())
					: String.format(
							"between date_add(now(), interval %d %s) " + "and date_add(now(), interval %d %s)",
							timeRangeMin, timeSpan.toString(), timeRangeMax, timeSpan.toString());
			if (timeSpan == TimeSpanType.All)
				timeRangeString = "<= now()";

			String pageRangeString = pageRangeMax == Integer.MAX_VALUE ? String.format(">= %d", pageRangeMin)
					: String.format("between %d and %d", pageRangeMin, pageRangeMax);

			String labelString = String.format(" and (%s or %s)",
					mainLabels.size() > 0 ? "ml.name in " + createArrayPlaceholder(mainLabels.size()) : "",
					subLabels.size() > 0 ? "sl.name in " + createArrayPlaceholder(subLabels.size()) : "");
			if (mainLabels.size() == 0 && subLabels.size() == 0)
				labelString = labelString.replaceAll(" or ", "").replaceAll(" and \\(\\)", "");
			else if (mainLabels.size() == 0 || subLabels.size() == 0)
				labelString = labelString.replaceAll(" or ", "");

			String queryBookIdSQL = "select distinct b.id from book b" + " join book_stat s on b.id = s.book_id"
					+ " join label ml on b.label_id = ml.id " + " join sub_label sl on b.sublabel_id = sl.id"
					+ " join press p on b.press_id = p.id" + " join book_author ba on b.id = ba.book_id"
					+ " join author a on ba.author_id = a.id"
					+ String.format(" where b.publish_time %s and b.pages %s", timeRangeString, pageRangeString)
					+ labelString + (includeFree ? " " : " and b.original_price > 0") + " and ("
					+ createArrayPlaceholder(
							" or b.name like ? or b.description like ? or p.name like ? or a.name like ?"
									+ " or sl.name like ? or ml.name like ?",
							queryText.length, 4)
					+ ")" + " order by ";
			final int queryTextCount = 6;

			switch (order) {
			case Recommend:
				queryBookIdSQL += "b.id";
				break;
			case Time:
				queryBookIdSQL += "b.publish_time";
				break;
			case Rating:
				queryBookIdSQL += "s.overall_rating";
				break;
			case Price:
				queryBookIdSQL += "price_now(b.id)";
				break;
			case Discount:
				queryBookIdSQL += "s.discount";
				break;
			case ReviewAmount:
				queryBookIdSQL += "s.reviews";
				break;
			case BuyAmount:
				queryBookIdSQL += "s.buys";
				break;
			case PreviewAmount:
				queryBookIdSQL += "s.previews";
				break;
			case PageCount:
				queryBookIdSQL += "b.pages";
				break;
			case DanmuAmount:
				queryBookIdSQL += "s.danmus";
				break;
			default:
				break;
			}

			queryBookIdSQL += String.format(" %s limit ? offset ?;", orderDesend ? "desc" : "asc");

			getConnection();
			pstmt = conn.prepareStatement(queryBookIdSQL);
			int i = 1;
			if (mainLabels.size() > 0) {
				for (String s : mainLabels) {
					pstmt.setString(i++, s);
				}
			}
			if (subLabels.size() > 0) {
				for (String s : subLabels) {
					pstmt.setString(i++, s);
				}
			}
			for (String s : queryText) { // for names filter
				String q = "%" + s + "%";
				for (int ii = 0; ii < queryTextCount; ++ii)
					pstmt.setString(i++, q);

			}
			pstmt.setInt(i++, count > MAX_COUNT ? MAX_COUNT : count);
			pstmt.setInt(i++, from);
			rs = pstmt.executeQuery();

			ArrayList<Integer> ids = new ArrayList<Integer>(count);
			while (rs.next()) {
				ids.add(rs.getInt(1));
			}

			closeAll();

			InfoToFront infoToFront = new InfoToFront();
			infoToFront.setIDs(ids);
			return infoToFront;
		}
	}

	private InfoToFront GetBillboardsByQuery(BillboardsOrderType order, boolean orderDesend, InfoFromFront info,
			int from, int count) throws SQLException {
		if (info.getQueryText() == null || info.getQueryText().trim().length() == 0) {
			// Every thing except for SearchType, OrderDescend, Order are actually default value

			String queryBookIdSQL = "select b.id from billboard b ";
			switch (order) {
			case Recommend:
				queryBookIdSQL += "order by b.id";
				break;
			case Time:
				queryBookIdSQL += "order by b.edit_time";
				break;
			default:
				break;
			}

			queryBookIdSQL += String.format(" %s limit ? offset ?;", orderDesend ? "desc" : "asc");

			getConnection();
			pstmt = conn.prepareStatement(queryBookIdSQL);
			pstmt.setInt(1, count > MAX_COUNT ? MAX_COUNT : count);
			pstmt.setInt(2, from);
			rs = pstmt.executeQuery();

			ArrayList<Integer> ids = new ArrayList<Integer>(count);
			while (rs.next()) {
				ids.add(rs.getInt(1));
			}

			closeAll();

			InfoToFront infoToFront = new InfoToFront();
			infoToFront.setIDs(ids);
			return infoToFront;
		}
		else {
			// Full query with default values
			String[] queryText = info.getQueryText().split(" ");

			// boolean includeFree;
			TimeSpanType timeSpan;
			int timeRangeMin, timeRangeMax;
			// int pageRangeMin, pageRangeMax;
			// ArrayList<String> subLabels, mainLabels;
			Object[] result = unpackInfoByDefault(info);
			// includeFree = (boolean) result[0];
			timeSpan = (TimeSpanType) result[1];
			timeRangeMin = (int) result[2];
			timeRangeMax = (int) result[3];
			// pageRangeMin = (int) result[4];
			// pageRangeMax = (int) result[5];

			String timeRangeString = timeRangeMin == Integer.MIN_VALUE
					? String.format("<= date_add(now(), interval %d %s)", timeRangeMax, timeSpan.toString())
					: String.format(
							"between date_add(now(), interval %d %s) " + "and date_add(now(), interval %d %s)",
							timeRangeMin, timeSpan.toString(), timeRangeMax, timeSpan.toString());
			if (timeSpan == TimeSpanType.All)
				timeRangeString = "<= now()";

			String queryBookIdSQL = "select distinct b.id from billboard b"
					+ String.format(" where b.edit_time %s", timeRangeString) + " and ("
					+ createArrayPlaceholder(" or b.title like ? or b.description like ?", queryText.length, 4)
					+ ")" + " order by ";

			switch (order) {
			case Recommend:
				queryBookIdSQL += "b.id";
				break;
			case Time:
				queryBookIdSQL += "b.edit_time";
				break;
			default:
				break;
			}

			queryBookIdSQL += String.format(" %s limit ? offset ?;", orderDesend ? "desc" : "asc");

			getConnection();
			pstmt = conn.prepareStatement(queryBookIdSQL);
			int i = 1;
			for (String s : queryText) { // for names filter
				String q = "%" + s + "%";
				for (int ii = 0; ii < 2; ++ii)
					pstmt.setString(i++, q);

			}
			pstmt.setInt(i++, count > MAX_COUNT ? MAX_COUNT : count);
			pstmt.setInt(i++, from);
			rs = pstmt.executeQuery();

			ArrayList<Integer> ids = new ArrayList<Integer>(count);
			while (rs.next()) {
				ids.add(rs.getInt(1));
			}

			closeAll();

			InfoToFront infoToFront = new InfoToFront();
			infoToFront.setIDs(ids);
			return infoToFront;
		}

	}

	private InfoToFront GetReadlistsByQuery(ReadlistsOrderType order, boolean orderDesend, InfoFromFront info,
			int from, int count) throws SQLException {
		if (info.getQueryText() == null || info.getQueryText().trim().length() == 0) {
			// Every thing except for SearchType, OrderDescend, Order are actually default value

			String queryBookIdSQL = "select b.id from readlist b order by ";
			switch (order) {
			case Recommend:
				queryBookIdSQL += "b.id";
				break;
			case Time:
				queryBookIdSQL += "b.edit_time";
				break;
			case FollowAmount:
				queryBookIdSQL += "follow_amount(b.id)";
				break;
			default:
				break;
			}

			queryBookIdSQL += String.format(" %s limit ? offset ?;", orderDesend ? "desc" : "asc");

			getConnection();
			pstmt = conn.prepareStatement(queryBookIdSQL);
			pstmt.setInt(1, count > MAX_COUNT ? MAX_COUNT : count);
			pstmt.setInt(2, from);
			rs = pstmt.executeQuery();

			ArrayList<Integer> ids = new ArrayList<Integer>(count);
			while (rs.next()) {
				ids.add(rs.getInt(1));
			}

			closeAll();

			InfoToFront infoToFront = new InfoToFront();
			infoToFront.setIDs(ids);
			return infoToFront;
		}
		else {
			// Full query with default values
			String[] queryText = info.getQueryText().split(" ");

			// boolean includeFree;
			TimeSpanType timeSpan;
			int timeRangeMin, timeRangeMax;
			// int pageRangeMin, pageRangeMax;
			// ArrayList<String> subLabels, mainLabels;
			Object[] result = unpackInfoByDefault(info);
			// includeFree = (boolean) result[0];
			timeSpan = (TimeSpanType) result[1];
			timeRangeMin = (int) result[2];
			timeRangeMax = (int) result[3];
			// pageRangeMin = (int) result[4];
			// pageRangeMax = (int) result[5];

			String timeRangeString = timeRangeMin == Integer.MIN_VALUE
					? String.format("<= date_add(now(), interval %d %s)", timeRangeMax, timeSpan.toString())
					: String.format(
							"between date_add(now(), interval %d %s) " + "and date_add(now(), interval %d %s)",
							timeRangeMin, timeSpan.toString(), timeRangeMax, timeSpan.toString());
			if (timeSpan == TimeSpanType.All)
				timeRangeString = "<= now()";

			String queryBookIdSQL = "select distinct b.id from readlist b" + " join user u on b.create_user = u.id"
					+ String.format(" where b.edit_time %s", timeRangeString) + " and ("
					+ createArrayPlaceholder(" or b.title like ? or b.description like ? or u.name like ?",
							queryText.length, 4)
					+ ")" + " order by ";

			switch (order) {
			case Recommend:
				queryBookIdSQL += "b.id";
				break;
			case Time:
				queryBookIdSQL += "b.edit_time";
				break;
			case FollowAmount:
				queryBookIdSQL += "follow_amount(b.id)";
				break;
			default:
				break;
			}

			queryBookIdSQL += String.format(" %s limit ? offset ?;", orderDesend ? "desc" : "asc");

			getConnection();
			pstmt = conn.prepareStatement(queryBookIdSQL);
			int i = 1;
			for (String s : queryText) { // for names filter
				String q = "%" + s + "%";
				for (int ii = 0; ii < 3; ++ii)
					pstmt.setString(i++, q);

			}
			pstmt.setInt(i++, count > MAX_COUNT ? MAX_COUNT : count);
			pstmt.setInt(i++, from);
			rs = pstmt.executeQuery();

			ArrayList<Integer> ids = new ArrayList<Integer>(count);
			while (rs.next()) {
				ids.add(rs.getInt(1));
			}

			closeAll();

			InfoToFront infoToFront = new InfoToFront();
			infoToFront.setIDs(ids);
			return infoToFront;
		}
	}

}
