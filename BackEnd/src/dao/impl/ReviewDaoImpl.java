package dao.impl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import dao.ReviewDao;
import socket.InfoFromFront;
import socket.InfoToFront;

public class ReviewDaoImpl extends BaseDao implements ReviewDao {

	public ReviewDaoImpl() {
		super();
	}

	@Override
	public InfoToFront GetBookReviews(InfoFromFront infoFromFront) throws SQLException {
		int bookId, from, count;
		bookId = infoFromFront.getBookId();
		from = infoFromFront.getFrom();
		count = infoFromFront.getCount();

		List<Integer> reviewId = new LinkedList<Integer>();

		getConnection();

		String sql = "select id" + " from review" + " where book_id = ?" + " order by edit_time"
				+ " limit ? offset ?";
		pstmt = conn.prepareStatement(sql);
		pstmt.setInt(1, bookId);
		pstmt.setInt(2, count);
		pstmt.setInt(3, from);
		rs = pstmt.executeQuery();

		while (rs.next()) {
			reviewId.add(rs.getInt("id"));
		}

		InfoToFront info = new InfoToFront();
		info.setIDs(reviewId);
		return info;
	}

	@Override
	public InfoToFront GetReview(InfoFromFront infoFromFront) throws SQLException {
		int reviewId = infoFromFront.getReviewId();
		InfoToFront info = new InfoToFront();

		getConnection();

		String sql = "select u.name, r.rating, r.edit_time, r.title, r.content, b.name as book_name"
				+ " from review r" + " join book b on r.book_id = b.id" + " join user u on u.id = r.user_id"
				+ " where r.id = ?";

		pstmt = conn.prepareStatement(sql);
		pstmt.setInt(1, reviewId);
		rs = pstmt.executeQuery();

		while (rs.next()) {
			info.setCreateUser(rs.getString("name"));
			info.setRating(rs.getInt("rating"));
			info.setTimeStap(rs.getTimestamp("edit_time").getTime() / 1000); // milliseconds/1000 = seconds
			info.setTitle(rs.getString("title"));
			info.setContent(rs.getString("content"));
			info.setBookName(rs.getString("book_name"));
		}

		return info;
	}

	@Override
	public InfoToFront ChangeReview(InfoFromFront infoFromFront) throws SQLException {
		int userId = infoFromFront.getUserId();
		int reviewId = infoFromFront.getReviewId();
		boolean isDeleteAction = infoFromFront.getDeleteAction();
		String newTitle = infoFromFront.getNewTitle();
		String newContent = infoFromFront.getNewContent();
		int newRating = infoFromFront.getNewRating();

		InfoToFront info = new InfoToFront();

		String sql = "";
		getConnection();

		if (isDeleteAction) {
			sql += "DELETE FROM review WHERE user_id = ? AND id = ?;";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, userId);
			pstmt.setInt(2, reviewId);
		}
		else {
			sql += "UPDATE review " + "SET title = ?, content = ?, rating = ?" + " WHERE id = ? AND user_id = ?;";

			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, newTitle);
			pstmt.setString(2, newContent);
			pstmt.setInt(3, newRating);
			pstmt.setInt(4, reviewId);
			pstmt.setInt(5, userId);
		}
		int rows = pstmt.executeUpdate();

		if (rows == 1)
			info.setSuccess(true);
		else
			info.setSuccess(false);

		return info;
	}

	@Override
	public InfoToFront CreateReview(InfoFromFront infoFromFront) throws SQLException {
		int bookId, userId, rating;
		String title, content;
		bookId = infoFromFront.getBookId();
		userId = infoFromFront.getUserId();
		rating = infoFromFront.getRating();
		title = infoFromFront.getTitle();
		content = infoFromFront.getContent();

		InfoToFront info = new InfoToFront();

		String sql = "insert into review(user_id, book_id, title, content, rating) values(?,?,?,?,?);";

		getConnection();
		pstmt = conn.prepareStatement(sql);
		pstmt.setInt(1, userId);
		pstmt.setInt(2, bookId);
		pstmt.setString(3, title);
		pstmt.setString(4, content);
		pstmt.setInt(5, rating);

		int rows = pstmt.executeUpdate();
		info.setSuccess(rows == 1);

		closeAll();
		return info;
	}

	@Override
	public InfoToFront GetMyReviews(InfoFromFront infoFromFront) throws SQLException {
		int userId = infoFromFront.getUserId();
		InfoToFront info = new InfoToFront();

		if (userId <= 0) {
			info.setIDs(new ArrayList<Integer>());
			return info;
		}

		List<Integer> reviews = new LinkedList<Integer>();

		String sql = "select id from review where user_id = ?;";

		getConnection();
		pstmt = conn.prepareStatement(sql);
		pstmt.setInt(1, userId);

		rs = pstmt.executeQuery();
		while (rs.next()) {
			reviews.add(rs.getInt("id"));
		}

		info.setIDs(reviews);
		return info;
	}
}
