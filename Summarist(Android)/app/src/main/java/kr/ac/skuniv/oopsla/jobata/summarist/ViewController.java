package kr.ac.skuniv.oopsla.jobata.summarist;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import static android.app.AlertDialog.THEME_HOLO_LIGHT;

public class ViewController {
    private AppCompatActivity activity;
    private ViewModel viewModel;

    public ViewController(final AppCompatActivity activity) {
        this.activity = activity;
        viewModel = new ViewModel(activity, this);
        viewModel.getListView().setAdapter(new ArrayAdapter<String>(activity, android.R.layout.simple_list_item_1, viewModel.getNavItems()));
        viewModel.getListView().setOnItemClickListener(new ListView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0 :
                        viewModel.setI(new Intent(activity, MainActivity.class));
                        activity.startActivity(viewModel.getI());
                        activity.finish();
                        break;
                    case 1 :
                        viewModel.setI(new Intent(activity, BNActivity.class));
                        activity.startActivity(viewModel.getI());
                        activity.finish();
                        break;
                    case 2 :
                        viewModel.setI(new Intent(activity, TKActivity.class));
                        activity.startActivity(viewModel.getI());
                        activity.finish();
                        break;
                }
            }
        });
        viewModel.getTabHost().setup();
        viewModel.setTs1(viewModel.getTabHost().newTabSpec("TAB Spec 1"));
        viewModel.getTs1().setContent(R.id.content1);
        viewModel.getTs1().setIndicator("뉴스");
        viewModel.getTabHost().addTab(viewModel.getTs1());
        viewModel.setTs2(viewModel.getTabHost().newTabSpec("TAB Spec 2"));
        viewModel.getTs2().setContent(R.id.content2);
        viewModel.getTs2().setIndicator("검색어");
        viewModel.getTabHost().addTab(viewModel.getTs2());
        for(int i = 0; i < viewModel.getTabHost().getTabWidget().getChildCount(); i++)
            viewModel.getTabHost().getTabWidget().getChildTabViewAt(i).setBackgroundColor(Color.parseColor("#EDEDED"));
        viewModel.getTabHost().getTabWidget().setCurrentTab(0);
        viewModel.getTabHost().getTabWidget().getChildAt(0).setBackgroundColor(Color.parseColor("#3498DB"));
        viewModel.getTabHost().setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {
                for(int i = 0; i < viewModel.getTabHost().getTabWidget().getChildCount(); i++)
                    viewModel.getTabHost().getTabWidget().getChildTabViewAt(i).setBackgroundColor(Color.parseColor("#EDEDED"));
                viewModel.getTabHost().getTabWidget().getChildAt(viewModel.getTabHost().getCurrentTab()).setBackgroundColor(Color.parseColor("#3498DB"));
                viewModel.setD_year(viewModel.getCalendar().get(Calendar.YEAR));
                viewModel.setD_month(viewModel.getCalendar().get(Calendar.MONTH));
                viewModel.setD_day(viewModel.getCalendar().get(Calendar.DAY_OF_MONTH));
                viewModel.setD_hour(viewModel.getCalendar().get(Calendar.HOUR_OF_DAY));
                if(viewModel.getTabHost().getCurrentTab() == 0) {
                    new JSONTask(viewModel.getRank_0(), 100).execute(viewModel.getIpAdress() + "/users100");
                    viewModel.setTab_state(0, 0);
                    viewModel.getDate_text()[0].setText(viewModel.getDateFormat()[0].format(new Date(viewModel.getGetNow()[1])));
                    viewModel.setTab_state(2, 0);
                    viewModel.getTab_1()[0].setSelected(true);
                    for(int i = 1; i < 2; i++)
                        viewModel.getTab_1()[i].setSelected(false);
                    viewModel.setGetNow(2, viewModel.getGetNow()[0]);
                    for(int i = 0; i < 10; i++) {
                        viewModel.getTv_rank()[i].setText(Integer.toString(i + 1));
                    }
                }
                else {
                    new JSONTask(viewModel.getRank_1(), 10).execute(viewModel.getIpAdress() + "/users10");
                    viewModel.setTab_state(0, 1);
                    viewModel.getDate_text()[1].setText(viewModel.getDateFormat()[0].format(new Date(viewModel.getGetNow()[2])));
                    viewModel.setTab_state(1, 0);
                    viewModel.getTab_0()[0].setSelected(true);
                    for(int i = 1; i < 6; i++)
                        viewModel.getTab_0()[i].setSelected(false);
                    viewModel.setGetNow(1, viewModel.getGetNow()[0]);
                }
            }
        });
        viewModel.getDate_btn()[0].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(activity, THEME_HOLO_LIGHT, dateSetListener, viewModel.getD_year(), viewModel.getD_month(), viewModel.getD_day()).show();
            }
        });
        viewModel.getDate_btn()[1].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(activity, THEME_HOLO_LIGHT, dateSetListener, viewModel.getD_year(), viewModel.getD_month(), viewModel.getD_day()).show();
            }
        });
        viewModel.getTime_btn()[0].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new TimePickerDialog(activity, THEME_HOLO_LIGHT, timeSetListener, viewModel.getD_hour(), 0, true).show();
            }
        });
        viewModel.getTime_btn()[1].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new TimePickerDialog(activity, THEME_HOLO_LIGHT, timeSetListener, viewModel.getD_hour(), 0, true).show();
            }
        });
        viewModel.getNav_btn().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewModel.getDrawerLayout().openDrawer(Gravity.START);
            }
        });
        viewModel.getDate_text()[0].setText(viewModel.getDateFormat()[0].format(new Date(viewModel.getGetNow()[1])));
        viewModel.getDate_text()[0].setText(viewModel.getDateFormat()[0].format(new Date(viewModel.getGetNow()[2])));
        viewModel.getLeft_btn()[0].setOnClickListener(bListener);
        viewModel.getLeft_btn()[1].setOnClickListener(bListener);
        viewModel.getRight_btn()[0].setOnClickListener(bListener);
        viewModel.getRight_btn()[1].setOnClickListener(bListener);
        for(int i = 0; i < 6; i++) {
            viewModel.getTab_0()[i].setOnClickListener(bListener);
        }
        for(int i = 0; i < 2; i++) {
            viewModel.getTab_1()[i].setOnClickListener(bListener);
        }
        viewModel.getTab_0()[0].setSelected(true);
        viewModel.getTab_1()[0].setSelected(true);
        new JSONTask(viewModel.getRank_0(), 100).execute(viewModel.getIpAdress() + "/users100");
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
            if (result <= viewModel.getGetNow()[0]) {
                viewModel.setD_day(year);
                viewModel.setD_month(month);
                viewModel.setD_day(dayOfMonth);
                switch (viewModel.getTab_state()[0]) {
                    case 0:
                        viewModel.setGetNow(1, result);
                        viewModel.getDate_text()[0].setText(viewModel.getDateFormat()[0].format(new Date(viewModel.getGetNow()[1])));
                        new JSONTaskPOST(viewModel.getRank_0(), viewModel.getTab_state()[1] + 100, viewModel.getDateFormat()[1].format(new Date(viewModel.getGetNow()[1])), viewModel.getD_hour()).execute(viewModel.getIpAdress() + "/post");
                        break;
                    case 1:
                        viewModel.setGetNow(2, result);
                        viewModel.getDate_text()[1].setText(viewModel.getDateFormat()[0].format(new Date(viewModel.getGetNow()[2])));
                        new JSONTaskPOST(viewModel.getRank_1(), viewModel.getTab_state()[2] + 10, viewModel.getDateFormat()[1].format(new Date(viewModel.getGetNow()[2])), viewModel.getD_hour()).execute(viewModel.getIpAdress() + "/post");
                        break;
                }
            } else
                Toast.makeText(activity, "Impossible", Toast.LENGTH_SHORT).show();
        }
    };

    TimePickerDialog.OnTimeSetListener timeSetListener = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            Calendar calendar = Calendar.getInstance(Locale.KOREA);
            calendar.set(viewModel.getD_year(), viewModel.getD_month(), viewModel.getD_day(), hourOfDay, minute);
            calendar.set(Calendar.MINUTE, 0);
            long result = calendar.getTimeInMillis();
            if (result <= viewModel.getGetNow()[0]) {
                viewModel.setD_hour(hourOfDay);
                switch (viewModel.getTab_state()[0]) {
                    case 0:
                        viewModel.setGetNow(1, result);
                        viewModel.getDate_text()[0].setText(viewModel.getDateFormat()[0].format(new Date(viewModel.getGetNow()[1])));
                        new JSONTaskPOST(viewModel.getRank_0(), viewModel.getTab_state()[1] + 100, viewModel.getDateFormat()[1].format(new Date(viewModel.getGetNow()[1])), viewModel.getD_hour()).execute(viewModel.getIpAdress() + "/post");
                        break;
                    case 1:
                        viewModel.setGetNow(2, result);
                        viewModel.getDate_text()[1].setText(viewModel.getDateFormat()[0].format(new Date(viewModel.getGetNow()[2])));
                        new JSONTaskPOST(viewModel.getRank_1(), viewModel.getTab_state()[2] + 10, viewModel.getDateFormat()[1].format(new Date(viewModel.getGetNow()[2])), viewModel.getD_hour()).execute(viewModel.getIpAdress() + "/post");
                        break;
                }
            } else
                Toast.makeText(activity, "Impossible", Toast.LENGTH_SHORT).show();
        }
    };

    Button.OnClickListener bListener = new Button.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.left_btn_1:
                    viewModel.setD_day(viewModel.getD_day() - 1);
                    viewModel.setGetNow(1, viewModel.getGetNow()[1] - viewModel.getDay());
                    viewModel.getDate_text()[0].setText(viewModel.getDateFormat()[0].format(new Date(viewModel.getGetNow()[1])));
                    new JSONTaskPOST(viewModel.getRank_0(), viewModel.getTab_state()[1] + 100, viewModel.getDateFormat()[1].format(new Date(viewModel.getGetNow()[1])), viewModel.getD_hour()).execute(viewModel.getIpAdress() + "/post");
                    break;
                case R.id.right_btn_1:
                    if(viewModel.getGetNow()[0] <= viewModel.getGetNow()[1]) {
                        Toast.makeText(activity, "Impossible", Toast.LENGTH_SHORT).show(); break;
                    }
                    else {
                        viewModel.setD_day(viewModel.getD_day() + 1);
                        viewModel.setGetNow(1, viewModel.getGetNow()[1] + viewModel.getDay());
                        viewModel.getDate_text()[0].setText(viewModel.getDateFormat()[0].format(new Date(viewModel.getGetNow()[1])));
                        new JSONTaskPOST(viewModel.getRank_0(), viewModel.getTab_state()[1] + 100, viewModel.getDateFormat()[1].format(new Date(viewModel.getGetNow()[1])), viewModel.getD_hour()).execute(viewModel.getIpAdress() + "/post");
                        break;
                    }
                case R.id.left_btn_2:
                    viewModel.setD_day(viewModel.getD_day() - 1);
                    viewModel.setGetNow(2, viewModel.getGetNow()[2] - viewModel.getDay());
                    viewModel.getDate_text()[1].setText(viewModel.getDateFormat()[0].format(new Date(viewModel.getGetNow()[2])));
                    new JSONTaskPOST(viewModel.getRank_1(), viewModel.getTab_state()[2] + 10, viewModel.getDateFormat()[1].format(new Date(viewModel.getGetNow()[2])), viewModel.getD_hour()).execute(viewModel.getIpAdress() + "/post");
                    break;
                case R.id.right_btn_2:
                    if(viewModel.getGetNow()[0] <= viewModel.getGetNow()[2]) {
                        Toast.makeText(activity, "Impossible", Toast.LENGTH_SHORT).show(); break;
                    }
                    else {
                        viewModel.setD_day(viewModel.getD_day() + 1);
                        viewModel.setGetNow(2, viewModel.getGetNow()[2] + viewModel.getDay());
                        viewModel.getDate_text()[1].setText(viewModel.getDateFormat()[0].format(new Date(viewModel.getGetNow()[2])));
                        new JSONTaskPOST(viewModel.getRank_1(), viewModel.getTab_state()[2] + 10, viewModel.getDateFormat()[1].format(new Date(viewModel.getGetNow()[2])), viewModel.getD_hour()).execute(viewModel.getIpAdress() + "/post");
                        break;
                    }
                case R.id.tab_0_1:
                    viewModel.setTab_state(1, 0);
                    for(int i = 0; i < 6; i ++) {
                        if(i != viewModel.getTab_state()[1])
                            viewModel.getTab_0()[i].setSelected(false);
                        else
                            viewModel.getTab_0()[i].setSelected(true);
                    }
                    new JSONTaskPOST(viewModel.getRank_0(), viewModel.getTab_state()[1] + 100, viewModel.getDateFormat()[1].format(new Date(viewModel.getGetNow()[1])), viewModel.getD_hour()).execute(viewModel.getIpAdress() + "/post");
                    break;
                case R.id.tab_0_2:
                    viewModel.setTab_state(1, 1);
                    for(int i = 0; i < 6; i ++) {
                        if(i != viewModel.getTab_state()[1])
                            viewModel.getTab_0()[i].setSelected(false);
                        else
                            viewModel.getTab_0()[i].setSelected(true);
                    }
                    new JSONTaskPOST(viewModel.getRank_0(), viewModel.getTab_state()[1] + 100, viewModel.getDateFormat()[1].format(new Date(viewModel.getGetNow()[1])), viewModel.getD_hour()).execute(viewModel.getIpAdress() + "/post");
                    break;
                case R.id.tab_0_3:
                    viewModel.setTab_state(1, 2);
                    for(int i = 0; i < 6; i ++) {
                        if(i != viewModel.getTab_state()[1])
                            viewModel.getTab_0()[i].setSelected(false);
                        else
                            viewModel.getTab_0()[i].setSelected(true);
                    }
                    new JSONTaskPOST(viewModel.getRank_0(), viewModel.getTab_state()[1] + 100, viewModel.getDateFormat()[1].format(new Date(viewModel.getGetNow()[1])), viewModel.getD_hour()).execute(viewModel.getIpAdress() + "/post");
                    break;
                case R.id.tab_0_4:
                    viewModel.setTab_state(1, 3);
                    for(int i = 0; i < 6; i ++) {
                        if(i != viewModel.getTab_state()[1])
                            viewModel.getTab_0()[i].setSelected(false);
                        else
                            viewModel.getTab_0()[i].setSelected(true);
                    }
                    new JSONTaskPOST(viewModel.getRank_0(), viewModel.getTab_state()[1] + 100, viewModel.getDateFormat()[1].format(new Date(viewModel.getGetNow()[1])), viewModel.getD_hour()).execute(viewModel.getIpAdress() + "/post");
                    break;
                case R.id.tab_0_5:
                    viewModel.setTab_state(1, 4);
                    for(int i = 0; i < 6; i ++) {
                        if(i != viewModel.getTab_state()[1])
                            viewModel.getTab_0()[i].setSelected(false);
                        else
                            viewModel.getTab_0()[i].setSelected(true);
                    }
                    new JSONTaskPOST(viewModel.getRank_0(), viewModel.getTab_state()[1] + 100, viewModel.getDateFormat()[1].format(new Date(viewModel.getGetNow()[1])), viewModel.getD_hour()).execute(viewModel.getIpAdress() + "/post");
                    break;
                case R.id.tab_0_6:
                    viewModel.setTab_state(1, 5);
                    for(int i = 0; i < 6; i ++) {
                        if(i != viewModel.getTab_state()[1])
                            viewModel.getTab_0()[i].setSelected(false);
                        else
                            viewModel.getTab_0()[i].setSelected(true);
                    }
                    new JSONTaskPOST(viewModel.getRank_0(), viewModel.getTab_state()[1] + 100, viewModel.getDateFormat()[1].format(new Date(viewModel.getGetNow()[1])), viewModel.getD_hour()).execute(viewModel.getIpAdress() + "/post");
                    break;
                case R.id.tab_1_1:
                    viewModel.setTab_state(2, 0);
                    for(int i = 0; i < 10; i++) {
                        viewModel.getTv_rank()[i].setText(Integer.toString(i + 1));
                    }
                    for(int i = 0; i < 2; i ++) {
                        if(i != viewModel.getTab_state()[2])
                            viewModel.getTab_1()[i].setSelected(false);
                        else
                            viewModel.getTab_1()[i].setSelected(true);
                    }
                    new JSONTaskPOST(viewModel.getRank_1(), viewModel.getTab_state()[2] + 10, viewModel.getDateFormat()[1].format(new Date(viewModel.getGetNow()[2])), viewModel.getD_hour()).execute(viewModel.getIpAdress() + "/post");
                    break;
                case R.id.tab_1_2:
                    viewModel.setTab_state(2, 1);
                    for(int i = 0; i < 10; i++) {
                        viewModel.getTv_rank()[i].setText(Integer.toString(i + 11));
                    }

                    for(int i = 0; i < 2; i ++) {
                        if(i != viewModel.getTab_state()[2])
                            viewModel.getTab_1()[i].setSelected(false);
                        else
                            viewModel.getTab_1()[i].setSelected(true);
                    }
                    new JSONTaskPOST(viewModel.getRank_1(), viewModel.getTab_state()[2] + 10, viewModel.getDateFormat()[1].format(new Date(viewModel.getGetNow()[2])), viewModel.getD_hour()).execute(viewModel.getIpAdress() + "/post");
                    break;
            }
        }
    };
}
