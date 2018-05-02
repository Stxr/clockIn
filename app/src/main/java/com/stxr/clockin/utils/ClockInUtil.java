package com.stxr.clockin.utils;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.stxr.clockin.entity.ClockIn;
import com.stxr.clockin.entity.MyUser;
import com.stxr.clockin.ui.BossActivity;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListener;

/**
 * Created by stxr on 2018/4/30.
 */

public class ClockInUtil {

    public static void showOnUi(Context context, ClockIn clockIn, final TextView name, TextView time, ImageView imageView) {
        BmobQuery<MyUser> user = new BmobQuery<>();

        user.getObject(clockIn.getUser().getObjectId(), new QueryListener<MyUser>() {
            @Override
            public void done(MyUser myUser, BmobException e) {
                if (e == null) {
                    name.setText(myUser.getUsername());
                }
            }
        });
        time.setText(clockIn.getUpdatedAt());
        Glide.with(context).load(clockIn.getPhotoFile().getUrl()).into(imageView);
    }

    public static <T extends BmobObject> void query(Class<T> tClass ,FindListener<T> listener) {
        BmobQuery<T> queryClockIn = new BmobQuery<>();
        boolean isCache = queryClockIn.hasCachedResult(tClass);
        //降序排列
        queryClockIn.order("-createdAt");
        if (isCache) {
            queryClockIn.setCachePolicy(BmobQuery.CachePolicy.CACHE_ELSE_NETWORK);   // 如果有缓存的话，则设置策略为CACHE_ELSE_NETWORK
        } else {
            queryClockIn.setCachePolicy(BmobQuery.CachePolicy.NETWORK_ELSE_CACHE);   // 如果没有缓存的话，则设置策略为NETWORK_ELSE_CACHE
        }
        queryClockIn.setMaxCacheAge(TimeUnit.DAYS.toMillis(1));
        queryClockIn.findObjects(listener);
    }
    public static <T extends BmobObject> void query(Class<T> tClass,String key,Object value,FindListener<T> listener) {
        BmobQuery<T> queryClockIn = new BmobQuery<>();
        boolean isCache = queryClockIn.hasCachedResult(tClass);
        queryClockIn.addWhereEqualTo(key, value);
        //降序排列
        queryClockIn.order("-createdAt");
        if (isCache) {
            queryClockIn.setCachePolicy(BmobQuery.CachePolicy.CACHE_ELSE_NETWORK);   // 如果有缓存的话，则设置策略为CACHE_ELSE_NETWORK
        } else {
            queryClockIn.setCachePolicy(BmobQuery.CachePolicy.NETWORK_ELSE_CACHE);   // 如果没有缓存的话，则设置策略为NETWORK_ELSE_CACHE
        }
        queryClockIn.setMaxCacheAge(TimeUnit.DAYS.toMillis(1));
        queryClockIn.findObjects(listener);
    }

    public static <T extends BmobObject>void  query(Class<T> tClass,String id,QueryListener<T> listener) {
        BmobQuery<T> user = new BmobQuery<>();
        user.getObject(id, listener);
    }

}
