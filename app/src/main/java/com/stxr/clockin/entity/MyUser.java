package com.stxr.clockin.entity;

import cn.bmob.v3.BmobUser;

/**
 * Created by stxr on 2018/4/16.
 */

public class MyUser extends BmobUser {
    private boolean isBoss;
    private MyUser MyBoss;
    private String limitArea;
    public boolean isBoss() {
        return isBoss;
    }

    public void setBoss(boolean boss) {
        isBoss = boss;
    }

    public String getLimitArea() {
        return limitArea;
    }

    public void setLimitArea(String limitArea) {
        this.limitArea = limitArea;
    }

    public MyUser getMyBoss() {
        return MyBoss;
    }

    public void setMyBoss(MyUser myBoss) {
        MyBoss = myBoss;
    }

    @Override
    public String toString() {
        return super.getUsername();
    }
}
