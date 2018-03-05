package kr.ac.skuniv.oopsla.jobata.summarist;

public class SummaristVO {
    private int type, hour, rank;
    private String date, url, title;

    public SummaristVO(int type, int hour, int rank, String date, String url, String title) {
        this.type = type;
        this.hour = hour;
        this.rank = rank;
        this.date = date;
        this.url = url;
        this.title = title;
    }

    public int getType() {
        return type;
    }
    public void setType(int type) {
        this.type = type;
    }
    public int getHour() {
        return hour;
    }
    public void setHour(int hour) {
        this.hour = hour;
    }
    public int getRank() {
        return rank;
    }
    public void setRank(int rank) { this.rank = rank; }
    public String getDate() { return date; }
    public void setDate(String date) {
        this.date = date;
    }
    public String getUrl() {
        return url;
    }
    public void setUrl(String url) {
        this.url = url;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
}
