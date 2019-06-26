package entities;
import java.sql.Date;

public class Author {
    private int authorId;
    private String authorName;
    private int countyId;
    private Date born;
    private Date died;
    private boolean isTranslator;

    public Author(){

    }

    public Author(int authorId,String authorName, Date born){
        this.authorId = authorId;
        this.authorName = authorName;
        this.born = born;
    }

    public Author(int authorId, String authorName) {
        this.authorId = authorId;
        this.authorName = authorName;
    }

    public Author(int authorId, String authorName, int countyId, Date born, Date died, boolean isTranslator) {
        this.authorId = authorId;
        this.authorName = authorName;
        this.countyId = countyId;
        this.born = born;
        this.died = died;
        this.isTranslator = isTranslator;
    }



    public int getAuthorId() {
        return authorId;
    }

    public void setAuthorId(int authorId) {
        this.authorId = authorId;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public int getCountyId() {
        return countyId;
    }

    public void setCountyId(int countyId) {
        this.countyId = countyId;
    }

    public Date getBorn() {
        return born;
    }

    public void setBorn(Date born) {
        this.born = born;
    }

    public Date getDied() {
        return died;
    }

    public void setDied(Date died) {
        this.died = died;
    }

    public boolean isTranslator() {
        return isTranslator;
    }

    public void setTranslator(boolean translator) {
        isTranslator = translator;
    }
}
