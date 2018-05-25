package com.stxr.clockin.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Patterns;
import android.widget.EditText;

import com.stxr.clockin.R;
import com.stxr.clockin.utils.ToastUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by stxr on 2018/5/25.
 */

public class FindPasswordActivity extends BaseActivity {
    @BindView(R.id.edt_email)
    EditText edt_email;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_findp_assword);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.btn_confirm)
    void onConfirm() {
        final String email = edt_email.getText().toString();
        if (Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            BmobUser.resetPasswordByEmail(email, new UpdateListener() {
                @Override
                public void done(BmobException e) {
                    if (e == null) {
                        ToastUtil.show(FindPasswordActivity.this, "重置密码请求成功，请到" + email + "邮箱进行密码重置操作");
                        finish();
                    } else {
                        ToastUtil.show(FindPasswordActivity.this, "失败:" + e.getMessage());
                    }
                }
            });
        } else {
            edt_email.setError("邮箱格式错误");
        }
    }
}
