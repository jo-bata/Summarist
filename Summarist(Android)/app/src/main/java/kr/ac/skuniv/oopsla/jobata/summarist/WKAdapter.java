package kr.ac.skuniv.oopsla.jobata.summarist;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.text.util.Linkify;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WKAdapter extends BaseAdapter {
    private AppCompatActivity activity;
    ArrayList<WKVO> wkList = new ArrayList<WKVO>();

    public WKAdapter(AppCompatActivity activity) { this.activity = activity; }

    @Override
    public int getCount() { return wkList.size(); }

    @Override
    public Object getItem(int position) { return null; }

    @Override
    public long getItemId(int position) { return 0; }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null) {
            LayoutInflater vi = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = vi.inflate(R.layout.wk_listview, null);
        }
        WKVO wkVO = wkList.get(position);
        TextView title_tv = (TextView)convertView.findViewById(R.id.title_tv);
        Linkify.TransformFilter transform = new Linkify.TransformFilter() {
            @Override
            public String transformUrl(Matcher match, String url) {
                return "";
            }
        };
        title_tv.setText("");
        title_tv.append("" + wkVO.getTitle() + " (기사보기)");
        Linkify.addLinks(title_tv, Pattern.compile("(기사보기)"), wkVO.getUrl(), null, transform);
        return convertView;
    }
    public void addOne(WKVO wkVO) { wkList.add(wkVO); }
}