package entities;

public class Country {
    private int id;
    private char[] country_code = new char[2];
    private String full_en;
    private String full_cn;

    public Country(int id, char[] country_code, String full_en, String full_cn) {
        this.id = id;
        this.country_code = country_code;
        this.full_en = full_en;
        this.full_cn = full_cn;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public char[] getCountry_code() {
        return country_code;
    }

    public void setCountry_code(char[] country_code) {
        this.country_code = country_code;
    }

    public String getFull_en() {
        return full_en;
    }

    public void setFull_en(String full_en) {
        this.full_en = full_en;
    }

    public String getFull_cn() {
        return full_cn;
    }

    public void setFull_cn(String full_cn) {
        this.full_cn = full_cn;
    }
}
