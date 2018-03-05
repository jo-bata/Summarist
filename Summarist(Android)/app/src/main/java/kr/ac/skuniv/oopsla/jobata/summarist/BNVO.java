package kr.ac.skuniv.oopsla.jobata.summarist;

public class BNVO {
    private String date, time, title;

    public BNVO(String date, String time, String title) {
        this.date = date;
        this.time = time;
        this.title = title;
    }

    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }
    public String getTime() { return time; }
    public void setTime(String time) { this.time = time; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
}
