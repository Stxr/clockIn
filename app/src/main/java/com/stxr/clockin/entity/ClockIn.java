package com.stxr.clockin.entity;

import java.util.UUID;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;

/**
 * Created by stxr on 2018/4/19.
 * 签到
 */

public class ClockIn extends BmobObject {
    private double latitude;
    private double Longitude;
    private String photoName;
    private BmobFile photoFile;


    public ClockIn() {
        if (getObjectId() == null) {
            setObjectId(UUID.randomUUID().toString());
        }
    }

    private MyUser user;

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return Longitude;
    }

    public void setLongitude(double longitude) {
        Longitude = longitude;
    }

    public MyUser getUser() {
        return user;
    }

    public void setUser(MyUser user) {
        this.user = user;
    }


    public String getPhotoName() {
        return "IMG_" + getObjectId() + ".jpg";
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
                ", Longitude=" + Longitude +
                ", photoName='" + photoName + '\'' +
                ", photoFile=" + photoFile +
                ", user=" + user +
                '}';
    }

}
