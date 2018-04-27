package com.stxr.clockin.ui;

import android.content.DialogInterface;
import android.os.Bundle;
import android.preference.ListPreference;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.AbsSeekBar;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.stxr.clockin.R;
import com.stxr.clockin.entity.NoteForLeave;
import com.stxr.clockin.utils.ToastUtil;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

/**
 * Created by stxr on 2018/4/26.
 * 请假
 */

public class AskForLeaveActivity extends BaseActivity {

    @BindView(R.id.edt_leave_reason)
    EditText edt_reason;
    @BindView(R.id.tv_time_to)
    TextView tv_endTime;
    @BindView(R.id.tv_time_from)
    TextView tv_startTime;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ask_for_leave);
        ButterKnife.bind(this);
        //默认设置为当天
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("Y-M-d,EE");
        String format = dateFormat.format(date);
        tv_startTime.setText(format);
        tv_endTime.setText(format);
    }

    /**
     * 时间设置
     * @param v
     */
    @OnClick({R.id.tv_time_from, R.id.tv_time_to})
    void timeSelect(final View v) {
        final DatePicker datePicker = new DatePicker(this);
        new AlertDialog.Builder(this)
                .setView(datePicker)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int day = datePicker.getDayOfMonth();
                        int month = datePicker.getMonth();
                        int year = datePicker.getYear();
                        Date time = new GregorianCalendar(year, month, day).getTime();
                        TextView textView = (TextView) v;
                        SimpleDateFormat format = new SimpleDateFormat("Y-M-d,EE", Locale.CHINESE);
                        textView.setText(format.format(time));
                    }
                })
                .setNegativeButton("取消", null)
                .create()
                .show();
    }

    /**
     * 确定
     * @param v
     */
    @OnClick(R.id.btn_confirm)
    void confirm(final View v) {
        NoteForLeave note = new NoteForLeave();
        String endTime = tv_endTime.getText().toString();
        String reason = edt_reason.getText().toString();
        String startTime = tv_startTime.getText().toString();
        if (!reason.equals("")) {
            note.setEndTime(endTime);
            note.setStartTime(startTime);
            note.setReason(reason);
            note.save(new SaveListener<String>() {
                @Override
                public void done(String s, BmobException e) {
                    if (e == null) {
                        new AlertDialog.Builder(AskForLeaveActivity.this)
                                .setMessage("请求成功")
                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        AskForLeaveActivity.this.finish();
                                    }
                                })
                                .setCancelable(false)
                                .create()
                                .show();
                    } else {
                        ToastUtil.show(AskForLeaveActivity.this, e.getMessage());
                    }
                }
            });
        } else {
            ToastUtil.show(AskForLeaveActivity.this, "请填写请假理由");
        }
    }
}
