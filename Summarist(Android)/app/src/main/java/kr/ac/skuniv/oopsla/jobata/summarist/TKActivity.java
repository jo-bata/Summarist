package kr.ac.skuniv.oopsla.jobata.summarist;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class TKActivity extends AppCompatActivity {
    TKViewController tkViewController;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_today_kewords);
        tkViewController = new TKViewController(this);
    }
}