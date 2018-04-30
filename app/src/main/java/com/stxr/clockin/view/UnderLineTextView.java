package com.stxr.clockin.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by stxr on 2018/4/29.
 */

public class UnderLineTextView extends android.support.v7.widget.AppCompatTextView {

    private Paint mPaint1, paint;
    private Canvas canvas;


    public UnderLineTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        paint = new Paint();
        paint.setColor(Color.GRAY);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(4);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawLine(0, getMeasuredHeight(), getMeasuredWidth(), getMeasuredHeight(), paint);
        canvas.save();
        super.onDraw(canvas);
        canvas.restore();
    }
}
