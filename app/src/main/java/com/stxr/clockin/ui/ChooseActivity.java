package com.stxr.clockin.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;

import com.stxr.clockin.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by stxr on 2018/4/15.
 * 选择登录界面
 */

public class ChooseActivity extends BaseActivity {
    public static final int BOSS = 1;
    public static final int EMPLOYER = 2;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose);
        ButterKnife.bind(this);

    }

    @OnClick({R.id.btn_boss, R.id.btn_employer})
    void chooseWay(View v) {
        if (v.getId() == R.id.btn_boss) {  //老板登录
            startActivity(SignInActivity.newInstance(this,BOSS));

        } else if (v.getId() == R.id.btn_employer) {  //员工登录
            startActivity(SignInActivity.newInstance(this,EMPLOYER));
        }
    }
}
