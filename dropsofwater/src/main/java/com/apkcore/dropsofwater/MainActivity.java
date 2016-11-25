package com.apkcore.dropsofwater;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.apkcore.dropsofwater.weight.WaveHelper;
import com.apkcore.dropsofwater.weight.WaveView;

public class MainActivity extends AppCompatActivity {

    private WaveHelper mWaveHelper;
    private int mBorderColor = Color.parseColor("#44FFFFFF");
    private int mBorderWidth = 10;
    private WaveView mWaveView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mWaveView = (WaveView) findViewById(R.id.wave);
        mWaveHelper = new WaveHelper(mWaveView);
    }


    @Override
    protected void onPause() {
        super.onPause();
        mWaveHelper.cancel();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mWaveView.setWaveColor(
                Color.parseColor("#f16d7a"));
//        mBorderColor = Color.parseColor("#f16d7a");
//        mWaterWaveView.setBorder(mBorderWidth, mBorderColor);
        mWaveHelper.start();
    }
}
