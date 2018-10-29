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

public class ViewModel {
    private AppCompatActivity activity;
    private ViewController viewController;
    private DrawerLayout drawerLayout;
    private String[] navItems = {"Home", "속보", "일간키워드"};
    private ListView listView;
    private TabHost tabHost;
    private TabHost.TabSpec ts1, ts2;
    private Button nav_btn, left_btn[] = new Button[2], right_btn[] = new Button[2], date_btn[] = new Button[2], time_btn[] = new Button[2], tab_0[] = new Button[9], tab_1[] = new Button[11];
    private TextView date_text[] = new TextView[2];
    private TextView rank_0[] = new TextView[10];
    private TextView[] rank_1 = new TextView[10];
    private TextView[] tv_rank = new TextView[10];
    private int[] ids_rank_1 = new int[]{R.id.rank_0_1, R.id.rank_0_2, R.id.rank_0_3, R.id.rank_0_4, R.id.rank_0_5, R.id.rank_0_6, R.id.rank_0_7, R.id.rank_0_8, R.id.rank_0_9, R.id.rank_0_10};
    private int[] ids_rank_2 = new int[]{R.id.rank_1_1, R.id.rank_1_2, R.id.rank_1_3, R.id.rank_1_4, R.id.rank_1_5, R.id.rank_1_6, R.id.rank_1_7, R.id.rank_1_8, R.id.rank_1_9, R.id.rank_1_10};
    private int[] ids_tab_0 = new int[]{R.id.tab_0_1, R.id.tab_0_2, R.id.tab_0_3, R.id.tab_0_4, R.id.tab_0_5, R.id.tab_0_6};
    private int[] ids_tab_1 = new int[]{R.id.tab_1_1, R.id.tab_1_2};
    private int[] ids_tv_rank = new int[]{R.id.tv_rank_1, R.id.tv_rank_2, R.id.tv_rank_3, R.id.tv_rank_4, R.id.tv_rank_5, R.id.tv_rank_6, R.id.tv_rank_7, R.id.tv_rank_8, R.id.tv_rank_9, R.id.tv_rank_10};
    private Calendar calendar = Calendar.getInstance(Locale.KOREA);
    private long getNow[] = new long[3], day = 1000 * 60 * 60 * 24;
    private SimpleDateFormat dateFormat[] = new SimpleDateFormat[2];
    private int tab_state[] = new int[3], d_year = calendar.get(Calendar.YEAR), d_month = calendar.get(Calendar.MONTH), d_day = calendar.get(Calendar.DAY_OF_MONTH), d_hour = calendar.get(Calendar.HOUR_OF_DAY);
    private Intent i;
    private String ipAdress = "http://192.168.10.129:3000";

    public ViewModel(AppCompatActivity activity, ViewController viewController) {
        this.activity = activity;
        this.viewController = viewController;
        drawerLayout = (DrawerLayout) activity.findViewById(R.id.main_drawer);
        listView = (ListView) activity.findViewById(R.id.nav_list);
        tabHost = (TabHost) activity.findViewById(R.id.tabhost);
        date_btn[0] = (Button) activity.findViewById(R.id.date_btn_1);
        date_btn[1] = (Button) activity.findViewById(R.id.date_btn_2);
        time_btn[0] = (Button) activity.findViewById(R.id.time_btn_1);
        time_btn[1] = (Button) activity.findViewById(R.id.time_btn_2);
        nav_btn = (Button) activity.findViewById(R.id.nav_btn);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        for (int i = 0; i < 3; i++)
            getNow[i] = calendar.getTimeInMillis();
        dateFormat[0] = new SimpleDateFormat("yyyy-MM-dd HH", Locale.KOREA);
        dateFormat[1] = new SimpleDateFormat("yyyy-MM-dd", Locale.KOREA);
        left_btn[0] = (Button) activity.findViewById(R.id.left_btn_1);
        right_btn[0] = (Button) activity.findViewById(R.id.right_btn_1);
        date_text[0] = (TextView) activity.findViewById(R.id.date_text_1);
        left_btn[1] = (Button) activity.findViewById(R.id.left_btn_2);
        right_btn[1] = (Button) activity.findViewById(R.id.right_btn_2);
        date_text[1] = (TextView) activity.findViewById(R.id.date_text_2);
        for (int i = 0; i < 6; i++)
            tab_0[i] = (Button) activity.findViewById(ids_tab_0[i]);
        for (int i = 0; i < 2; i++)
            tab_1[i] = (Button) activity.findViewById(ids_tab_1[i]);
        for (int i = 0; i < 10; i++) {
            rank_0[i] = (TextView) activity.findViewById(ids_rank_1[i]);
            rank_1[i] = (TextView) activity.findViewById(ids_rank_2[i]);
        }
        for (int i = 0; i < 10; i++)
            tv_rank[i] = (TextView) activity.findViewById(ids_tv_rank[i]);
    }
    public DrawerLayout getDrawerLayout() { return drawerLayout; }
    public String[] getNavItems() { return navItems; }
    public ListView getListView() { return listView; }
    public TabHost getTabHost() { return tabHost; }
    public TabHost.TabSpec getTs1() { return ts1; }
    public void setTs1(TabHost.TabSpec ts1) { this.ts1 = ts1; }
    public TabHost.TabSpec getTs2() { return ts2; }
    public void setTs2(TabHost.TabSpec ts2) { this.ts2 = ts2; }
    public Button getNav_btn() { return nav_btn; }
    public Button[] getLeft_btn() { return left_btn; }
    public Button[] getRight_btn() { return right_btn; }
    public Button[] getDate_btn() { return date_btn; }
    public Button[] getTime_btn() { return time_btn; }
    public Button[] getTab_0() { return tab_0; }
    public Button[] getTab_1() { return tab_1; }
    public TextView[] getDate_text() { return date_text; }
    public TextView[] getRank_0() { return rank_0; }
    public TextView[] getRank_1() { return rank_1; }
    public TextView[] getTv_rank() { return tv_rank; }
    public void setTv_rank(TextView textView, int index) { this.tv_rank[index] = textView; }
    public int[] getIds_rank_1() { return ids_rank_1; }
    public int[] getIds_rank_2() { return ids_rank_2; }
    public int[] getIds_tab_0() { return ids_tab_0; }
    public int[] getIds_tab_1() { return ids_tab_1; }
    public int[] getIds_tv_rank() { return ids_tv_rank; }
    public Calendar getCalendar() { return calendar; }
    public long[] getGetNow() { return getNow; }
    public void setGetNow(int index, long getNow) { this.getNow[index] = getNow; }
    public long getDay() { return day; }
    public SimpleDateFormat[] getDateFormat() { return dateFormat; }
    public int[] getTab_state() { return tab_state; }
    public void setTab_state(int index, int tab_state) { this.tab_state[index] = tab_state; }
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
