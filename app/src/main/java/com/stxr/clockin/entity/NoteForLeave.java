package com.stxr.clockin.entity;

import cn.bmob.v3.BmobObject;

/**
 * Created by stxr on 2018/4/26.
 */

public class NoteForLeave extends BmobObject{
    private String reason;
    private String startTime;
    private String endTime;

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }
}
