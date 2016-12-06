package com.example.ly.animationdemo.widget.charge;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

import com.framelibrary.utils.HZDisplayUtil;
import com.socks.library.KLog;

/**
 * 远赢平台充电中的动画，分为直流和交流动画
 * file:///Users/ly/liji/doc/work/远赢平台/design/预览图/APP充电中-丫丫充电-交流.gif
 * <p>
 * Created by ly on 2016/12/2.
 */

public class YYChargingViewDemo extends View {

    /**
     * 每一个小进度的画笔
     */
    private Paint mProgressPaint;

    /**
     * 总进度的画笔
     */
    private Paint mLinePaint;

    private int mWidth;
    private int mHeight;

    private Context context;

    private int progress = 0;

    private int t = 800 / 61;

    //直流电: direct-current （ DC ）
    public static final int DC = 1;

    //交流电:	alternating current ( AC ) （交流电流） AC
    public static final int AC = 2;

    //充电类型，默认为交流
    private int chargeType = DC;

    //直流充电中的进度显示和隐藏
    private boolean show = true;

    private boolean init = true;

    public YYChargingViewDemo(Context context) {
        this(context, null);
    }

    public YYChargingViewDemo(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public YYChargingViewDemo(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        initPaint();

    }

    private void initPaint() {
        mLinePaint = new Paint();
        mLinePaint.setStyle(Paint.Style.STROKE);
        mLinePaint.setColor(Color.parseColor("#b49d7c"));
        mLinePaint.setStrokeWidth(getdp(2));
    }


    /**
     * 当前进度
     *
     * @return
     */
    public int getProgress() {
        return progress % 101;
    }

    /**
     * 设置充电进度
     *
     * @param progress
     */
    public void setProgress(int progress) {
        this.progress = progress;
        invalidate();
    }

    /**
     * 充电类型
     *
     * @return
     */
    public int getChargeType() {
        return chargeType;
    }


    /**
     * 设置充电类型
     *
     * @param chargeType
     */
    public void setChargeType(int chargeType) {
        this.chargeType = chargeType;
    }

    /**
     * 设置播放动画的进度
     *
     * @param progress
     */
    public void setmProgressAnim(int progress) {
        ObjectAnimator anim = ObjectAnimator.ofInt(this, "progress", progress);
        anim.setDuration(10 * 1000);
        anim.setInterpolator(new LinearInterpolator());
        anim.setRepeatCount(ValueAnimator.INFINITE);
        anim.setRepeatMode(ValueAnimator.RESTART);
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
            }
        });
        anim.start();

    }

    public void setDCAnimation(final int progress){
        ValueAnimator animator=ValueAnimator.ofFloat(0,1);
        animator.setInterpolator(new LinearInterpolator());
        animator.setDuration(1000);
        animator.setRepeatCount(-1);
        animator.setRepeatMode(ValueAnimator.RESTART);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value=(float)animation.getAnimatedValue();
                KLog.d("value: "+value);
                if (value>0.5){
                    show=true;
                }else{
                    show=false;
                }
                setProgress(progress);
            }
        });
        animator.start();

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        KLog.d("pogress：" + getProgress());

        //顶部圆角矩形
        RectF rectF1 = new RectF(100, 50, 150, 80);
        canvas.drawRoundRect(rectF1, getdp(2), getdp(2), mLinePaint);
        mLinePaint.setStyle(Paint.Style.FILL);
        mLinePaint.setColor(Color.parseColor("#463938"));
        RectF rectFOuter = new RectF(100, 50, 150, 80);
        canvas.drawRoundRect(rectFOuter, getdp(2), getdp(2), mLinePaint);


        //总的进度，圆角矩形
        mLinePaint.setStyle(Paint.Style.STROKE);
        mLinePaint.setColor(Color.parseColor("#b49d7c"));
        mLinePaint.setStrokeWidth(getdp(2));
        RectF rectF = new RectF(50, 80, 200, 880);
        canvas.drawRoundRect(rectF, getdp(10), getdp(10), mLinePaint);
        mLinePaint.setStyle(Paint.Style.FILL);
        mLinePaint.setColor(Color.parseColor("#463938"));
        RectF rectFInner = new RectF(52, 82, 198, 878);
        canvas.drawRoundRect(rectFInner, getdp(10), getdp(10), mLinePaint);


        for (int i = 1; i <= 20; i++) {
            KLog.d("默认为灰色背景");
            RectF proRect = new RectF(50 + 2 * t, (80 + (3 * i - 2) * t), 200 - 2 * t, 80 + (3 * i * t));
            mLinePaint.setStyle(Paint.Style.FILL);
            mLinePaint.setColor(Color.parseColor("#544645"));
            canvas.drawRoundRect(proRect, getdp(5), getdp(5), mLinePaint);
        }

        if (getChargeType() == AC) {
            //绘制交流动画
            drawACAnimation(canvas);
        } else {
            //绘制直流动画
            drawDCAnimation(canvas);
        }


    }

    /**
     * 绘制直流动画
     *
     * @param canvas
     */
    private void drawDCAnimation(Canvas canvas) {
        KLog.d("绘制直流动画");
        int j = getProgress() / 5;
        KLog.d("绘制直流动画,j: " + j);


        //已经充好的进度
        for (int i = 1; i <= j; i++) {
            RectF proRect = new RectF(50 + 2 * t, (80 + (3 * i - 2) * t), 200 - 2 * t, 80 + (3 * i * t));
            mLinePaint.setStyle(Paint.Style.FILL);
            mLinePaint.setColor(Color.parseColor("#ffea00"));
            canvas.drawRoundRect(proRect, getdp(5), getdp(5), mLinePaint);
        }

        //下一个进度，隐藏和显示交替执行动画
        int i = j + 1;
        RectF proRect = new RectF(50 + 2 * t, (80 + (3 * i - 2) * t), 200 - 2 * t, 80 + (3 * i * t));
        mLinePaint.setStyle(Paint.Style.FILL);
        if (show) {
            mLinePaint.setColor(Color.parseColor("#ffea00"));
        } else {
            mLinePaint.setColor(Color.parseColor("#544645"));
        }
        canvas.drawRoundRect(proRect, getdp(5), getdp(5), mLinePaint);

    }

    /**
     * 绘制交流动画
     *
     * @param canvas
     */
    private void drawACAnimation(final Canvas canvas) {
        KLog.d("绘制交流动画");
        int j = getProgress() / 5;
        for (int i = 20; i >= (20 - j); i--) {
            drawCurrent(canvas, i);
        }


    }

    /**
     * 某个进度已绘制
     *
     * @param i
     */
    private void drawCurrent(Canvas canvas, int i) {
        RectF proRect = new RectF(50 + 2 * t, (80 + (3 * i - 2) * t), 200 - 2 * t, 80 + (3 * i * t));
        mLinePaint.setStyle(Paint.Style.FILL);
        mLinePaint.setColor(Color.parseColor("#ffea00"));
        canvas.drawRoundRect(proRect, getdp(5), getdp(5), mLinePaint);
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
            mWidth = getdp(20);
        }
        if (hMode == MeasureSpec.EXACTLY) {
            mHeight = hSize;
        } else {
            mHeight = getdp(400);
        }
        setMeasuredDimension(mWidth, mHeight);
    }


    private int getdp(int px) {
        return (int) HZDisplayUtil.dip2px(context, px);
    }
}
