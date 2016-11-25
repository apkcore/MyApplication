package com.example.living.myapplication;

import android.app.Application;

/**
 * Created by living on 2016/8/30.
 */
public class MyApplication extends Application {
    private static MyApplication instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }

    public static MyApplication getInstance() {
        return instance;
    }
}
