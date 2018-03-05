package kr.ac.skuniv.oopsla.jobata.summarist;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Build;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TabHost;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


public class BNViewModel {
    private AppCompatActivity activity;
    private BNViewController bnViewController;
    private DrawerLayout drawerLayout;
    private String[] navItems = {"Home", "속보", "주간키워드"};
    private ListView nav_list, list_view;
    private TabHost tabHost;
    private TabHost.TabSpec ts1;
    private Button nav_btn, setting_btn;
    private TextView date_text, time_text;
    private Calendar calendar;
    private long getNow;
    private SimpleDateFormat dateFormat1, dateFormat2;
    private Thread t, bnT;
    private Intent i;
    private BNAdapter bnAdapter;
    private ArrayAdapter<String> arrayAdapter;
    private AlertDialog.Builder builder;
    private AlertDialog dialog;
    public static Boolean PUSH_STATE = true;
    public static String KEYWORD_STATE = "";

    public BNViewModel(final AppCompatActivity activity, BNViewController bnViewController) {
        this.activity = activity;
        this.bnViewController = bnViewController;
        drawerLayout = (DrawerLayout) activity.findViewById(R.id.bn_drawer);
        list_view = (ListView) activity.findViewById(R.id.list_view);
        nav_list = (ListView) activity.findViewById(R.id.nav_list);
        tabHost = (TabHost) activity.findViewById(R.id.tabhost);
        nav_btn = (Button) activity.findViewById(R.id.nav_btn);
        setting_btn = (Button) activity.findViewById(R.id.setting_btn);
        date_text = (TextView) activity.findViewById(R.id.date_text);
        time_text = (TextView) activity.findViewById(R.id.time_text);
        dateFormat1 = new SimpleDateFormat("yyyy-MM-dd E");
        dateFormat2 = new SimpleDateFormat("HH : mm : ss");
        arrayAdapter = new ArrayAdapter<String>(activity, android.R.layout.simple_list_item_1, navItems);
        builder = new AlertDialog.Builder(new ContextThemeWrapper(activity, R.style.LightThemeSelector));

        t = new Thread() {
            @Override
            public void run() {
                try {
                    while (!isInterrupted()) {
                        Thread.sleep(1000);
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                calendar = Calendar.getInstance(Locale.KOREA);
                                getNow = calendar.getTimeInMillis();
                                date_text.setText(dateFormat1.format(new Date(getNow)));
                                time_text.setText(dateFormat2.format(new Date(getNow)));
                            }
                        });
                    }
                } catch(InterruptedException e){
                }
            }
        };
        bnT = new Thread() {
            @Override
            public void run() {
                try {
                    while (!isInterrupted()) {
                        Thread.sleep(60000);
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                new JSONTaskBN(BNViewModel.this).execute("http://192.168.0.6:3000/bn");
                            }
                        });
                    }
                } catch(InterruptedException e){
                }
            }
        };
        bnAdapter = new BNAdapter(activity);
    }

    public View getDialogView(){
        View v = activity.getLayoutInflater().inflate(R.layout.alertdialog, null);
        final Switch pushSwitch = (Switch)v.findViewById(R.id.pushSwitch);
        final EditText keywordEdit = (EditText)v.findViewById(R.id.editText);
        Button okButton = (Button)v.findViewById(R.id.okButton);
        keywordEdit.getBackground().setColorFilter(Color.parseColor("#3498DB"), PorterDuff.Mode.SRC_ATOP);
        pushSwitch.setChecked(PUSH_STATE);
        keywordEdit.setText(KEYWORD_STATE);
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setPUSH_STATE(pushSwitch.isChecked());
                setKEYWORD_STATE(keywordEdit.getText().toString().trim());
                dialog.dismiss();
            }
        });
        return v;
    }
    public DrawerLayout getDrawerLayout() { return drawerLayout; }
    public String[] getNavItems() { return navItems; }
    public ListView getNav_list() { return nav_list; }
    public ListView getList_view() { return list_view; }
    public TabHost getTabHost() { return tabHost; }
    public TabHost.TabSpec getTs1() { return ts1; }
    public void setTs1(TabHost.TabSpec ts1) { this.ts1 = ts1; }
    public Button getNav_btn() { return nav_btn; }
    public Button getSetting_btn() { return setting_btn; }
    public TextView getDate_text() { return date_text; }
    public TextView getTime_text() { return time_text; }
    public Calendar getCalendar() { return calendar; }
    public void setCalendar(Calendar calendar) { this.calendar = calendar; }
    public long getGetNow() { return getNow; }
    public SimpleDateFormat getDateFormat1() { return dateFormat1; }
    public SimpleDateFormat getDateFormat2() { return dateFormat2; }
    public Thread getT() { return t; }
    public Thread getBnT() { return bnT; }
    public Intent getI() { return i; }
    public void setI(Intent i) { this.i = i; }
    public BNAdapter getBnAdapter() { return bnAdapter; }
    public ArrayAdapter<String> getArrayAdapter() { return arrayAdapter; }
    public AlertDialog.Builder getBuilder() { return builder; }
    public AlertDialog getDialog() { return dialog; }
    public void setDialog(AlertDialog dialog) { this.dialog = dialog; }
    public Boolean getPUSH_STATE() { return PUSH_STATE; }
    public void setPUSH_STATE(Boolean PUSH_STATE) { this.PUSH_STATE = PUSH_STATE; }
    public String getKEYWORD_STATE() { return KEYWORD_STATE; }
    public void setKEYWORD_STATE(String KEYWORD_STATE) { this.KEYWORD_STATE = KEYWORD_STATE; }
}
