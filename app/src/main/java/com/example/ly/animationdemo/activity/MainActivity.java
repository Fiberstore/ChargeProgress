package com.example.ly.animationdemo.activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.ly.animationdemo.R;
import com.example.ly.animationdemo.widget.ZhiMaXinYong;

public class MainActivity extends AppCompatActivity {

    ZhiMaXinYong zhimaxinyong;
    int i = 0;
    boolean flag = true;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                zhimaxinyong.setCurrentNumAnim(msg.arg1);
                flag = !flag;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        zhimaxinyong = (ZhiMaXinYong) findViewById(R.id.zhimaxinyong);
        zhimaxinyong.setCurrentNumAnim(i = (i + 450) % 500);


        Button btnChange = (Button) findViewById(R.id.btnChange);
        btnChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, ViewTestActivity.class));
            }
        });


        Button btnDraw = (Button) findViewById(R.id.btnDraw);
        btnDraw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (flag) {
                    i = 0;
                } else {
                    i = (i + 450) % 500;
                }
                Message message = mHandler.obtainMessage();
                message.what = 1;
                message.arg1 = i;
                mHandler.sendMessage(message);
            }
        });
    }
}
