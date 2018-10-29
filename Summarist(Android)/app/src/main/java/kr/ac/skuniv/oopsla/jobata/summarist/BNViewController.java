package kr.ac.skuniv.oopsla.jobata.summarist;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class BNViewController {
    private AppCompatActivity activity;
    private BNViewModel bnViewModel;

    public BNViewController(final AppCompatActivity activity) {
        this.activity = activity;
        bnViewModel = new BNViewModel(activity, this);
        bnViewModel.getNav_list().setAdapter(bnViewModel.getArrayAdapter());
        bnViewModel.getNav_list().setOnItemClickListener(new ListView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0 :
                        bnViewModel.setI(new Intent(activity, MainActivity.class));
                        activity.startActivity(bnViewModel.getI());
                        activity.finish();
                        break;
                    case 1 :
                        bnViewModel.setI(new Intent(activity, BNActivity.class));
                        activity.startActivity(bnViewModel.getI());
                        activity.finish();
                    case 2 :
                        bnViewModel.setI(new Intent(activity, TKActivity.class));
                        activity.startActivity(bnViewModel.getI());
                        activity.finish();
                        break;
                }
            }
        });
        bnViewModel.getList_view().setAdapter(bnViewModel.getBnAdapter());
        bnViewModel.getBnT().start();
        bnViewModel.getBnAdapter().notifyDataSetChanged();
        bnViewModel.setCalendar(Calendar.getInstance(Locale.KOREA));
        bnViewModel.getDate_text().setText(bnViewModel.getDateFormat1().format(new Date(bnViewModel.getCalendar().getTimeInMillis())));
        bnViewModel.getTime_text().setText(bnViewModel.getDateFormat2().format(new Date(bnViewModel.getCalendar().getTimeInMillis())));
        bnViewModel.getT().start();
        bnViewModel.getTabHost().setup();
        bnViewModel.setTs1(bnViewModel.getTabHost().newTabSpec("TAB Spec 1"));
        bnViewModel.getTs1().setContent(R.id.content1);
        bnViewModel.getTs1().setIndicator("뉴스 속보");
        bnViewModel.getTabHost().addTab(bnViewModel.getTs1());
        bnViewModel.getTabHost().getTabWidget().setCurrentTab(0);
        bnViewModel.getTabHost().getTabWidget().getChildTabViewAt(0).setBackgroundColor(Color.parseColor("#3498DB"));
        bnViewModel.getNav_btn().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bnViewModel.getDrawerLayout().openDrawer(Gravity.START);
            }
        });
        bnViewModel.getSetting_btn().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bnViewModel.getBuilder().setView(bnViewModel.getDialogView());
                bnViewModel.setDialog(bnViewModel.getBuilder().show());
            }
        });
        new JSONTaskBN(bnViewModel).execute(bnViewModel.getIpAdress() + "/bn");
    }
}
