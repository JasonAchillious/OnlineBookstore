package Dao;

public interface ReviewDao {
    // get reviews' ID
    int[] getBookReviews(int bookId, int from, int count, boolean newFirst);


}
