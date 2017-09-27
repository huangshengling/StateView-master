package cn.holy.demo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;


/**
 * author : Holy Spirit
 * time   : 2017/09/26
 * desc   :
 * version: 1.0
 */
public class SecondActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        getFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, SecondFragment.newInstance())
                .commit();
    }
}
