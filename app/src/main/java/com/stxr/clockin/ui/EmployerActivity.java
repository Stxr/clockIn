package com.stxr.clockin.ui;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.content.FileProvider;
import android.view.View;

import com.baidu.mapapi.model.LatLng;
import com.github.clans.fab.FloatingActionButton;
import com.stxr.clockin.R;
import com.stxr.clockin.entity.ClockIn;
import com.stxr.clockin.entity.MyUser;
import com.stxr.clockin.utils.ToastUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UploadFileListener;

/**
 * Created by stxr on 2018/4/15.
 */

public class EmployerActivity extends BaseMapActivity {
    public static final int REQUEST_PHOTO = 123;
    private ClockIn clockIn;
    //请假菜单
    private FloatingActionButton fab_leave;
    private FloatingActionButton fab_clockIn;


    @Override
    List<FloatingActionButton> addFab() {
        List<FloatingActionButton> buttons = new ArrayList<>();
        fab_leave = createFAB(R.color.colorPrimary, R.color.colorPrimaryDark, R.drawable.ic_leave, "请假");
        fab_clockIn = createFAB(R.color.colorPrimary, R.color.colorPrimaryDark, R.drawable.clock_in, "打卡");
        buttons.add(fab_clockIn);
        buttons.add(fab_leave);
        return buttons;
    }

    @Override
    LatLng firstFocusOnMap() {
        return null;
    }

    @Override
    float zoom() {
        return 20;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData();
    }

    private void initData() {
        fab_clockIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fab_menu.close(true);
                clockIn();
            }
        });
        fab_leave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fab_menu.close(true);
                askForLeave();
            }
        });
    }

    //签到
    void clockIn() {
        clockIn = new ClockIn();
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        //Android 7.0 以上需要用FileProvider来加载图片
        Uri uri = FileProvider.getUriForFile(this,
                "com.stxr.clockin.photo_provider",
                ClockInLab.getPhotoFile(EmployerActivity.this, clockIn));
        //赋予权限
        List<ResolveInfo> resInfoList = getPackageManager()
                .queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        for (ResolveInfo resolveInfo : resInfoList) {
            String packageName = resolveInfo.activityInfo.packageName;
            grantUriPermission(packageName, uri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        }
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        startActivityForResult(intent, REQUEST_PHOTO);
    }

    //请假
    void askForLeave() {
        startActivity(AskForLeaveActivity.class);
    }

    private void updateData(BmobFile file) {
        //设置经纬度
        clockIn.setLatitude(currentLocation.getLatitude());
        clockIn.setLongitude(currentLocation.getLongitude());
        //绑定用户
        clockIn.setUser(BmobUser.getCurrentUser(MyUser.class));
        clockIn.setPhotoFile(file);
        clockIn.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                if (e == null) {
                    Snackbar.make(mapView, "打卡成功", Snackbar.LENGTH_SHORT).show();
                } else {
                    ToastUtil.show(EmployerActivity.this, e.getMessage());
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_PHOTO && resultCode == -1) {
            //取消权限
            Uri uri = FileProvider.getUriForFile(this,
                    "com.stxr.clockin.photo_provider",
                    ClockInLab.getPhotoFile(EmployerActivity.this, clockIn));
            revokeUriPermission(uri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            final BmobFile file = new BmobFile(new File(ClockInLab.getPhotoPath(this, clockIn)));
            dialog.show("正在上传打卡信息");
            file.uploadblock(new UploadFileListener() {
                @Override
                public void done(BmobException e) {
                    dialog.dismiss();
                    if (e == null) {
                        updateData(file);
                    } else {
                        ToastUtil.show(EmployerActivity.this, e.getMessage());
                    }
                }
            });
        }
    }

}
