package com.example.ly.animationdemo.widget.charge;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import com.example.ly.animationdemo.R;
import com.socks.library.KLog;

/**
 * 充电中的动画
 * Created by ly on 2016/12/5.
 */

public class ChargingView extends View {

    private Paint mPaint;

    private Context context;

    //view的宽度和高度
    private int mWidth;
    private int mHeight;

    //竖直方向
    private static final int VERTICAL = 0;
    //水平方向
    private static final int HORIZONTAL = 1;

    //动画view的方向，竖直和水平
    private int oritation = VERTICAL;

    //周围线框
    private String color_strock = "#b49d7c";
    //背景填充色
    private String color_bg = "#463938";
    //已充电
    private String color_charged = "#ffea00";
    //未充电
    private String color_charge = "#544645";

    public ChargingView(Context context) {
        this(context, null);
    }

    public ChargingView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ChargingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        initPaint();
        initView(attrs);
    }

    private void initView(AttributeSet attrs) {
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.YYChargingView);
        oritation = array.getInt(R.styleable.YYChargingView_oritation, VERTICAL);
    }

    private void initPaint() {
        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(Color.parseColor(color_strock));
        mPaint.setStrokeWidth(dp2px(2));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        mWidth = getMeasuredWidth();
        mHeight = getMeasuredHeight();

        //绘制顶部矩形
        drawTopRect(canvas);

        //绘制底部矩形
        drawBottomRect(canvas);

        //绘制未完成的充电进度，共20个
        int item_height = (mHeight - dp2px(2) - dp2px(5)) / 61;


        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(Color.parseColor(color_charge));
        for (int i = 1; i <= 20; i++) {
            KLog.d("默认为灰色背景");
            RectF proRect = new RectF(mWidth / 6 + dp2px(5), (dp2px(15) + (3 * i - 2) * item_height),
                    5 * mWidth / 6 - dp2px(5), dp2px(15) + (3 * i * item_height));
            canvas.drawRoundRect(proRect, dp2px(2), dp2px(2), mPaint);
        }


    }

    /**
     * 绘制底部矩形
     *
     * @param canvas
     */
    private void drawBottomRect(Canvas canvas) {

        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(Color.parseColor(color_strock));
        mPaint.setStrokeWidth(dp2px(2));

        RectF rectF1 = new RectF(mWidth / 6, dp2px(2) + dp2px(5), mWidth * 5 / 6, getBottom());
        canvas.drawRoundRect(rectF1, dp2px(2), dp2px(2), mPaint);

        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(Color.parseColor(color_bg));

        RectF bg = new RectF(mWidth / 6 + dp2px(1), dp2px(2) + dp2px(5) + dp2px(1), mWidth * 5 / 6 - dp2px(1), getBottom() - dp2px(1));
        canvas.drawRect(bg, mPaint);

    }

    /**
     * 绘制顶部矩形
     *
     * @param canvas
     */
    private void drawTopRect(Canvas canvas) {

        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(Color.parseColor(color_strock));
        mPaint.setStrokeWidth(dp2px(2));

        RectF rectF1 = new RectF(mWidth / 3, dp2px(2), mWidth * 2 / 3, dp2px(2) + dp2px(5));
        canvas.drawRoundRect(rectF1, dp2px(2), dp2px(2), mPaint);

        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(Color.parseColor(color_bg));

        RectF rectFOuter = new RectF(mWidth / 3 + dp2px(1), dp2px(2) + dp2px(1), mWidth * 2 / 3 - dp2px(1), dp2px(2) + dp2px(5) - dp2px(1));
        canvas.drawRect(rectFOuter, mPaint);

    }

    /**
     * 测量view的宽和高，给定默认值，宽300dp，高400dp
     *
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int wSize = MeasureSpec.getSize(widthMeasureSpec);
        int wMode = MeasureSpec.getMode(widthMeasureSpec);
        int hSize = MeasureSpec.getSize(heightMeasureSpec);
        int hMode = MeasureSpec.getMode(heightMeasureSpec);

        if (wMode == MeasureSpec.EXACTLY) {
            mWidth = wSize;
        } else {
            if (oritation == VERTICAL) {
                mWidth = dp2px(10);
            } else {
                mWidth = dp2px(100);
            }

        }
        if (hMode == MeasureSpec.EXACTLY) {
            mHeight = hSize;
        } else {
            if (oritation == VERTICAL) {
                mHeight = dp2px(100);
            } else {
                mHeight = dp2px(10);
            }
        }
        setMeasuredDimension(mWidth, mHeight);
    }


    //一些工具方法
    protected int dp2px(int dp) {
        return (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                dp,
                getResources().getDisplayMetrics());
    }

    protected int sp2px(int sp) {
        return (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_SP,
                sp,
                getResources().getDisplayMetrics());
    }
}
