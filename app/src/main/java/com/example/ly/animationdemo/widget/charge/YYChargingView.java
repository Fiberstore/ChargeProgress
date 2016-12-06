package com.example.ly.animationdemo.widget.charge;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.LinearInterpolator;

import com.example.ly.animationdemo.R;
import com.socks.library.KLog;

/**
 * 充电中的动画
 * Created by ly on 2016/12/5.
 */

public class YYChargingView extends View {

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

    //圆角半径
    private int value_cornor = dp2px(2);

    private int progress = 0;

    //交替显示充电完成的动画
    private boolean show = true;

    //直流电: direct-current （ DC ）
    public static final int DC = 1;

    //交流电:	alternating current ( AC ) （交流电流） AC
    public static final int AC = 2;

    //充电类型，默认为交流
    private int chargeType = DC;
    private ObjectAnimator animAC;
    private ValueAnimator animatorDC;

    public YYChargingView(Context context) {
        this(context, null);
    }

    public YYChargingView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public YYChargingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        mWidth = dp2px(20);//边界和间隔都是1dp,中间是8dp,共12dp
        mHeight = 21 * dp2px(3) + 20 * dp2px(5) + dp2px(5);
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

    public void closeAnimation() {

        if (animAC != null) {
            animAC.cancel();
        }

        if (animatorDC != null) {
            animatorDC.cancel();
        }
    }

    /**
     * 当前进度
     *
     * @return
     */
    public int getProgress() {
        return progress % 100;
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


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        mWidth = getMeasuredWidth();
        mHeight = getMeasuredHeight();

        KLog.d("width: " + mWidth + " ,  mheigh: " + mHeight);

        //绘制顶部矩形
        drawTopRect(canvas);

        //绘制底部矩形
        drawBottomRect(canvas);

        for (int i = 1; i <= 20; i++) {
            int item_left = mWidth / 5;
            int item_top = dp2px(5) + i * dp2px(3) + (i - 1) * dp2px(5);
            int item_right = 4 * mWidth / 5;
            int item_bottom = dp2px(5) + i * (dp2px(3) + dp2px(5));
            RectF proRect = new RectF(item_left, item_top, item_right, item_bottom);
            mPaint.setStyle(Paint.Style.FILL);
            mPaint.setColor(Color.parseColor(color_charge));
            canvas.drawRoundRect(proRect, dp2px(2), dp2px(2), mPaint);
        }

        //直流动画
        if (chargeType == DC) {
            drawDCAniamtion(canvas);
        } else {
            //交流动画
            drawACAnimaiton(canvas);

        }


    }


    /**
     * 绘制交流动画
     *
     * @param canvas
     */
    private void drawACAnimaiton(Canvas canvas) {
        int j = getProgress() / 5;
        KLog.d("绘制交流动画,j: " + j);

        //已经充好的进度
        for (int i = 20; i >= (20 - j); i--) {
            int item_left = mWidth / 5;
            int item_top = dp2px(5) + i * dp2px(3) + (i - 1) * dp2px(5);
            int item_right = 4 * mWidth / 5;
            int item_bottom = dp2px(5) + i * (dp2px(3) + dp2px(5));
            RectF proRect = new RectF(item_left, item_top, item_right, item_bottom);
            mPaint.setStyle(Paint.Style.FILL);
            mPaint.setColor(Color.parseColor(color_charged));
            canvas.drawRoundRect(proRect, dp2px(2), dp2px(2), mPaint);
        }

    }

    /**
     * 直流动画
     *
     * @param canvas
     */
    private void drawDCAniamtion(Canvas canvas) {
        int j = getProgress() / 5;
        KLog.d("绘制直流动画,j: " + j);


        //已经充好的进度
        for (int i = 20; i > (20 - j); i--) {
            int item_left = mWidth / 5;
            int item_top = dp2px(5) + i * dp2px(3) + (i - 1) * dp2px(5);
            int item_right = 4 * mWidth / 5;
            int item_bottom = dp2px(5) + i * (dp2px(3) + dp2px(5));
            RectF proRect = new RectF(item_left, item_top, item_right, item_bottom);
            mPaint.setStyle(Paint.Style.FILL);
            mPaint.setColor(Color.parseColor(color_charged));
            canvas.drawRoundRect(proRect, dp2px(2), dp2px(2), mPaint);
        }

        //下一个进度，隐藏和显示交替执行动画
        int i = 20 - j;

        if (i > 0) {
            int item_left = mWidth / 5;
            int item_top = dp2px(5) + i * dp2px(3) + (i - 1) * dp2px(5);
            int item_right = 4 * mWidth / 5;
            int item_bottom = dp2px(5) + i * (dp2px(3) + dp2px(5));
            RectF proRect = new RectF(item_left, item_top, item_right, item_bottom);

            mPaint.setStyle(Paint.Style.FILL);
            if (show) {
                mPaint.setColor(Color.parseColor(color_charged));
            } else {
                mPaint.setColor(Color.parseColor(color_charge));
            }
            canvas.drawRoundRect(proRect, dp2px(2), dp2px(2), mPaint);
        }
    }

    /**
     * 设置播放动画的进度
     */
    public void setACAnimation() {
        chargeType = AC;
        animAC = ObjectAnimator.ofInt(this, "progress", 100);
        animAC.setDuration(10 * 1000);
        animAC.setInterpolator(new LinearInterpolator());
        animAC.setRepeatCount(ValueAnimator.INFINITE);

        animAC.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                invalidate();
            }
        });
        animAC.start();
    }


    /**
     * 直流动画
     *
     * @param progress
     */
    public void setDCAnimation(final int progress) {
        chargeType = DC;
        animatorDC = ValueAnimator.ofFloat(0, 1);
        animatorDC.setInterpolator(new LinearInterpolator());
        animatorDC.setDuration(1000);
        animatorDC.setRepeatCount(-1);
        animatorDC.setRepeatMode(ValueAnimator.RESTART);
        animatorDC.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (float) animation.getAnimatedValue();
                KLog.d("value: " + value);
                if (value > 0.5) {
                    show = true;
                } else {
                    show = false;
                }
                setProgress(progress);
            }
        });
        animatorDC.start();

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

        RectF rectF1 = new RectF(0, dp2px(5), mWidth, mHeight);
        canvas.drawRoundRect(rectF1, value_cornor, value_cornor, mPaint);
//
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(Color.parseColor(color_bg));

        //内边框，减去1dp
        RectF bg = new RectF(dp2px(1), dp2px(5) + dp2px(1), mWidth - dp2px(1), mHeight - dp2px(1));
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

        RectF rectF1 = new RectF(mWidth / 4, 0, mWidth * 3 / 4, dp2px(5));
        canvas.drawRoundRect(rectF1, value_cornor, value_cornor, mPaint);

        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(Color.parseColor(color_bg));

        RectF rectFOuter = new RectF(mWidth / 4 + dp2px(1), dp2px(1), mWidth * 3 / 4 - dp2px(1), dp2px(5) - dp2px(1));
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
            mWidth = dp2px(20);//边界和间隔都是1dp,中间是8dp,共12dp

        }
        if (hMode == MeasureSpec.EXACTLY) {
            mHeight = hSize;
        } else {
            mHeight = 21 * dp2px(3) + 20 * dp2px(5) + dp2px(5);
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
