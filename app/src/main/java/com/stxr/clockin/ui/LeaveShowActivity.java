package com.stxr.clockin.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.stxr.clockin.R;
import com.stxr.clockin.adapter.LeaveShowAdapter;
import com.stxr.clockin.entity.NoteForLeave;
import com.stxr.clockin.utils.ClockInUtil;
import com.stxr.clockin.utils.ToastUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by stxr on 2018/4/30.
 */

public class LeaveShowActivity extends BaseActivity {
    @BindView(R.id.rv_show)
    RecyclerView recyclerView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clock_in);
        ButterKnife.bind(this);
        dialog = new CustomLoadingDialog(this, "正在加载");
        dialog.show();
        ClockInUtil.query(NoteForLeave.class, new FindListener<NoteForLeave>() {
            @Override
            public void done(List<NoteForLeave> list, BmobException e) {
                if (e == null) {
                    dialog.dismiss();
                    recyclerView.setLayoutManager(new LinearLayoutManager(LeaveShowActivity.this));
                    recyclerView.setAdapter(new LeaveShowAdapter(LeaveShowActivity.this, list));
                } else {
                    ToastUtil.show(LeaveShowActivity.this,e.getMessage());
                }
            }
        });
    }
}
