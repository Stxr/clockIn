package com.stxr.clockin.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import com.stxr.clockin.R;

/**
 * Created by stxr on 2018/4/15.
 */

public abstract class BaseActivity extends AppCompatActivity {
//    protected abstract Object getFragment();
//    @Override
//    protected void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_fragment_container);
//        FragmentManager fm = getSupportFragmentManager();
//        Fragment fragment = fm.findFragmentById(R.id.fragment_container);
//        if (fragment == null) {
//            if (getFragment() instanceof Fragment) {
//                fm.beginTransaction()
//                        .add(R.id.fragment_container, (Fragment) getFragment())
//                        .commit();
//            } else if (getFragment() instanceof Integer) {
//
//            }
//        }
//    }


    protected void startActivity(Class<?> clazz) {
        Intent intent = new Intent(this, clazz);
        startActivity(intent);
    }
}
