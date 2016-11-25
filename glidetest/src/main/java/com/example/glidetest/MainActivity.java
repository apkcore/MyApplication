package com.example.glidetest;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ImageView img = (ImageView) findViewById(R.id.img);
//        Glide.with(this).load("http://img2.3lian.com/2014/f6/173/d/51.jpg").into(img);
//        Glide.with(this).load(R.mipmap.ic_launcher).into(img);
//        Glide.with(this).load("http://img2.3lian.com/2014/f6/173/d/55.jpg").placeholder(R.mipmap.ic_launcher).into(img);
        Glide.with(this).load("http://img1.3lian.com/2015/w4/17/d/64.gif").placeholder(R.mipmap.ic_launcher).animate(R.anim.anim).into(img);
    }
}
