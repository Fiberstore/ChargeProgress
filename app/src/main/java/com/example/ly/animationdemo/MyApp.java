package com.example.ly.animationdemo;

import android.app.Application;

import com.socks.library.KLog;

/**
 * Created by ly on 2016/12/3.
 */

public class MyApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        KLog.init(true,"liji_animaition_demo");
    }
}
