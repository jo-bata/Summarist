package kr.ac.skuniv.oopsla.jobata.summarist;

public class WKVO {
    private String date, keyword, title, url;
    private int rank, num;

    public WKVO(String date, String keyword, String title, String url, int rank, int num) {
        this.date = date;
        this.keyword = keyword;
        this.title = title;
        this.url = url;
        this.rank = rank;
        this.num = num;
    }

    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }
    public String getKeyword() { return keyword; }
    public void setKeyword(String keyword) { this.keyword = keyword; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getUrl() { return url; }
    public void setUrl(String url) { this.url = url; }
    public int getRank() { return rank; }
    public void setRank(int rank) { this.rank = rank; }
    public int getNum() { return num; }
    public void setNum(int num) { this.num = num; }
}
