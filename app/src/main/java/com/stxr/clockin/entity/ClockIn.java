package com.stxr.clockin.entity;

import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.model.LatLng;
import com.stxr.clockin.R;
import com.stxr.clockin.utils.clusterutil.clustering.ClusterItem;

import java.util.UUID;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;

/**
 * Created by stxr on 2018/4/19.
 * 签到
 */

public class ClockIn extends BmobObject implements ClusterItem{
    private double latitude;
    private double longitude;
    private String photoName;
    private BmobFile photoFile;


    public ClockIn() {
        if (getObjectId() == null) {
            setObjectId(UUID.randomUUID().toString());
        }
        photoName = "IMG_" + getObjectId() + ".jpg";
    }

    private MyUser user;

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public MyUser getUser() {
        return user;
    }

    public void setUser(MyUser user) {
        this.user = user;
    }

    public String getPhotoName() {
        return photoName;
    }

    public BmobFile getPhotoFile() {
        return photoFile;
    }

    public void setPhotoFile(BmobFile photoFile) {
        this.photoFile = photoFile;
    }

    @Override
    public String toString() {
        return "ClockIn{" +
                "latitude=" + latitude +
                ", longitude=" + longitude +
                ", photoName='" + photoName + '\'' +
                ", photoFile=" + photoFile +
                ", user=" + user +
                '}';
    }

    @Override
    public LatLng getPosition() {
        return new LatLng(latitude,longitude);
    }

    @Override
    public BitmapDescriptor getBitmapDescriptor() {
        return BitmapDescriptorFactory.fromResource(R.drawable.ic_clock_in);
    }

    public void setPhotoName(String photoName) {
        this.photoName = photoName;
    }
}
