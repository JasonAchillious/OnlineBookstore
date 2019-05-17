package entities;

public class Book {
    private int bookId;
    private String bookName;
    private int authorId;
    private int publisherId;
    private int labelId;
    private String ISBN;
    private int pages;
    private double price;


    public int getBookId() {
        return bookId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public int getAuthorId() {
        return authorId;
    }

    public void setAuthorId(int authorId) {
        this.authorId = authorId;
    }

    public int getPublisherId() {
        return publisherId;
    }

    public void setPublisherId(int publisherId) {
        this.publisherId = publisherId;
    }

    public int getLabelId() {
        return labelId;
    }

    public void setLabelId(int labelId) {
        this.labelId = labelId;
    }

    public String getISBN() {
        return ISBN;
    }

    public void setISBN(String ISBN) {
        this.ISBN = ISBN;
    }

    public int getPages() {
        return pages;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Book(int bookId, String bookName, int authorId, int publisherId, int labelId, String ISBN, int pages, double price) {
        this.bookId = bookId;
        this.bookName = bookName;
        this.authorId = authorId;
        this.publisherId = publisherId;
        this.labelId = labelId;
        this.ISBN = ISBN;
        this.pages = pages;
        this.price = price;
    }

    public Book(int bookId, String bookName){
        this.bookId = bookId;
        this.bookName = bookName;

    }
}
