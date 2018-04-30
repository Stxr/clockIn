package com.stxr.clockin.utils;

import com.baidu.mapapi.model.LatLng;

import java.util.List;

/**
 * Created by stxr on 2018/4/30.
 */

public class AreaUtil {
    //判断一个点是否在多边形内部
    public static boolean withinArea(LatLng testPoint, List<LatLng> area) {
        int crossings = 0;
        for (int i = 0;i < area.size();i++) {
            //斜率
            double slope = (area.get(i + 1).latitude - area.get(i).latitude) / (area.get(i + 1).longitude - area.get(i).longitude);
            boolean cond1 = (area.get(i).longitude <= testPoint.longitude) && (area.get(i + 1).longitude > testPoint.longitude);
            boolean cond2 = (area.get(i + 1).longitude <= testPoint.longitude) && (area.get(i).longitude > testPoint.longitude);
            boolean above = testPoint.latitude < slope * (testPoint.longitude - area.get(i).longitude) + area.get(i).latitude;
            if((cond1||cond2)&&above) crossings++;
        }
        //是奇数个点则说明在多边形内部
        return (crossings % 2) != 0;
    }
}
