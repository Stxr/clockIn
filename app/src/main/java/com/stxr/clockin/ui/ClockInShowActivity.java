package com.stxr.clockin.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.stxr.clockin.R;
import com.stxr.clockin.adapter.ClockInShowAdapter;
import com.stxr.clockin.entity.ClockIn;
import com.stxr.clockin.entity.MyUser;
import com.stxr.clockin.utils.ClockInUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by stxr on 2018/4/30.
 */

public class ClockInShowActivity extends BaseActivity {

    private List<ClockIn> clockInList;
    @BindView(R.id.rv_show)
    RecyclerView recyclerView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clock_in);
        ButterKnife.bind(this);
        dialog = new CustomLoadingDialog(this, "正在加载");
        dialog.show();
        ClockInUtil.query(ClockIn.class, new FindListener<ClockIn>() {
            @Override
            public void done(List<ClockIn> clockIns, BmobException e) {
                if (e == null) {
                    dialog.dismiss();
                    clockInList = clockIns;
                    recyclerView.setLayoutManager(new LinearLayoutManager(ClockInShowActivity.this));
                    recyclerView.setAdapter(new ClockInShowAdapter(ClockInShowActivity.this,clockIns));
                }
            }
        });
    }
}
