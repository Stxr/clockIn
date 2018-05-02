package com.stxr.clockin.ui;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.model.LatLngBounds;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.stxr.clockin.R;
import com.stxr.clockin.entity.ClockIn;
import com.stxr.clockin.entity.MyUser;
import com.stxr.clockin.utils.ToastUtil;
import com.stxr.clockin.view.CustomLoadingDialog;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.bmob.v3.BmobUser;

/**
 * Created by stxr on 2018/4/27.
 */

public abstract class BaseMapActivity extends BaseActivity implements SensorEventListener {

    private static long FIRST_TIME = 0;
    //    private List<FloatingActionButton> fabs;
    //菜单
    @BindView(R.id.fab_menu)
    FloatingActionMenu fab_menu;
    @BindView(R.id.bmapView)
    MapView mapView;
    //底部提示栏
    @BindView(R.id.rl_hint_bar)
    RelativeLayout rl_hint_bar;
    @BindView(R.id.tv_confirm)
    TextView tv_confirm;
    @BindView(R.id.tv_undo)
    TextView tv_undo;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawer;
    @BindView(R.id.nav_view)
    NavigationView nav_view;

    protected BaiduMap baiduMap;
    protected LocationClient locationClient;
    //当前所在位置
    protected BDLocation currentLocation;
    private MyLocationListener locationListener = new MyLocationListener();
    private boolean isFirstLoc=true;


    private MyLocationData locData;
    private int currentDirection = 0;
    private double lastX;
    private SensorManager sensorManager;


    //添加菜单
    abstract List<FloatingActionButton> addFab();

    /**
     *  设置初始聚焦的坐标，null为当前定位的坐标。
     */
    abstract LatLng firstFocusOnMap();

    /**
     * 设置初始缩放的坐标
     * @return
     */
    abstract float zoom();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        if (getSupportActionBar() != null) {
            //隐藏标题栏
            getSupportActionBar().hide();
            setSupportActionBar(toolbar);
        }
        ButterKnife.bind(this);
        initData();

    }

    private void initData() {
        //左边菜单头部
        View view = LayoutInflater.from(this).inflate(R.layout.nav_header_main, nav_view,false);
        TextView textView = view.findViewById(R.id.tv_name1);
        TextView tv_character = view.findViewById(R.id.tv_character);
        textView.setText(BmobUser.getCurrentUser(MyUser.class).getUsername());
        tv_character.setText(BmobUser.getCurrentUser(MyUser.class).isBoss()?"管理员":"员工");
        nav_view.addHeaderView(view);

        //显示左上角汉堡按钮
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        //不显示放大缩小按钮
        mapView.showZoomControls(false);
        //实例化地图
        baiduMap = mapView.getMap();
        //得到当前定位
        locationClient = new LocationClient(getApplicationContext());
        locationClient.registerLocationListener(locationListener);
        //设置定位样式
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true); // 打开gps
        option.setCoorType("bd09ll"); // 设置坐标类型
        option.setScanSpan(1000);
        locationClient.setLocOption(option);
        //开始定位
        locationClient.start();

        baiduMap.setMyLocationEnabled(true);

        //设置定位样式
        MyLocationConfiguration config = new MyLocationConfiguration(MyLocationConfiguration.LocationMode.FOLLOWING,
                true,
                null);
        baiduMap.setMyLocationConfiguration(config);
        baiduMap.setMapStatus(MapStatusUpdateFactory.zoomTo(zoom()));

        //设置陀螺仪
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        sensorManager.registerListener(this,
                sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION)
                , SensorManager.SENSOR_DELAY_UI);


        //设置加载框
        dialog = new CustomLoadingDialog(this, "正在加载");
        //添加菜单
        for (FloatingActionButton button : addFab()) {
            fab_menu.addMenuButton(button);
        }
    }
    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        double x = sensorEvent.values[SensorManager.DATA_X];
        if (Math.abs(x - lastX) > 1.0) {
            currentDirection = (int) x;
            if (currentLocation != null) {
                locData = new MyLocationData.Builder()
                        .accuracy(currentLocation.getRadius())
                        // 此处设置开发者获取到的方向信息，顺时针0-360
                        .direction(currentDirection).latitude(currentLocation.getLatitude())
                        .longitude(currentLocation.getLongitude()).build();
                baiduMap.setMyLocationData(locData);
            }
        }
        lastX = x;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
    /**
     * 添加FAB菜单按钮
     *
     * @param normalColor
     * @param pressColor
     * @param image
     * @param text
     * @return
     */
    protected FloatingActionButton createFAB(int normalColor, int pressColor, int image, String text) {
        FloatingActionButton button = new FloatingActionButton(this);
        button.setColorNormalResId(normalColor);
        button.setColorPressedResId(pressColor);
        button.setImageResource(image);
        button.setLabelText(text);
        return button;
    }

    /**
     * 得到当前位置坐标
     * @return
     */
    protected LatLng getCurrentLatLng() {
        if (currentLocation != null) {
            return new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
        } else {
            return null;
        }
    }



    @Override
    protected void onDestroy() {
        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
        mapView.onPause();
    }
    @Override
    protected void onStop() {
        super.onStop();
        sensorManager.unregisterListener(this);
    }

    class MyLocationListener extends BDAbstractLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {
            // map view 销毁后不在处理新接收的位置
            if (location == null || mapView == null) {
                return;
            }
            currentLocation = location;
            locData = new MyLocationData.Builder()
                    .accuracy(location.getRadius())
                    // 此处设置开发者获取到的方向信息，顺时针0-360
                    .direction(currentDirection).latitude(location.getLatitude())
                    .longitude(location.getLongitude()).build();
            if (isFirstLoc) {
                isFirstLoc = false;
                baiduMap.setMyLocationData(locData);
                MapStatus.Builder builder = new MapStatus.Builder();
                if (firstFocusOnMap() != null) {
                    builder.target(firstFocusOnMap()).zoom(zoom());
                } else {
                    builder.target(new LatLng(location.getLatitude(),location.getLongitude())).zoom(20);
                }
                baiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
            }
        }
    }
    protected void focusCluster(List<ClockIn> clockIns) {
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        for (ClockIn clockIn : clockIns) {
            builder = builder.include(new LatLng(clockIn.getLatitude(), clockIn.getLongitude()));
        }
        LatLngBounds bounds = builder.build();
        MapStatusUpdate u = MapStatusUpdateFactory.newLatLngBounds(bounds,mapView.getWidth(),mapView.getHeight());
        baiduMap.animateMapStatus(u);
    }

    @Override
    public void onBackPressed() {
        if (System.currentTimeMillis() - FIRST_TIME < 2000) {
            finish();
        } else {
            FIRST_TIME = System.currentTimeMillis();
            ToastUtil.show(this,"再按一次返回键退出");
        }
    }
}

