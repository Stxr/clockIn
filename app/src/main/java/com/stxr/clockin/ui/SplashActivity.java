package com.stxr.clockin.ui;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;

import com.stxr.clockin.R;

import java.lang.ref.WeakReference;

/**
 * Created by stxr on 2018/4/15.
 * 过渡页
 */

public class SplashActivity extends BaseActivity {
    //延时1500ms
    private static final int DELAY_MILLIS = 1500;
    private static final int WHAT = 111;
    private static MyHandler handler;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splash);

        handler = new MyHandler(this);
        handler.sendEmptyMessageDelayed(WHAT, DELAY_MILLIS);
    }

    /**
     * 这么写是为了避免内存泄露
     */
    private static class MyHandler extends Handler {
        private WeakReference<BaseActivity> activity;
        private MyHandler(BaseActivity activity) {
            this.activity = new WeakReference<>(activity);
        }
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == WHAT) {
                //跳转到选择登录界面
                activity.get().startActivity(ChooseActivity.class);
                //结束界面
                activity.get().finish();
            }
        }
    }

}
