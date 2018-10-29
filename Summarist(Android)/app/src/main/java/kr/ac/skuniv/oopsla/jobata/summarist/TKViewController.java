package kr.ac.skuniv.oopsla.jobata.summarist;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import static android.app.AlertDialog.THEME_HOLO_LIGHT;

public class TKViewController {
    private AppCompatActivity activity;
    private TKViewModel tkViewModel;

    public TKViewController(final AppCompatActivity activity) {
        this.activity = activity;
        tkViewModel = new TKViewModel(activity, this);
        tkViewModel.getListView().setAdapter(new ArrayAdapter<String>(activity, android.R.layout.simple_list_item_1, tkViewModel.getNavItems()));
        tkViewModel.getListView().setOnItemClickListener(new ListView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        tkViewModel.setI(new Intent(activity, MainActivity.class));
                        activity.startActivity(tkViewModel.getI());
                        activity.finish();
                        break;
                    case 1:
                        tkViewModel.setI(new Intent(activity, BNActivity.class));
                        activity.startActivity(tkViewModel.getI());
                        activity.finish();
                        break;
                    case 2:
                        tkViewModel.setI(new Intent(activity, TKActivity.class));
                        activity.startActivity(tkViewModel.getI());
                        activity.finish();
                        break;
                }
            }
        });
        tkViewModel.getTabHost().setup();
        tkViewModel.setTs(tkViewModel.getTabHost().newTabSpec("TAB Spec 1"));
        tkViewModel.getTs().setContent(R.id.content1);
        tkViewModel.getTs().setIndicator("일간 키워드");
        tkViewModel.getTabHost().addTab(tkViewModel.getTs());
        tkViewModel.getTabHost().getTabWidget().setCurrentTab(0);
        tkViewModel.getTabHost().getTabWidget().getChildTabViewAt(0).setBackgroundColor(Color.parseColor("#3498DB"));
        tkViewModel.getNav_btn().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tkViewModel.getDrawerLayout().openDrawer(Gravity.START);
            }
        });
        tkViewModel.getDate_btn().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(activity, THEME_HOLO_LIGHT, dateSetListener, tkViewModel.getD_year(), tkViewModel.getD_month(), tkViewModel.getD_day()).show();
            }
        });
        tkViewModel.getDate_text().setText(tkViewModel.getDateFormat()[0].format(new Date(tkViewModel.getGetNow()[1])));
        tkViewModel.getLeft_btn().setOnClickListener(bListener);
        tkViewModel.getRight_btn().setOnClickListener(bListener);
        for(int i = 0; i < 5; i++) {
            tkViewModel.getTab()[i].setOnClickListener(bListener);
        }
        tkViewModel.getDate_text().setText(tkViewModel.getDateFormat()[0].format(new Date(tkViewModel.getGetNow()[1])));
        tkViewModel.setTab_state(0);
        tkViewModel.getTab()[0].setSelected(true);
        for(int i = 0; i < 10; i++)
            tkViewModel.getTv_rank()[i].setText("" + (i + 1));
        new JSONTaskTK(tkViewModel.getRank(), tkViewModel.getTab_state(), tkViewModel.getDateFormat()[1].format(new Date(tkViewModel.getGetNow()[1])), tkViewModel.getD_hour()).execute(tkViewModel.getIpAdress() + "/tk");
    }

    DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            Calendar calendar = Calendar.getInstance(Locale.KOREA);
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);
            calendar.set(year, month, dayOfMonth);
            long result = calendar.getTimeInMillis();
            if (result <= tkViewModel.getGetNow()[0]) {
                tkViewModel.setD_day(year);
                tkViewModel.setD_month(month);
                tkViewModel.setD_day(dayOfMonth);
                tkViewModel.setGetNow(1, result);
                tkViewModel.getDate_text().setText(tkViewModel.getDateFormat()[1].format(new Date(tkViewModel.getGetNow()[1])));
                new JSONTaskTK(tkViewModel.getRank(), tkViewModel.getTab_state(), tkViewModel.getDateFormat()[1].format(new Date(tkViewModel.getGetNow()[1])), 23).execute(tkViewModel.getIpAdress() + "/tk");
            } else
                Toast.makeText(activity, "Impossible", Toast.LENGTH_SHORT).show();
        }
    };

    Button.OnClickListener bListener = new Button.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.tk_left_btn:
                    tkViewModel.setD_day(tkViewModel.getD_day() - 1);
                    tkViewModel.setGetNow(1, tkViewModel.getGetNow()[1] - tkViewModel.getDay());
                    tkViewModel.getDate_text().setText(tkViewModel.getDateFormat()[1].format(new Date(tkViewModel.getGetNow()[1])));
                    new JSONTaskTK(tkViewModel.getRank(), tkViewModel.getTab_state(), tkViewModel.getDateFormat()[1].format(new Date(tkViewModel.getGetNow()[1])), 23).execute(tkViewModel.getIpAdress() + "/tk");
                    break;
                case R.id.tk_right_btn:
                    if(tkViewModel.getGetNow()[0] <= tkViewModel.getGetNow()[1]) {
                        Toast.makeText(activity, "Impossible", Toast.LENGTH_SHORT).show(); break;
                    }
                    else if(tkViewModel.getGetNow()[0] == tkViewModel.getGetNow()[1]) {
                        tkViewModel.setD_day(tkViewModel.getD_day() + 1);
                        tkViewModel.setGetNow(1, tkViewModel.getGetNow()[1] + tkViewModel.getDay());
                        tkViewModel.getDate_text().setText(tkViewModel.getDateFormat()[0].format(new Date(tkViewModel.getGetNow()[1])));
                        new JSONTaskTK(tkViewModel.getRank(), tkViewModel.getTab_state(), tkViewModel.getDateFormat()[1].format(new Date(tkViewModel.getGetNow()[1])), tkViewModel.getD_hour()).execute(tkViewModel.getIpAdress() + "/tk");
                        break;
                    }
                    else {
                        tkViewModel.setD_day(tkViewModel.getD_day() + 1);
                        tkViewModel.setGetNow(1, tkViewModel.getGetNow()[1] + tkViewModel.getDay());
                        tkViewModel.getDate_text().setText(tkViewModel.getDateFormat()[1].format(new Date(tkViewModel.getGetNow()[1])));
                        new JSONTaskTK(tkViewModel.getRank(), tkViewModel.getTab_state(), tkViewModel.getDateFormat()[1].format(new Date(tkViewModel.getGetNow()[1])), 23).execute(tkViewModel.getIpAdress() + "/tk");
                        break;
                    }
                case R.id.tk_tab_0:
                    tkViewModel.setTab_state(0);
                    for(int i = 0; i < 5; i ++) {
                        if(i != tkViewModel.getTab_state())
                            tkViewModel.getTab()[i].setSelected(false);
                        else
                            tkViewModel.getTab()[i].setSelected(true);
                    }
                    for(int i = 0; i < 10; i++)
                        tkViewModel.getTv_rank()[i].setText("" + (i + 1));
                    if(tkViewModel.getGetNow()[0] == tkViewModel.getGetNow()[1])
                        new JSONTaskTK(tkViewModel.getRank(), tkViewModel.getTab_state(), tkViewModel.getDateFormat()[1].format(new Date(tkViewModel.getGetNow()[1])), tkViewModel.getD_hour()).execute(tkViewModel.getIpAdress() + "/tk");
                    else
                        new JSONTaskTK(tkViewModel.getRank(), tkViewModel.getTab_state(), tkViewModel.getDateFormat()[1].format(new Date(tkViewModel.getGetNow()[1])), 23).execute(tkViewModel.getIpAdress() + "/tk");
                    break;
                case R.id.tk_tab_1:
                    tkViewModel.setTab_state(1);
                    for(int i = 0; i < 5; i ++) {
                        if(i != tkViewModel.getTab_state())
                            tkViewModel.getTab()[i].setSelected(false);
                        else
                            tkViewModel.getTab()[i].setSelected(true);
                    }
                    for(int i = 0; i < 10; i++)
                        tkViewModel.getTv_rank()[i].setText("" + (i + 11));
                    if(tkViewModel.getGetNow()[0] == tkViewModel.getGetNow()[1])
                        new JSONTaskTK(tkViewModel.getRank(), tkViewModel.getTab_state(), tkViewModel.getDateFormat()[1].format(new Date(tkViewModel.getGetNow()[1])), tkViewModel.getD_hour()).execute(tkViewModel.getIpAdress() + "/tk");
                    else
                        new JSONTaskTK(tkViewModel.getRank(), tkViewModel.getTab_state(), tkViewModel.getDateFormat()[1].format(new Date(tkViewModel.getGetNow()[1])), 23).execute(tkViewModel.getIpAdress() + "/tk");
                    break;
                case R.id.tk_tab_2:
                    tkViewModel.setTab_state(2);
                    for(int i = 0; i < 5; i ++) {
                        if(i != tkViewModel.getTab_state())
                            tkViewModel.getTab()[i].setSelected(false);
                        else
                            tkViewModel.getTab()[i].setSelected(true);
                    }
                    for(int i = 0; i < 10; i++)
                        tkViewModel.getTv_rank()[i].setText("" + (i + 21));
                    if(tkViewModel.getGetNow()[0] == tkViewModel.getGetNow()[1])
                        new JSONTaskTK(tkViewModel.getRank(), tkViewModel.getTab_state(), tkViewModel.getDateFormat()[1].format(new Date(tkViewModel.getGetNow()[1])), tkViewModel.getD_hour()).execute(tkViewModel.getIpAdress() + "/tk");
                    else
                        new JSONTaskTK(tkViewModel.getRank(), tkViewModel.getTab_state(), tkViewModel.getDateFormat()[1].format(new Date(tkViewModel.getGetNow()[1])), 23).execute(tkViewModel.getIpAdress() + "/tk");
                    break;
                case R.id.tk_tab_3:
                    tkViewModel.setTab_state(3);
                    for(int i = 0; i < 5; i ++) {
                        if(i != tkViewModel.getTab_state())
                            tkViewModel.getTab()[i].setSelected(false);
                        else
                            tkViewModel.getTab()[i].setSelected(true);
                    }
                    for(int i = 0; i < 10; i++)
                        tkViewModel.getTv_rank()[i].setText("" + (i + 31));
                    if(tkViewModel.getGetNow()[0] == tkViewModel.getGetNow()[1])
                        new JSONTaskTK(tkViewModel.getRank(), tkViewModel.getTab_state(), tkViewModel.getDateFormat()[1].format(new Date(tkViewModel.getGetNow()[1])), tkViewModel.getD_hour()).execute(tkViewModel.getIpAdress() + "/tk");
                    else
                        new JSONTaskTK(tkViewModel.getRank(), tkViewModel.getTab_state(), tkViewModel.getDateFormat()[1].format(new Date(tkViewModel.getGetNow()[1])), 23).execute(tkViewModel.getIpAdress() + "/tk");
                    break;
                case R.id.tk_tab_4:
                    tkViewModel.setTab_state(4);
                    for(int i = 0; i < 5; i ++) {
                        if(i != tkViewModel.getTab_state())
                            tkViewModel.getTab()[i].setSelected(false);
                        else
                            tkViewModel.getTab()[i].setSelected(true);
                    }
                    for(int i = 0; i < 10; i++)
                        tkViewModel.getTv_rank()[i].setText("" + (i + 41));
                    if(tkViewModel.getGetNow()[0] == tkViewModel.getGetNow()[1])
                        new JSONTaskTK(tkViewModel.getRank(), tkViewModel.getTab_state(), tkViewModel.getDateFormat()[1].format(new Date(tkViewModel.getGetNow()[1])), tkViewModel.getD_hour()).execute(tkViewModel.getIpAdress() + "/tk");
                    else
                        new JSONTaskTK(tkViewModel.getRank(), tkViewModel.getTab_state(), tkViewModel.getDateFormat()[1].format(new Date(tkViewModel.getGetNow()[1])), 23).execute(tkViewModel.getIpAdress() + "/tk");
                    break;
            }
        }
    };
}