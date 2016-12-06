package com.framelibrary.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

/**
 * 框架基类
 */
public abstract class FrameBaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initVariables();
        initViews();
        loadData();
    }

    public abstract void initVariables();

    public abstract void initViews();

    public abstract void loadData();
}
