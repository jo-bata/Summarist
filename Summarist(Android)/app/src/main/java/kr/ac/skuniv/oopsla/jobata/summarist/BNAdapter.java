package kr.ac.skuniv.oopsla.jobata.summarist;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Pattern;

public class BNAdapter extends BaseAdapter {
    private AppCompatActivity activity;
    ArrayList<BNVO> bnList = new ArrayList<BNVO>();

    public BNAdapter(AppCompatActivity activity) {
        this.activity = activity;
    }

    @Override
    public int getCount() { return bnList.size(); }

    @Override
    public Object getItem(int position) { return null; }

    @Override
    public long getItemId(int position) { return 0; }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null) {
            LayoutInflater vi = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = vi.inflate(R.layout.bn_listview, null);
        }
        BNVO bnVO = bnList.get(position);
        TextView time_tv = (TextView)convertView.findViewById(R.id.time_tv);
        TextView title_tv = (TextView)convertView.findViewById(R.id.title_tv);
        Calendar calendar = Calendar.getInstance(Locale.KOREA);
        long getNow = calendar.getTimeInMillis();
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
        String nowTime = dateFormat.format(new Date(getNow));
        String timeSplit1[] = nowTime.split(":");
        String timeSplit2[] = bnVO.getTime().split(":");
        int hour = Integer.parseInt(timeSplit1[0]) - Integer.parseInt(timeSplit2[0]);
        int min = Integer.parseInt(timeSplit1[1]) - Integer.parseInt(timeSplit2[1]);
        if(hour > 0) {
            if(min < 0 && hour == 1)
                time_tv.setText("" + (min + 60) + "분전.");
            else
                time_tv.setText("" + hour + "시간전.");
        }
        else
            time_tv.setText("" + min + "분전.");
        title_tv.setText(bnVO.getTitle());
        return convertView;
    }
    public void addOne(BNVO bnVO) { bnList.add(bnVO); }
}