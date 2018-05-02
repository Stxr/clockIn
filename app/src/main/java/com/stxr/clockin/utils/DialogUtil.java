package com.stxr.clockin.utils;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.stxr.clockin.R;
import com.stxr.clockin.view.CustomDialog;

/**
 * Created by stxr on 2018/5/2.
 */

public class DialogUtil {
    public static CustomDialog showImage(Context context, String url){
        View view = LayoutInflater.from(context).inflate(R.layout.image,null );
        ImageView imageView = view.findViewById(R.id.image_dialog);
        Glide.with(context)
                .load(url)
                .apply(new RequestOptions().override(1080,1920))
                .into(imageView);
       return new CustomDialog(context,view, R.style.Theme_Dialog);
    }
}
