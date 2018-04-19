package com.stxr.clockin.ui;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.stxr.clockin.R;
import com.stxr.clockin.entity.ClockIn;
import com.stxr.clockin.entity.MyUser;
import com.stxr.clockin.utils.ToastUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

/**
 * Created by stxr on 2018/4/15.
 */

public class EmployerActivity extends BaseActivity implements SensorEventListener {
    private LocationClient mLocationClient = null;
    private MyLocationListener myListener = new MyLocationListener();
    private BaiduMap mBaiduMap;
    private double mCurrentLat = 0.0;
    private double mCurrentLon = 0.0;
    private boolean isFirstLoc = true;
    //得到方向传感器
    private SensorManager sensorManager;
    private MyLocationData locData;

    @BindView(R.id.bmapView)
    MapView mMapView;
    private int mCurrentDirection = 0;
    private double lastX;
    private float mCurrentAccracy;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employer);
        ButterKnife.bind(this);
        if (getSupportActionBar() != null) {
            //隐藏标题栏
            getSupportActionBar().hide();
        }
        initData();
    }

    private void initData() {
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        sensorManager.registerListener(this,
                sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION)
                , SensorManager.SENSOR_DELAY_UI);

        mMapView.showZoomControls(false);
        mBaiduMap = mMapView.getMap();
        mLocationClient = new LocationClient(getApplicationContext());
        mLocationClient.registerLocationListener(myListener);
        option();
        mLocationClient.start();
        // 开启定位图层
        mBaiduMap.setMyLocationEnabled(true);
        mBaiduMap.setMapStatus(MapStatusUpdateFactory.zoomTo(20));
        //设置定位样式
        MyLocationConfiguration config = new MyLocationConfiguration(MyLocationConfiguration.LocationMode.FOLLOWING,
                true,
                null);
        mBaiduMap.setMyLocationConfiguration(config);
    }

    //签到
    @OnClick(R.id.fab_clockIn)
    void clockIn() {
        ClockIn clockIn = new ClockIn();
        clockIn.setLatitude(mCurrentLat);
        clockIn.setLongitude(mCurrentLon);
        clockIn.setUser(BmobUser.getCurrentUser(MyUser.class));
        clockIn.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                if (e == null) {
                    Snackbar.make(mMapView, "打卡成功", Snackbar.LENGTH_SHORT).show();
                } else {
                    ToastUtil.show(EmployerActivity.this, e.getMessage());
                }
            }
        });
    }


    void option() {
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true); // 打开gps
        option.setCoorType("bd09ll"); // 设置坐标类型
        option.setScanSpan(1000);
        mLocationClient.setLocOption(option);
    }

    @Override
    protected void onDestroy() {
        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
        mMapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
        mMapView.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        double x = sensorEvent.values[SensorManager.DATA_X];
        if (Math.abs(x - lastX) > 1.0) {
            mCurrentDirection = (int) x;
            locData = new MyLocationData.Builder()
                    .accuracy(mCurrentAccracy)
                    // 此处设置开发者获取到的方向信息，顺时针0-360
                    .direction(mCurrentDirection).latitude(mCurrentLat)
                    .longitude(mCurrentLon).build();
            mBaiduMap.setMyLocationData(locData);
        }
        lastX = x;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }


    public class MyLocationListener extends BDAbstractLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {
            // map view 销毁后不在处理新接收的位置
            if (location == null || mMapView == null) {
                return;
            }
            mCurrentLat = location.getLatitude();
            mCurrentLon = location.getLongitude();
            mCurrentAccracy = location.getRadius();
            locData = new MyLocationData.Builder()
                    .accuracy(mCurrentAccracy)
                    // 此处设置开发者获取到的方向信息，顺时针0-360
                    .direction(mCurrentDirection).latitude(location.getLatitude())
                    .longitude(location.getLongitude()).build();

            if (isFirstLoc) {
                isFirstLoc = false;
                LatLng ll = new LatLng(location.getLatitude(),
                        location.getLongitude());
                MapStatus.Builder builder = new MapStatus.Builder();
                builder.target(ll).zoom(18.0f);
                mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
            }
        }
    }
}
