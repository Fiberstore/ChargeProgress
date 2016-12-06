package com.example.ly.animationdemo.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.example.chargingprogress.ChargingProgess;
import com.example.ly.animationdemo.R;

public class ViewTestActivity extends AppCompatActivity {

    ChargingProgess chargingprigressView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_test);
        chargingprigressView = (ChargingProgess) findViewById(R.id.chargingprigressView);

        Button btnACDraw = (Button) findViewById(R.id.btnACDraw);
        btnACDraw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chargingprigressView.closeAnimation();
                chargingprigressView.setACAnimation();
            }
        });

        Button btnDCDraw = (Button) findViewById(R.id.btnDCDraw);
        btnDCDraw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chargingprigressView.closeAnimation();
                chargingprigressView.setDCAnimation(80);
            }
        });
    }


}
