package com.stxr.clockin.entity;

import cn.bmob.v3.BmobObject;

/**
 * Created by stxr on 2018/4/19.
 */

public class ClockIn extends BmobObject {
    private double latitude;
    private double Longitude;
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
}
