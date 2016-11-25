package com.example.laucherview;

import android.os.Bundle;
import android.view.View;

import com.example.laucherview.base.BaseActivity;

import immortalz.me.library.TransitionsHeleper;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.load_view).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TransitionsHeleper.startAcitivty(MainActivity.this,SecondActivity.class,findViewById(R.id.load_view));
            }
        });
    }
}
