package entities;
import java.sql.Date;


public class Transaction {
    private int id;
    private int user_id;
    private int book_id;
    private Date deal_time;
    private double deal_price;
    private boolean paied;
    private Date paied_time;

    public Transaction(int id, int user_id, int book_id, Date deal_time, double deal_price, boolean paied) {
        this.id = id;
        this.user_id = user_id;
        this.book_id = book_id;
        this.deal_time = deal_time;
        this.deal_price = deal_price;
        this.paied = paied;
    }

    public Transaction(int id, int user_id, int book_id, Date deal_time, double deal_price, boolean paied, Date paied_time) {
        this.id = id;
        this.user_id = user_id;
        this.book_id = book_id;
        this.deal_time = deal_time;
        this.deal_price = deal_price;
        this.paied = paied;
        this.paied_time = paied_time;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getBook_id() {
        return book_id;
    }

    public void setBook_id(int book_id) {
        this.book_id = book_id;
    }

    public Date getDeal_time() {
        return deal_time;
    }

    public void setDeal_time(Date deal_time) {
        this.deal_time = deal_time;
    }

    public double getDeal_price() {
        return deal_price;
    }

    public void setDeal_price(double deal_price) {
        this.deal_price = deal_price;
    }

    public boolean isPaied() {
        return paied;
    }

    public void setPaied(boolean paied) {
        this.paied = paied;
    }

    public Date getPaied_time() {
        return paied_time;
    }

    public void setPaied_time(Date paied_time) {
        this.paied_time = paied_time;
    }
}
