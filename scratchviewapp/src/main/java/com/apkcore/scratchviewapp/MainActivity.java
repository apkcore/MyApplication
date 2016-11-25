package com.apkcore.scratchviewapp;

import android.os.Bundle;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.scroll)
    NestedScrollView mScroll;
    @BindView(R.id.bt1)
    Button mBt1;
    @BindView(R.id.bt2)
    Button mBt2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        mBt1.setOnClickListener(this);
        mBt2.setOnClickListener(this);
    }

    public void intro() {
        BottomSheetBehavior behavior = BottomSheetBehavior.from(findViewById(R.id.scroll));
        Log.d("bsb3",behavior.getState()+"");
        if (behavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
            behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        } else {
            behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bt1:
                intro();
                break;
            case R.id.bt2:
                View recyclerView = LayoutInflater.from(this).inflate(R.layout.rebviewd, null);
                final BottomSheetDialog dialog = new BottomSheetDialog(this);
                dialog.setContentView(recyclerView);
                dialog.show();
                break;
            default:
                break;
        }
    }
}
