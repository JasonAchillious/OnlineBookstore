package Dao.impl;

import Dao.ReviewDao;
import Socket.DataToFront;
import Socket.InfoFromFront;

import java.sql.SQLException;
import java.util.LinkedList;

public class ReviewDaoImpl extends BaseDao implements ReviewDao {

    /**
     * issue: The database has no review id. need to discuss.
     * Get the ReviewId from n to n+count order by recently edited time.
     * Finally edited in 2019.5.18. Jason Zhao.
     * param bookId
     * param from
     * param count
     * return Integer[]
     * @throws SQLException
     */
    @Override
    public DataToFront GetBookReviews(InfoFromFront infoFromFront) throws SQLException {
        int bookId, from, count;
        bookId = infoFromFront.getBookId();
        from = infoFromFront.getFrom();
        count = infoFromFront.getCount();
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
     * param reviewId
     * @return DataToFront
     * @throws SQLException
     */
    @Override
    public DataToFront GetReviews(InfoFromFront infoFromFront) throws SQLException {
        int reviewId = infoFromFront.getReviewId();
        DataToFront dataToFront = new DataToFront();
        dataToFront.setType("GetReview");

        getConnection();

        String sql = "select r.user_id, r.rating, r.edit_time, r.title, r.content, b.name as book_name"
                        +"from review r join book b on r.book_id = b.id"
                        + "where r.review_id = ?";

        pstmt = conn.prepareStatement(sql);
        pstmt.setInt(1, reviewId);
        rs = pstmt.executeQuery();


        while (rs.next()){
            dataToFront.setCreateUser(rs.getString("user_id"));
            dataToFront.setRating(rs.getInt("rating"));
            dataToFront.setTimeStap(rs.getTimestamp("edit_time").getTime());
            dataToFront.setTitle(rs.getString("title"));
            dataToFront.setContent(rs.getString("content"));
            dataToFront.setBookName(rs.getString("book_name"));
        }

        return dataToFront;
    }

    @Override
    public DataToFront ChangeReview(InfoFromFront infoFromFront) throws SQLException
    {
        int userId = infoFromFront.getUserId();
        int reviewId = infoFromFront.getReviewId();
        boolean isDeleteAction = infoFromFront.getDeleteAction();
        String newTitle = infoFromFront.getTitle();
        String newContent = infoFromFront.getNewContent();
        int newRating = infoFromFront.getRating();
        DataToFront dataToFront = new DataToFront();
        dataToFront.setType("ChangeReview");

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

        if (rows == 1) dataToFront.setSuccess(true);
        else dataToFront.setSuccess(false);

        return dataToFront;
    }

    @Override
    public DataToFront CreateReview(InfoFromFront infoFromFront) throws SQLException {
        int bookId, rating;
        String title, content;
        bookId = infoFromFront.getBookId();
        rating = infoFromFront.getRating();
        title = infoFromFront.getTitle();
        content = infoFromFront.getContent();
        return null;
    }

    @Override
    public DataToFront CheckBuyComplete(InfoFromFront infoFromFront) throws SQLException {
        int userId = infoFromFront.getUserId();
        int bookId = infoFromFront.getBookId();

        return null;
    }


}
