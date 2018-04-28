package com.stxr.clockin.ui;

import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.model.LatLng;
import com.stxr.clockin.R;
import com.stxr.clockin.utils.clusterutil.clustering.ClusterItem;

/**
 * Created by stxr on 2018/4/28.
 * 标注打卡的点
 */

public class MyItem implements ClusterItem {
    private final LatLng position;

    public MyItem(LatLng position) {
        this.position = position;
    }

    @Override
    public LatLng getPosition() {
        return position;
    }

    @Override
    public BitmapDescriptor getBitmapDescriptor() {
        return BitmapDescriptorFactory.fromResource(R.drawable.ic_clock_in);
    }
}
