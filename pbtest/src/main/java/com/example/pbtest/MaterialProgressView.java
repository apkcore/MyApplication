package com.example.pbtest;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

public class MaterialProgressView extends ImageView {
    private MaterialProgressDrawable mProgress;
    private int[] colors = {
            0xFFFF0000, 0xFFFF7F00, 0xFFFFFF00, 0xFF00FF00
            , 0xFF00FFFF, 0xFF0000FF, 0xFF8B00FF};
    private static final int CIRCLE_BG_LIGHT = 0xFFFAFAFA;

    public MaterialProgressView(Context context) {
        super(context);
        init(context);
    }

    public MaterialProgressView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public MaterialProgressView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        mProgress = new MaterialProgressDrawable(context, this);
        this.setImageDrawable(mProgress);
        //背景
        mProgress.setBackgroundColor(CIRCLE_BG_LIGHT);
        //颜色
        mProgress.setColorSchemeColors(colors);
        //尺寸
        mProgress.updateSizes(MaterialProgressDrawable.DEFAULT);
        //旋转角度，0-1
        mProgress.setProgressRotation(0f);
        //圆环范围，0-1
        mProgress.setStartEndTrim(0f, 1f);
        //箭头大小，0-1
        mProgress.setArrowScale(1f);
        //透明度，0-255
        mProgress.setAlpha(255);
        mProgress.showArrow(true);
        setVisibility(this.getVisibility());
    }

    public void setBackgroundColor(int color) {
        mProgress.setBackgroundColor(color);
    }

    public void setColorSchemeColors(int... colors) {
        mProgress.setColorSchemeColors(colors);
    }

    public void setVisibility(int visibility) {
        if (visibility == View.VISIBLE) {
            mProgress.start();
        } else {
            mProgress.stop();
        }
    }
}
