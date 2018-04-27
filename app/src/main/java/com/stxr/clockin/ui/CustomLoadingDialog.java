package com.stxr.clockin.ui;

import android.app.ActionBar;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.stxr.clockin.R;

import org.w3c.dom.Text;

/**
 * Created by stxr on 2018/4/26.
 */

public class CustomLoadingDialog extends Dialog {
    private String mText;
    public CustomLoadingDialog(@NonNull Context context,String text) {
        super(context, R.style.Theme_Dialog);
        setContentView(R.layout.dialog_loading);
        mText = text;
        setCancelable(false);
    }
    public void setText(String text) {
        this.mText = text;
    }

    public void show(String text) {
        this.mText = text;
        TextView tv_loading_dialog =findViewById(R.id.tv_loading_dialog);
        tv_loading_dialog.setText(mText);
        super.show();
    }
}
