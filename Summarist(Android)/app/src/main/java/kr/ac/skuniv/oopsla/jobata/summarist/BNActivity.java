package kr.ac.skuniv.oopsla.jobata.summarist;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class BNActivity extends AppCompatActivity {
    BNViewController bnViewController;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_breaking_news);
        bnViewController = new BNViewController(this);
    }
}
