package kr.ac.skuniv.oopsla.jobata.summarist;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class WKViewController {
    private AppCompatActivity activity;
    private WKViewModel wkViewModel;

    public WKViewController(final AppCompatActivity activity) {
        this.activity = activity;
        wkViewModel = new WKViewModel(activity, this);
        wkViewModel.getNav_list().setAdapter(wkViewModel.getArrayAdapter());
        wkViewModel.getNav_list().setOnItemClickListener(new ListView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0 :
                        wkViewModel.setI(new Intent(activity, MainActivity.class));
                        activity.startActivity(wkViewModel.getI());
                        activity.finish();
                        break;
                    case 1 :
                        wkViewModel.setI(new Intent(activity, BNActivity.class));
                        activity.startActivity(wkViewModel.getI());
                        activity.finish();
                        break;
                    case 2 :
                        wkViewModel.setI(new Intent(activity, WKActivity.class));
                        activity.startActivity(wkViewModel.getI());
                        activity.finish();
                        break;
                }
            }
        });
        wkViewModel.getList_view().setAdapter(wkViewModel.getWkAdapter());
        wkViewModel.getWkAdapter().notifyDataSetChanged();
        wkViewModel.setCalendar(Calendar.getInstance(Locale.KOREA));
        wkViewModel.setGetNow(0, wkViewModel.getCalendar().getTimeInMillis() - (wkViewModel.getDay() * 6));
        wkViewModel.setGetNow(1, wkViewModel.getCalendar().getTimeInMillis());
        wkViewModel.getDate_text().setText(wkViewModel.getDateFormat().format(new Date(wkViewModel.getGetNow()[0])) + "~" + wkViewModel.getDateFormat2().format(new Date(wkViewModel.getGetNow()[1])));
        wkViewModel.getTabHost().setup();
        wkViewModel.setTs1(wkViewModel.getTabHost().newTabSpec("TAB Spec 1"));
        wkViewModel.getTs1().setContent(R.id.content1);
        wkViewModel.getTs1().setIndicator("주간 키워드");
        wkViewModel.getTabHost().addTab(wkViewModel.getTs1());
        wkViewModel.getTabHost().getTabWidget().setCurrentTab(0);
        wkViewModel.getTabHost().getTabWidget().getChildTabViewAt(0).setBackgroundColor(Color.parseColor("#3498DB"));
        wkViewModel.getNav_btn().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                wkViewModel.getDrawerLayout().openDrawer(Gravity.START);
            }
        });
        for(int i = 0; i < 20; i++) {
            final int finalI = i;
            wkViewModel.getTab_rank()[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    wkViewModel.setTab_state(finalI);
                    for(int i = 0; i < 20; i++) {
                        if(i != wkViewModel.getTab_state())
                            wkViewModel.getTab_rank()[i].setSelected(false);
                        else
                            wkViewModel.getTab_rank()[i].setSelected(true);
                    }
                    new JSONTaskWK(wkViewModel).execute("http://192.168.0.6:3000/wk");
                }
            });
        }
        wkViewModel.getTab_rank()[0].setSelected(true);
        new JSONTaskWK(wkViewModel).execute("http://192.168.0.6:3000/wk");  // 리스트뷰(post)
        new JSONTaskWK2(wkViewModel).execute("http://192.168.0.6:3000/getwk");  // 탭(get)
    }
}
