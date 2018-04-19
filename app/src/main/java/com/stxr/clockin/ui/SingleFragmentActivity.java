package com.stxr.clockin.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.stxr.clockin.R;

/**
 * Created by stxr on 2018/4/18.
 */

public class SingleFragmentActivity extends BaseActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment_container);
        Intent intent = getIntent();
        int resId = intent.getIntExtra("resId", -1);

        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.fragment_container);
        if (fragment == null) {
            fragment = FragmentFactory.createById(resId);
            fm.beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .addToBackStack(null)
                    .commit();
        }
    }
}
