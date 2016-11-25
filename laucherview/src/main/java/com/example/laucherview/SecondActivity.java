package com.example.laucherview;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.widget.ImageView;

import com.example.laucherview.base.BaseActivity;
import com.example.laucherview.widget.MagicCircle;

import immortalz.me.library.TransitionsHeleper;
import immortalz.me.library.bean.InfoBean;
import immortalz.me.library.method.ColorShowMethod;

public class SecondActivity extends BaseActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        TransitionsHeleper.getInstance().setShowMethod(new ColorShowMethod(R.color.bg_purple, R.color.bg_teal) {
            @Override
            public void loadCopyView(InfoBean bean, ImageView copyView) {
                AnimatorSet set = new AnimatorSet();
                set.playTogether(
                        ObjectAnimator.ofFloat(copyView, "alpha", 1f, 0f),
                        ObjectAnimator.ofFloat(copyView, "scaleX", 1.5f, 1f),
                        ObjectAnimator.ofFloat(copyView, "scaleY", 1.5f, 1f)
                );
                set.setInterpolator(new AccelerateInterpolator());
                set.setDuration(duration / 4 * 5).start();
            }

            @Override
            public void loadTargetView(InfoBean bean, ImageView targetView) {
                super.loadTargetView(bean, targetView);
            }
        }).show(this, null);

        findViewById(R.id.bt).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MagicCircle) findViewById(R.id.circle3)).startAnimation();
            }
        });
    }
}
