package entities;
import java.sql.Date;


public class Press {
    private int id;
    private String name;
    private String website;
    private int countryId;
    private String address;
    private Date establish_time;

    public Press(){

    }

    public Press(int id, String name, String website, int countryId, String address) {
        this.id = id;
        this.name = name;
        this.website = website;
        this.countryId = countryId;
        this.address = address;
    }

    public Press(int id, String name, String website, int countryId, String address, Date establish_time) {
        this.id = id;
        this.name = name;
        this.website = website;
        this.countryId = countryId;
        this.address = address;
        this.establish_time = establish_time;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public int getCountryId() {
        return countryId;
    }

    public void setCountryId(int countryId) {
        this.countryId = countryId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Date getEstablish_time() {
        return establish_time;
    }

    public void setEstablish_time(Date establish_time) {
        this.establish_time = establish_time;
    }
}
