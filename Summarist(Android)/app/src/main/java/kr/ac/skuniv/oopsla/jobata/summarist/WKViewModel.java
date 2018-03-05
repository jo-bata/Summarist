package kr.ac.skuniv.oopsla.jobata.summarist;

import android.content.Intent;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class WKViewModel {
    private AppCompatActivity activity;
    private WKViewController wkViewController;
    private DrawerLayout drawerLayout;
    private String[] navItems = {"Home", "속보", "주간키워드"};
    private ListView nav_list, list_view;
    private TabHost tabHost;
    private TabHost.TabSpec ts1;
    private Button nav_btn;
    private Button[] tab_rank = new Button[20];
    private int tab_state = 0;
    private int[] ids_tab_rank = new int[]{R.id.tab_rank_1, R.id.tab_rank_2, R.id.tab_rank_3, R.id.tab_rank_4, R.id.tab_rank_5, R.id.tab_rank_6, R.id.tab_rank_7, R.id.tab_rank_8, R.id.tab_rank_9, R.id.tab_rank_10,
                                                R.id.tab_rank_11, R.id.tab_rank_12, R.id.tab_rank_13, R.id.tab_rank_14, R.id.tab_rank_15, R.id.tab_rank_16, R.id.tab_rank_17, R.id.tab_rank_18, R.id.tab_rank_19, R.id.tab_rank_20};
    private TextView date_text;
    private Calendar calendar;
    private long getNow[] = new long[2], day = 1000 * 60 * 60 * 24;
    private SimpleDateFormat dateFormat, dateFormat2;
    private Intent i;
    private WKAdapter wkAdapter;
    private ArrayAdapter<String> arrayAdapter;

    public WKViewModel(AppCompatActivity activity, WKViewController wkViewController) {
        this.activity = activity;
        this.wkViewController = wkViewController;
        drawerLayout = (DrawerLayout) activity.findViewById(R.id.wk_drawer);
        list_view = (ListView) activity.findViewById(R.id.list_view);
        nav_list = (ListView) activity.findViewById(R.id.nav_list);
        tabHost = (TabHost) activity.findViewById(R.id.tabhost);
        nav_btn = (Button) activity.findViewById(R.id.nav_btn);
        for(int i = 0; i < 20; i ++)
            tab_rank[i] = (Button)activity.findViewById(ids_tab_rank[i]);
        date_text = (TextView) activity.findViewById(R.id.date_text);
        dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat2 = new SimpleDateFormat("MM-dd");
        arrayAdapter = new ArrayAdapter<String>(activity, android.R.layout.simple_list_item_1, navItems);
        wkAdapter = new WKAdapter(activity);
    }

    public DrawerLayout getDrawerLayout() { return drawerLayout; }
    public String[] getNavItems() { return navItems; }
    public ListView getNav_list() { return nav_list; }
    public ListView getList_view() { return list_view; }
    public TabHost getTabHost() { return tabHost; }
    public TabHost.TabSpec getTs1() { return ts1; }
    public void setTs1(TabHost.TabSpec ts1) { this.ts1 = ts1; }
    public Button getNav_btn() { return nav_btn; }
    public Button[] getTab_rank() { return tab_rank; }
    public int[] getIds_tab_rank() { return ids_tab_rank; }
    public int getTab_state() { return tab_state; }
    public void setTab_state(int tab_state) { this.tab_state = tab_state; }
    public TextView getDate_text() { return date_text; }
    public Calendar getCalendar() { return calendar; }
    public void setCalendar(Calendar calendar) { this.calendar = calendar; }
    public long[] getGetNow() { return getNow; }
    public void setGetNow(int index, long getNow) { this.getNow[index] = getNow; }
    public long getDay() { return day; }
    public SimpleDateFormat getDateFormat() { return dateFormat; }
    public SimpleDateFormat getDateFormat2() { return dateFormat2; }
    public Intent getI() { return i; }
    public void setI(Intent i) { this.i = i; }
    public WKAdapter getWkAdapter() { return wkAdapter; }
    public ArrayAdapter<String> getArrayAdapter() { return arrayAdapter; }
}
