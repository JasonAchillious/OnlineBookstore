package Dao.impl;

import Dao.ReviewDao;
import Socket.InfoFromFront;
import Socket.InfoToFront;

import java.sql.Date;
import java.sql.SQLException;
import java.util.LinkedList;

public class ReviewDaoImpl extends BaseDao implements ReviewDao {

    /**
     * issue: The database has no review id. need to discuss.
     * Get the ReviewId from n to n+count order by recently edited time.
     * Finally edited in 2019.5.18. Jason Zhao.
     * @param bookId
     * @param from
     * @param count
     * @return Integer[]
     * @throws SQLException
     */
    @Override
    public InfoToFront GetBookReviews(int bookId, int from, int count) throws SQLException {
        LinkedList<Integer> reviewId = new LinkedList<>();
        getConnection();

        String sql = " select review_id from review order by edit_time" +
                "where book_id = ?";
        pstmt = conn.prepareStatement(sql);
        pstmt.setInt(1, bookId);
        rs = pstmt.executeQuery();

        int i = 0, j = 0;
        while(rs.next() && i < from){
            i++;
        }
        while (rs.next() && j < count){
            reviewId.add(rs.getInt("review_id"));
        }

        Integer[] reId = reviewId.toArray(new Integer[reviewId.size()]);

        return null;
    }

    /**
     * Get the details of a specific review.
     * Finally edited in 2019.5.18. Jason Zhao.
     * @param reviewId
     * @return InfoToFront
     * @throws SQLException
     */
    @Override
    public InfoToFront GetReviews(int reviewId) throws SQLException {
        InfoToFront infoToFront = new InfoToFront();
        infoToFront.setType("GetReview");

        getConnection();

        String sql = "select r.user_id, r.rating, r.edit_time, r.title, r.content, b.name as book_name"
                        +"from review r join book b on r.book_id = b.id"
                        + "where r.review_id = ?";

        pstmt = conn.prepareStatement(sql);
        pstmt.setInt(1, reviewId);
        rs = pstmt.executeQuery();


        while (rs.next()){
            infoToFront.setCreateUser(rs.getString("user_id"));
            infoToFront.setRating(rs.getInt("rating"));
            infoToFront.setTimeStap(rs.getTimestamp("edit_time").getTime());
            infoToFront.setTitle(rs.getString("title"));
            infoToFront.setContent(rs.getString("content"));
            infoToFront.setBookName(rs.getString("book_name"));
        }

        return infoToFront;
    }

    @Override
    public InfoToFront ChangeReview(int userId, int reviewId, boolean isDeleteAction, String newTitle, String newContent, int newRating) throws SQLException
    {
        InfoToFront infoToFront = new InfoToFront();
        infoToFront.setType("ChangeReview");

        String sql = null;
        if(isDeleteAction)
        {
            sql = "DELETE FROM review WHERE user_id = ? AND review_id = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, userId);
            pstmt.setInt(2,reviewId);
        }
        else
        {
            sql = "set sql_safe_updates = 1" +
                    "UPDATE review " +
                    "SET title = ? , content = ? , rating = ?" +
                    "WHERE review_id = ? AND user_id = ?";

            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, newTitle);
            pstmt.setString(2, newContent);
            pstmt.setInt(3, newRating);

        }
        int rows = pstmt.executeUpdate();

        if (rows == 1) infoToFront.setSuccess(true);
        else infoToFront.setSuccess(false);

        return infoToFront;
    }

    @Override
    public InfoToFront CreateReview(InfoFromFront infoFromFront) throws SQLException {
        int bookId, rating;
        String title, content;
        bookId = infoFromFront.getBookId();
        rating = infoFromFront.getRating();
        title = infoFromFront.getTitle();
        content = infoFromFront.getContent();
        return null;
    }

    @Override
    public InfoToFront CheckBuyComplete(int userId, int bookId) throws SQLException {
        return null;
    }

    @Override
    public InfoToFront CancelTransaction(int userId, int bookId) throws SQLException {
        return null;
    }
}
