package com.example.pbtest;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class MainActivity extends AppCompatActivity {
//
    private int[] colors = {
            0xFFFF0000};
//
//    private final int CIRCLE_BG_LIGHT = 0xFFFAFAFA;

    private MaterialProgressView imageView;

//    private MaterialProgressDrawable mProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageView = (MaterialProgressView) findViewById(R.id.img);
        imageView.setBackgroundColor(Color.YELLOW);
        imageView.setColorSchemeColors(colors);
//        imageView.setVisibility(View.VISIBLE);
//        mProgress = new MaterialProgressDrawable(this, imageView);
//        imageView.setImageDrawable(mProgress);
//
//        //背景
//        mProgress.setBackgroundColor(CIRCLE_BG_LIGHT);
//        //颜色
////        int[] colors = {0xFFFF0000, 0xFF00FF00, 0xFF0000FF};
//        mProgress.setColorSchemeColors(colors);
//        //尺寸
//        mProgress.updateSizes(MaterialProgressDrawable.DEFAULT);
//        //旋转角度，0-1
//        mProgress.setProgressRotation(0f);
//        //圆环范围，0-1
//        mProgress.setStartEndTrim(0f, 1f);
//        //箭头大小，0-1
//        mProgress.setArrowScale(0f);
//        //透明度，0-255
//        mProgress.setAlpha(100);
//
//        //③start
//        mProgress.start();
//
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                imageView.setVisibility(View.VISIBLE);
            }
        }, 5000);
//        imageView = (ImageView) findViewById(R.id.image);

//        mProgress = new MaterialProgressDrawable(this, imageView);
//
//        mProgress.setBackgroundColor(CIRCLE_BG_LIGHT);
//        //圈圈颜色,可以是多种颜色
//        mProgress.setColorSchemeColors(colors);
//        //设置圈圈的各种大小
//        mProgress.updateSizes(MaterialProgressDrawable.LARGE);
//
//        imageView.setImageDrawable(mProgress);
    }

//    private ValueAnimator valueAnimator;
//
//    boolean start = false;
//
//    boolean visable = false;
//
//    public void visable(View v) {
//        if (valueAnimator == null) {
//            valueAnimator = valueAnimator.ofFloat(0f, 1f);
//            valueAnimator.setDuration(600);
//            valueAnimator.setInterpolator(new DecelerateInterpolator());
//            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
//                @Override
//                public void onAnimationUpdate(ValueAnimator animation) {
//                    float n = (float) animation.getAnimatedValue();
//                    //圈圈的旋转角度
//                    mProgress.setProgressRotation(n * 0.5f);
//                    //圈圈周长，0f-1F
//                    mProgress.setStartEndTrim(0f, n * 0.8f);
//                    //箭头大小，0f-1F
//                    mProgress.setArrowScale(n);
//                    //透明度，0-255
//                    mProgress.setAlpha((int) (255 * n));
//                }
//            });
//            valueAnimator.addListener(new AnimatorListenerAdapter() {
//                @Override
//                public void onAnimationEnd(Animator animation) {
//                    super.onAnimationEnd(animation);
//                    visable = true;
//                }
//            });
//        }
//
//        if (!valueAnimator.isRunning()) {
//            if (!visable) {
//                //是否显示箭头
//                mProgress.showArrow(true);
//                valueAnimator.start();
//            } else {
//                Toast.makeText(this, "看不见吗？", Toast.LENGTH_SHORT).show();
//            }
//        }
//    }
//
//    public void start(View v) {
//        if (visable) {
//            if (!start) {
//                mProgress.start();
//                start = true;
//            } else {
//                Toast.makeText(this, "已经在转了", Toast.LENGTH_SHORT).show();
//            }
//        } else {
//            Toast.makeText(this, "还没有显示", Toast.LENGTH_SHORT).show();
//        }
//    }
//
//    public void stop(View v) {
//        if (start) {
//            mProgress.stop();
//            start = false;
//            visable = false;
//        } else {
//            Toast.makeText(this, "又没有转", Toast.LENGTH_SHORT).show();
//        }
//    }
}
