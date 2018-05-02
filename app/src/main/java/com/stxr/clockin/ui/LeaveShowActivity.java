package com.stxr.clockin.ui;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;

import com.stxr.clockin.R;
import com.stxr.clockin.adapter.LeaveShowAdapter;
import com.stxr.clockin.entity.ClockIn;
import com.stxr.clockin.entity.MyUser;
import com.stxr.clockin.entity.NoteForLeave;
import com.stxr.clockin.utils.ClockInUtil;
import com.stxr.clockin.utils.ToastUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by stxr on 2018/4/30.
 */

public class LeaveShowActivity extends BaseActivity {
    private static List<NoteForLeave> notes;
    @BindView(R.id.rv_show)
    RecyclerView recyclerView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clock_in);
        ButterKnife.bind(this);

        recyclerView.setLayoutManager(new LinearLayoutManager(LeaveShowActivity.this));
        LeaveShowAdapter adapter = new LeaveShowAdapter(LeaveShowActivity.this, notes);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new LeaveShowAdapter.OnItemClickListener() {
            @Override
            public void onClick(final NoteForLeave noteForLeave, final ImageView photo) {
                if (BmobUser.getCurrentUser(MyUser.class).isBoss()) {
                    new AlertDialog.Builder(LeaveShowActivity.this)
                            .setMessage("是否同意请假")
                            .setPositiveButton("同意", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    noteForLeave.setAdmit(true);
                                    noteForLeave.update(new UpdateListener() {
                                        @Override
                                        public void done(BmobException e) {
                                            if (e == null) {
                                                photo.setImageResource(R.drawable.ic_pass);
                                            } else {
                                                ToastUtil.show(LeaveShowActivity.this,e.getMessage()+"请重新判断");
                                                noteForLeave.setAdmit(null);
                                            }
                                        }
                                    });
                                }
                            })
                            .setNegativeButton("不同意", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    noteForLeave.setAdmit(false);
                                    noteForLeave.update(new UpdateListener() {
                                        @Override
                                        public void done(BmobException e) {
                                            if (e == null) {
                                                photo.setImageResource(R.drawable.ic_failed);
                                            } else {
                                                ToastUtil.show(LeaveShowActivity.this,e.getMessage()+"请重新判断");
                                                noteForLeave.setAdmit(null);
                                            }
                                        }
                                    });
                                }
                            })
                            .create()
                            .show();
                }
            }
        });
    }

    public static Intent newInstance(Context context, List<NoteForLeave> list) {
        notes = list;
        return new Intent(context, LeaveShowActivity.class);
    }
}
