package com.stxr.clockin.view;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

/**
 * Created by stxr on 2018/4/29.
 */

public class CustomDialog extends Dialog {
    public CustomDialog(Context context, Object layout,int themeResId) {
        this(context, layout, WindowManager.LayoutParams.WRAP_CONTENT
                , WindowManager.LayoutParams.WRAP_CONTENT, themeResId,Gravity.CENTER);
    }

    public CustomDialog(Context context, Object layout, int height, int width, int themeResId, int gravity, int anim) {
        super(context, themeResId);
        if (layout instanceof Integer) {
            setContentView((int) layout);
        } else if (layout instanceof View) {
            setContentView((View) layout);
        }
        Window window = getWindow();
        WindowManager.LayoutParams attributes = window.getAttributes();
        attributes.height = height;
        attributes.width = width;
        attributes.gravity = gravity;
        window.setAttributes(attributes);
    }

    public CustomDialog(Context context, Object layout, int height, int width, int themeResId,int gravvity) {
        this(context, layout, height, width, themeResId, gravvity, 0);
    }
}
