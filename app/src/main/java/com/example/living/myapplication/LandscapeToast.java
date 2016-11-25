package com.example.living.myapplication;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

public class LandscapeToast {

    public static Toast makeText(Context context, CharSequence text, int duration) {
        Toast toast = Toast.makeText(context, text, duration);
        View toastV = toast.getView();
        View vv = LayoutInflater.from(context).inflate(R.layout.toast_ll, null);
        LinearLayout layout = (LinearLayout) vv.findViewById(R.id.toast_ll);// 待旋转布局
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        layout.addView(toastV, params);
        toast.setView(vv);
        toast.setGravity(Gravity.LEFT, 64, 0);
        return toast;
    }

    public static Toast show(CharSequence text) {
        Toast toast = makeText(MyApplication.getInstance().getApplicationContext(), text, Toast.LENGTH_SHORT);
        toast.show();
        return toast;
    }
}
