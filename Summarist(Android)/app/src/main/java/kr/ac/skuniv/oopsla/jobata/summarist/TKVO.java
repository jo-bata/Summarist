package kr.ac.skuniv.oopsla.jobata.summarist;

public class TKVO {
    private String date, keyword;
    private int hour, rank, count;

    public TKVO(String date, int hour, int rank, String keyword, int count) {
        this.date = date;
        this.hour = hour;
        this.rank = rank;
        this.keyword = keyword;
        this.count = count;
    }

    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }
    public int getHour() { return hour; }
    public void setHour(int hour) { this.hour = hour; }
    public String getKeyword() { return keyword; }
    public void setKeyword(String keyword) { this.keyword = keyword; }
    public int getRank() { return rank; }
    public void setRank(int rank) { this.rank = rank; }
    public int getCount() { return count; }
    public void setCount(int count) { this.count = count; }
}
