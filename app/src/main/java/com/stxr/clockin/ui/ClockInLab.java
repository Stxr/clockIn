package com.stxr.clockin.ui;

import android.content.Context;
import android.os.Environment;

import com.stxr.clockin.entity.ClockIn;

import java.io.File;

/**
 * Created by stxr on 2018/4/22.
 */

public class ClockInLab {
    public static File getPhotoFile(Context context, ClockIn clockIn) {
        File dir = context.getFilesDir();
        if (dir == null) {
            return null;
        }
        return new File(dir, clockIn.getPhotoName());
    }

    public static String getPhotoPath(Context context, ClockIn clockIn) {
        return context.getFilesDir().getAbsolutePath()+File.separator+clockIn.getPhotoName();
    }
}
