package com.stxr.clockin.ui;

import android.app.AlertDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mapapi.model.LatLng;
import com.bumptech.glide.Glide;
import com.github.clans.fab.FloatingActionButton;
import com.stxr.clockin.R;
import com.stxr.clockin.entity.ClockIn;
import com.stxr.clockin.entity.MyUser;
import com.stxr.clockin.utils.clusterutil.clustering.Cluster;
import com.stxr.clockin.utils.clusterutil.clustering.ClusterManager;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListener;

/**
 * Created by stxr on 2018/4/16.
 */

public class BossActivity extends BaseMapActivity {
    private FloatingActionButton fab_see;
    //设置点聚合
    private ClusterManager<ClockIn> clusterManager;
    private List<MyItem> items = new ArrayList<>();

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

        clusterManager = new ClusterManager<>(this, baiduMap);
//        clusterManager.addItems(items);
        // 设置地图监听，当地图状态发生改变时，进行点聚合运算
        baiduMap.setOnMapStatusChangeListener(clusterManager);
        // 设置maker点击时的响应
        baiduMap.setOnMarkerClickListener(clusterManager);

        clusterManager.setOnClusterClickListener(new ClusterManager.OnClusterClickListener<ClockIn>() {
            @Override
            public boolean onClusterClick(Cluster<ClockIn> cluster) {
                Toast.makeText(BossActivity.this,
                        "有" + cluster.getSize() + "个签到", Toast.LENGTH_SHORT).show();

                return false;
            }
        });
        clusterManager.setOnClusterItemClickListener(new ClusterManager.OnClusterItemClickListener<ClockIn>() {
            @Override
            public boolean onClusterItemClick(final ClockIn item) {
                //显示详情
                View view = LayoutInflater.from(BossActivity.this).inflate(R.layout.dialog_clock_in, null);
                final TextView tv_name = view.findViewById(R.id.tv_name);
                TextView tv_time = view.findViewById(R.id.tv_time);
                ImageView imageView = view.findViewById(R.id.iv_location);

                BmobQuery<MyUser> user = new BmobQuery<>();
                user.getObject(item.getUser().getObjectId(), new QueryListener<MyUser>() {
                    @Override
                    public void done(MyUser myUser, BmobException e) {
                        if (e == null) {
                            tv_name.setText(myUser.getUsername());
                        }
                    }
                });

                tv_time.setText(item.getUpdatedAt());
                Glide.with(BossActivity.this).load(item.getPhotoFile().getUrl()).into(imageView);
                new AlertDialog.Builder(BossActivity.this)
                        .setView(view)
                        .create()
                        .show();
                return false;
            }
        });
    }


    void query() {
        BmobQuery<ClockIn> queryClockIn = new BmobQuery<>();
        clusterManager.clearItems();
        queryClockIn.findObjects(new FindListener<ClockIn>() {
            @Override
            public void done(List<ClockIn> list, BmobException e) {
                if (e == null) {
                    for (ClockIn clockIn : list) {
//                        items.add(new MyItem(new LatLng(clockIn.getLatitude(), clockIn.getLongitude())));
                        clusterManager.addItem(clockIn);
                        Log.e(TAG, "done: " + clockIn.toString());
                    }
                    clusterManager.cluster();
                    focusCluster(list);
//                    clusterManager.addItems(items);
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
