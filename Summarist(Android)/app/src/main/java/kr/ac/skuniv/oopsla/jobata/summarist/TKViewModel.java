package kr.ac.skuniv.oopsla.jobata.summarist;

import android.content.Intent;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class TKViewModel {
    private AppCompatActivity activity;
    private TKViewController tkViewController;
    private DrawerLayout drawerLayout;
    private String[] navItems = {"Home", "속보", "일간키워드"};
    private ListView listView;
    private TabHost tabHost;
    private TabHost.TabSpec ts;
    private Button nav_btn, left_btn, right_btn, date_btn, tab[] = new Button[5];
    private TextView date_text;
    private TextView[] rank = new TextView[10];
    private TextView[] tv_rank = new TextView[10];
    private int[] ids_rank_1 = new int[]{R.id.tk_rank_1, R.id.tk_rank_2, R.id.tk_rank_3, R.id.tk_rank_4, R.id.tk_rank_5, R.id.tk_rank_6, R.id.tk_rank_7, R.id.tk_rank_8, R.id.tk_rank_9, R.id.tk_rank_10};
    private int[] ids_tab = new int[]{R.id.tk_tab_0, R.id.tk_tab_1, R.id.tk_tab_2, R.id.tk_tab_3, R.id.tk_tab_4};
    private int[] ids_tv_rank = new int[]{R.id.tk_tv_rank_1, R.id.tk_tv_rank_2, R.id.tk_tv_rank_3, R.id.tk_tv_rank_4, R.id.tk_tv_rank_5, R.id.tk_tv_rank_6, R.id.tk_tv_rank_7, R.id.tk_tv_rank_8, R.id.tk_tv_rank_9, R.id.tk_tv_rank_10};
    private Calendar calendar = Calendar.getInstance(Locale.KOREA);
    private long getNow[] = new long[3], day = 1000 * 60 * 60 * 24;
    private SimpleDateFormat dateFormat[] = new SimpleDateFormat[2];
    private int tab_state, d_year = calendar.get(Calendar.YEAR), d_month = calendar.get(Calendar.MONTH), d_day = calendar.get(Calendar.DAY_OF_MONTH), d_hour = calendar.get(Calendar.HOUR_OF_DAY);
    private Intent i;
    private String ipAdress = "http://192.168.10.129:3000";

    public TKViewModel(AppCompatActivity activity, TKViewController tkViewController) {
        this.activity = activity;
        this.tkViewController = tkViewController;
        drawerLayout = (DrawerLayout) activity.findViewById(R.id.tk_drawer);
        listView = (ListView) activity.findViewById(R.id.nav_list);
        tabHost = (TabHost) activity.findViewById(R.id.tabhost);
        date_btn = (Button) activity.findViewById(R.id.tk_date_btn);
        nav_btn = (Button) activity.findViewById(R.id.nav_btn);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        for (int i = 0; i < 3; i++)
            getNow[i] = calendar.getTimeInMillis();
        dateFormat[0] = new SimpleDateFormat("yyyy-MM-dd HH", Locale.KOREA);
        dateFormat[1] = new SimpleDateFormat("yyyy-MM-dd", Locale.KOREA);
        left_btn = (Button) activity.findViewById(R.id.tk_left_btn);
        right_btn = (Button) activity.findViewById(R.id.tk_right_btn);
        date_text = (TextView) activity.findViewById(R.id.tk_date_text);
        for (int i = 0; i < 5; i++)
            tab[i] = (Button) activity.findViewById(ids_tab[i]);
        for (int i = 0; i < 10; i++)
            rank[i] = (TextView) activity.findViewById(ids_rank_1[i]);
        for (int i = 0; i < 10; i++)
            tv_rank[i] = (TextView) activity.findViewById(ids_tv_rank[i]);
    }
    public DrawerLayout getDrawerLayout() { return drawerLayout; }
    public String[] getNavItems() { return navItems; }
    public ListView getListView() { return listView; }
    public TabHost getTabHost() { return tabHost; }
    public TabHost.TabSpec getTs() { return ts; }
    public void setTs(TabHost.TabSpec ts) { this.ts = ts; }
    public Button getNav_btn() { return nav_btn; }
    public Button getLeft_btn() { return left_btn; }
    public Button getRight_btn() { return right_btn; }
    public Button getDate_btn() { return date_btn; }
    public Button[] getTab() { return tab; }
    public TextView getDate_text() { return date_text; }
    public TextView[] getRank() { return rank; }
    public TextView[] getTv_rank() { return tv_rank; }
    public void setTv_rank(TextView textView, int index) { this.tv_rank[index] = textView; }
    public int[] getIds_rank_1() { return ids_rank_1; }
    public int[] getIds_tab() { return ids_tab; }
    public int[] getIds_tv_rank() { return ids_tv_rank; }
    public Calendar getCalendar() { return calendar; }
    public long[] getGetNow() { return getNow; }
    public void setGetNow(int index, long getNow) { this.getNow[index] = getNow; }
    public long getDay() { return day; }
    public SimpleDateFormat[] getDateFormat() { return dateFormat; }
    public int getTab_state() { return tab_state; }
    public void setTab_state(int tab_state) { this.tab_state = tab_state; }
    public int getD_year() { return d_year; }
    public void setD_year(int d_year) { this.d_year = d_year; }
    public int getD_month() { return d_month; }
    public void setD_month(int d_month) { this.d_month = d_month; }
    public int getD_day() { return d_day; }
    public void setD_day(int d_day) { this.d_day = d_day; }
    public int getD_hour() { return d_hour; }
    public void setD_hour(int d_hour) { this.d_hour = d_hour; }
    public Intent getI() { return i; }
    public void setI(Intent i) { this.i = i; }
    public String getIpAdress() { return ipAdress; }
}
