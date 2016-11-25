package com.example.laucherview.base;

import android.support.v7.app.AppCompatActivity;

import immortalz.me.library.TransitionsHeleper;

public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onDestroy() {
        super.onDestroy();
        TransitionsHeleper.unBind(this);
    }
}
