package com.stxr.clockin.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.stxr.clockin.R;
import com.stxr.clockin.entity.MyUser;
import com.stxr.clockin.utils.SavePassword;
import com.stxr.clockin.utils.ShareUtil;
import com.stxr.clockin.utils.ToastUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

/**
 * Created by stxr on 2018/4/15.
 * 登陆界面
 */

public class SignInActivity extends BaseActivity {
    public static final int SIGN_IN_REQUEST_CODE = 110;
    public static final String NAME = "level";
    public static int dir;
    @BindView(R.id.tv_sign_up)
    TextView tv_sign_up;
    @BindView(R.id.edt_id)
    EditText edt_id;
    @BindView(R.id.edt_password)
    EditText edt_password;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        dir = intent.getIntExtra(NAME, ChooseActivity.EMPLOYER);
        if (dir == ChooseActivity.BOSS) {
            setTitle("管理员登录");
            String name = SavePassword.getBossName(this);
            if (!name.equals("")) {
                edt_id.setText(name);
                edt_password.setText(SavePassword.getBossPassword(this,name));
            }
            //管理员不可注册
            tv_sign_up.setVisibility(View.GONE);
        } else {
            setTitle("职工登录");
            String name = SavePassword.getEmployerName(this);
            if (name != null) {
                edt_id.setText(name);
                edt_password.setText(SavePassword.getEmployerPassword(this,name));
            }
        }
    }

    @OnClick({R.id.btn_sign_in,R.id.tv_sign_up})
    void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_sign_in:
                MyUser user = new MyUser();
                user.setUsername(edt_id.getText().toString());
                user.setPassword(edt_password.getText().toString());
                if (dir == ChooseActivity.EMPLOYER) {
                    user.login(new SaveListener<MyUser>() {
                        @Override
                        public void done(MyUser myUser, BmobException e) {
                            if (e != null) {
                                ToastUtil.show(SignInActivity.this, e.getMessage());
                            } else {
                                if (!myUser.isBoss()) {
                                    startActivity(EmployerActivity.class);
                                    SavePassword.saveEmployerPassword(SignInActivity.this,myUser.getUsername(),edt_password.getText().toString());
                                    finish();
                                } else {
                                    ToastUtil.show(SignInActivity.this, "账号密码错误");
                                }
                            }
                        }
                    });

                } else {
                    user.login(new SaveListener<MyUser>() {
                        @Override
                        public void done(MyUser myUser, BmobException e) {
                            if (e == null && myUser.isBoss()) {
                                SavePassword.saveBossPassword(SignInActivity.this,myUser.getUsername(),edt_password.getText().toString());
                                startActivity(BossActivity.class);
                            } else {
                                ToastUtil.show(SignInActivity.this, "账号密码错误");
                            }
                        }
                    });
                }
                break;
            case R.id.tv_sign_up:
                Intent intent = new Intent(this, SignUpActivity.class);
                startActivityForResult(intent, SIGN_IN_REQUEST_CODE);
                break;
            default:
                break;
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SIGN_IN_REQUEST_CODE && resultCode == SignUpActivity.SIGN_UP_SUCCESS_RESULT_CODE) {
            if (data != null) {
                edt_id.setText(data.getStringExtra(SignUpActivity.ID));
            }
        }
    }

    public static Intent newInstance(Context context, int what) {
        Intent intent = new Intent(context, SignInActivity.class);
        intent.putExtra(NAME, what);
        return intent;
    }
}
