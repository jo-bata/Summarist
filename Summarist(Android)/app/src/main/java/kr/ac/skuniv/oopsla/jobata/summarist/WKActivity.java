package kr.ac.skuniv.oopsla.jobata.summarist;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;


public class WKActivity extends AppCompatActivity {
    WKViewController wkViewController;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weekly_keyword);
        wkViewController = new WKViewController(this);
    }
}
