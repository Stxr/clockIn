package com.stxr.clockin.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.stxr.clockin.R;

/**
 * 所有activity的基类
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}
