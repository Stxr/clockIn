package com.stxr.clockin.ui;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;

import com.stxr.clockin.view.CustomLoadingDialog;

/**
 * Created by stxr on 2018/4/15.
 */

public abstract class BaseActivity extends AppCompatActivity {
    protected final String TAG = this.getClass().getSimpleName();
    private int requestCode=32;
    protected CustomLoadingDialog dialog;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        permission();
    }


    protected void startActivity(Class<?> clazz) {
        Intent intent = new Intent(this, clazz);
        startActivity(intent);
    }
    void permission() {

        final String[] permissions = {
                //增加定位权限
                Manifest.permission.ACCESS_FINE_LOCATION
        };
        if (!checkPermission(this,permissions)) {
            ActivityCompat.requestPermissions(this, permissions, requestCode);
        }
    }

    /**
     * 检查权限是否拥有
     * @param context
     * @param permissions 要申请的权限
     * @return true:所有要申请的权限都有 false:缺少要申请的权限
     */
    boolean checkPermission(Context context, String[] permissions) {
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_DENIED) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}

