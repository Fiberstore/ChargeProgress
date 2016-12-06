package com.example.ly.animationdemo.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by ly on 2016/12/1.
 */

public class RectFTest extends View {

    private Paint mPaint;
    public RectFTest(Context context) {
        this(context,null);
    }

    public RectFTest(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public RectFTest(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setBackgroundColor(0xFFFF6347);
        initPaint();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);


        RectF rectF = new RectF(100,100,800,400);
        // 绘制背景矩形
        mPaint.setStrokeWidth(5);
        canvas.drawRect(rectF,mPaint);

        // 绘制圆弧
        mPaint.setColor(Color.BLUE);
        canvas.drawArc(rectF,0,90,false,mPaint);


        RectF rectF2 = new RectF(100,600,800,900);
        // 绘制背景矩形
        mPaint.setColor(Color.WHITE);
        canvas.drawRect(rectF2,mPaint);

        // 绘制圆弧
        mPaint.setColor(Color.BLUE);
        canvas.drawArc(rectF2,160,260,true,mPaint);

        canvas.rotate(-50);


    }


    private void initPaint() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setDither(true);
        mPaint.setColor(0xffffffff);
        mPaint.setStrokeWidth(5);
    }
}
