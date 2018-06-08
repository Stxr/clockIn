package com.stxr.clockin.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.PolygonOptions;
import com.baidu.mapapi.map.Stroke;
import com.baidu.mapapi.model.LatLng;
import com.github.clans.fab.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.stxr.clockin.R;
import com.stxr.clockin.adapter.ClockInShowAdapter;
import com.stxr.clockin.entity.ClockIn;
import com.stxr.clockin.entity.MyUser;
import com.stxr.clockin.entity.NoteForLeave;
import com.stxr.clockin.utils.ClockInUtil;
import com.stxr.clockin.utils.ToastUtil;
import com.stxr.clockin.utils.clusterutil.clustering.Cluster;
import com.stxr.clockin.utils.clusterutil.clustering.ClusterManager;
import com.stxr.clockin.view.CustomDialog;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by stxr on 2018/4/16.
 */

public class BossActivity extends BaseMapActivity implements NavigationView.OnNavigationItemSelectedListener {
    private FloatingActionButton fab_see;
    private FloatingActionButton fab_limit;
    private List<LatLng> limits = new ArrayList<>();
    //设置点聚合
    private ClusterManager<ClockIn> clusterManager;
    private List<MyItem> items = new ArrayList<>();

    private CustomDialog customDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData();
        loadingData();
    }

    private void loadingData() {
        Gson gson = new Gson();
        MyUser user = BmobUser.getCurrentUser(MyUser.class);
        if (user.getLimitArea() != null) {
            List<LatLng> area = gson.fromJson(user.getLimitArea(), new TypeToken<List<LatLng>>() {
            }.getType());
            drawLimit(area);
        }

    }

    void initData() {
        //侧边栏初始化
        nav_view.inflateMenu(R.menu.drawer_menu_boss);
        nav_view.setNavigationItemSelectedListener(this);
        fab_see.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fab_menu.close(true);
                query();
                loadingData();
            }
        });
        tv_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fab_menu.setVisibility(View.VISIBLE);
                rl_hint_bar.setVisibility(View.GONE);
                if (limits.size() >= 3) {
                    Gson gson = new Gson();
                    String s = gson.toJson(limits);
                    MyUser user = BmobUser.getCurrentUser(MyUser.class);
                    user.setLimitArea(s);
                    user.update(new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            if (e == null) {
                                ToastUtil.show(BossActivity.this, "设定打卡区域成功");
                            } else {
                                ToastUtil.show(BossActivity.this, e.getMessage());
                            }
                        }
                    });
                } else {
                    ToastUtil.show(BossActivity.this, "3点成面啊");
                    limits.clear();
                    baiduMap.clear();
                }
            }
        });
        tv_undo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                limits.remove(limits.size() - 1);
                if (limits.size() == 0) {
                    tv_undo.setVisibility(View.GONE);
                }
                baiduMap.clear();
                for (LatLng latLng : limits) {
                    addMarker(latLng);
                }
                drawLimit(limits);
            }
        });
        fab_limit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                baiduMap.clear();
                limits.clear();
                fab_menu.close(true);
                fab_menu.setVisibility(View.GONE);
                tv_undo.setVisibility(View.GONE);
                rl_hint_bar.setVisibility(View.VISIBLE);
            }
        });
        baiduMap.setOnMapClickListener(new BaiduMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                setLimit(latLng);
            }

            @Override
            public boolean onMapPoiClick(MapPoi mapPoi) {
                setLimit(mapPoi.getPosition());
                return true;
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

                ClockInUtil.showOnUi(BossActivity.this, item, tv_name, tv_time, imageView);
                customDialog = new CustomDialog(BossActivity.this, view, R.style.Theme_Dialog);
                customDialog.show();
                return false;
            }
        });
    }

    /**
     * 设置打卡限制
     */
    private void setLimit(LatLng latLng) {
        if (rl_hint_bar.isShown()) {
            addMarker(latLng);
            limits.add(latLng);
            if (limits.size() >= 1) {
                tv_undo.setVisibility(View.VISIBLE);
            } else {
                tv_undo.setVisibility(View.GONE);
            }
            drawLimit(limits);
        }
    }

    //打卡区域
    private void drawLimit(List<LatLng> list) {
        if (list.size() >= 3) {
            OverlayOptions ooPolygon = new PolygonOptions().points(list)
                    .stroke(new Stroke(5, 0x88FFFF00)).fillColor(0x88FFFF00);
            baiduMap.addOverlay(ooPolygon);
        } else {
//            ToastUtil.show(BossActivity.this, "必须大于等于3个点");
        }
    }

    /**
     * 查询打卡
     */
    private void query() {
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

    void addMarker(LatLng latLng) {
        BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory.fromResource(R.drawable.ic_clock_in);
        OverlayOptions icon = new MarkerOptions().position(latLng).icon(bitmapDescriptor).animateType(MarkerOptions.MarkerAnimateType.grow);
        baiduMap.addOverlay(icon);
    }


    @Override
    List<FloatingActionButton> addFab() {
        List<FloatingActionButton> buttons = new ArrayList<>();
        fab_see = createFAB(R.color.colorPrimary, R.color.colorPrimaryDark, R.drawable.ic_leave, "查看");
        fab_limit = createFAB(R.color.colorPrimary, R.color.colorPrimaryDark, R.drawable.ic_leave, "标注打卡区域");
        buttons.add(fab_see);
        buttons.add(fab_limit);
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

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_clock_in:
                dialog.show("正在加载中");
                ClockInUtil.query(ClockIn.class, new FindListener<ClockIn>() {
                    @Override
                    public void done(List<ClockIn> clockIns, BmobException e) {
                        if (e == null) {
                            startActivity(ClockInShowActivity.newInstance(BossActivity.this, clockIns));
                            dialog.dismiss();
                        }
                    }
                });
                break;
            case R.id.menu_ask_for_leave:
                dialog.show("正在加载中");
                ClockInUtil.query(NoteForLeave.class, new FindListener<NoteForLeave>() {
                    @Override
                    public void done(List<NoteForLeave> notes, BmobException e) {
                        if (e == null) {
                            startActivity(LeaveShowActivity.newInstance(BossActivity.this, notes));
                            dialog.dismiss();
                        }
                    }
                });
                break;
            case R.id.nav_quit:
                BmobUser.logOut();   //清除缓存用户对象
                startActivity(ChooseActivity.class);
                finish();
                break;

            default:

                break;

        }
        return true;
    }
}
