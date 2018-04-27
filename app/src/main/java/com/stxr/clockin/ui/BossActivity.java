package com.stxr.clockin.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;

import com.baidu.mapapi.model.LatLng;
import com.github.clans.fab.FloatingActionButton;
import com.stxr.clockin.R;
import com.stxr.clockin.entity.ClockIn;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by stxr on 2018/4/16.
 */

public class BossActivity extends BaseMapActivity {
    private FloatingActionButton fab_see;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData();
    }

    void initData() {
        fab_see.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                query();
            }
        });

    }


    void query() {
        BmobQuery<ClockIn> queryClockIn = new BmobQuery<>();
        queryClockIn.findObjects(new FindListener<ClockIn>() {
            @Override
            public void done(List<ClockIn> list, BmobException e) {
                if (e == null) {
                    for (ClockIn clockIn : list) {
                        Log.e(TAG, "done: "+clockIn.toString() );
                    }
                }
            }
        });
    }

    @Override
    List<FloatingActionButton> addFab() {
        List<FloatingActionButton> buttons = new ArrayList<>();
        fab_see = createFAB(R.color.colorPrimary, R.color.colorPrimaryDark, R.drawable.ic_leave, "查看");
        buttons.add(fab_see);
        return buttons;
    }

    @Override
    LatLng firstFocusOnMap() {
        return new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
    }

    @Override
    float zoom() {
        return 17;
    }
}
