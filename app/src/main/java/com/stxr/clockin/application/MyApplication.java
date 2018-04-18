package com.stxr.clockin.application;

import android.app.Application;

import com.baidu.mapapi.SDKInitializer;

import cn.bmob.v3.Bmob;

/**
 * Created by stxr on 2018/4/15.
 */

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        SDKInitializer.initialize(this);
        Bmob.initialize(this, "1f02bf865d777cde74a1bc88c3113134");
    }
}
