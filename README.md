---
title: android 自定义view+属性动画实现充电进度条功能
categories: 
- 技术
- android
date: 2016-12-06 21:24:01
tags:
- 自定义view
- 属性动画
- 进度条
---

近期项目中需要使用到一种类似手机电池充电进度的动画效果，以前没学属性动画的时候，是用图片+定时器的方式来完成的，最近一直在学习动画这一块，再加上复习一下自定义view的相关知识点，所以打算用属性动画和自定义view的方式来完成这个功能，将它开源出来，供有需要的人了解一下相关的内容。

本次实现的功能类似下面的效果：

<!--more-->
![充电效果](http://upload-images.jianshu.io/upload_images/676457-d3c427d921469665.gif?imageMogr2/auto-orient/strip)

接下来便详细解析一下如何完成这个功能，了解其中的原理，这样就能举一反三，实现其他类似的动画效果了。
####  **详细代码请看大屏幕**

[https://github.com/crazyandcoder/ChargeProgress](https://github.com/crazyandcoder/ChargeProgress)


### 图形解析

一般，我们自定义view时，是将该view进行化解，分成一个一个小部分，然后在重叠起来进行绘制，对于这个项目，也是按照相同的步骤进行。我们用Word来简单解析一下该动画所包含的基本结构。

![](http://img.blog.csdn.net/20161206213911483?watermark/2/text/aHR0cDovL2Jsb2cuY3Nkbi5uZXQvbGlqaV94Yw==/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70/gravity/SouthEast)

对于这个充电进度view，我将它分成了ABCD四个部分，下面来详细说明各个部分的组成。

> A部分

对于A而言，它是位于整个view的顶部，居中显示，是一个圆角矩形。

> B部分

对于B而言，它是整个view的重要组成部分，包含C和D两部分，其中B主要属性就是背景色的设置。

> C部分

对于C而言，C就是每一个进度的样式，显示的是未完成的进度条样式。


> D部分

对于D而言，它跟C是一样的，只不过是已经完成的进度样式，区别在于颜色的不一样。

其实，这个进度view图形结构还是比较简单的，只是一些简单的矩形，组合而成，因此对于以上的分析，我们轻易的得出一些重要的属性。

```
<?xml version="1.0" encoding="utf-8"?>
<resources>
    <declare-styleable name="charging_progress">

        <!--item个数-->
        <attr name="cgv_item_count" format="integer" />

        <!--边界宽度-->
        <attr name="cgv_border_width" format="dimension" />

        <!--边界颜色-->
        <attr name="cgv_border_color" format="color" />

        <!--圆角半径-->
        <attr name="cgv_border_cornor_radius" format="dimension" />

        <!--充电内每个进度item模块的宽度-->
        <attr name="cgv_item_width" format="dimension" />

        <!--充电内每个进度item模块的高度-->
        <attr name="cgv_item_height" format="dimension" />

        <!--充电内每个进度item模块的前景色，充电中的颜色-->
        <attr name="cgv_item_charging_src" format="color" />

        <!--充电内每个进度item模块的背景色，未充电的颜色-->
        <attr name="cgv_item_charging_background" format="color" />

        <!--view 的背景-->
        <attr name="cgv_background" format="color" />

    </declare-styleable>
</resources>
```

对于以上属性，我们在自定义view的时候需要在xml文件中进行设置，如果没有设置的话，我们给出一个默认。然后我们在代码中进行获取这些属性值。

```

//边界宽度
    private float border_width;
    //item个数
    private int item_count;

    //边界宽度
    private float item_width;
    //边界高度
    private float item_height;
    //view内部的进度前景色
    private int item_charging_src;
    //view内部的进度背景色
    private int item_charging_background;
    //view背景色
    private int background;
    //<!--边界颜色-->
    private int border_color;
    //圆角半径
    private float border_cornor_radius;

//获取xml中设定的属性值
 TypedArray array = mContext.obtainStyledAttributes(attrs, R.styleable.charging_progress);
       
        border_width = array.getDimension(R.styleable.charging_progress_cgv_border_width, dp2px(2));
        item_height = array.getDimension(R.styleable.charging_progress_cgv_item_height, dp2px(10));
        item_width = array.getDimension(R.styleable.charging_progress_cgv_item_width, dp2px(20));
        item_charging_src = array.getColor(R.styleable.charging_progress_cgv_item_charging_src, 0xffffea00);
        item_charging_background = array.getColor(R.styleable.charging_progress_cgv_item_charging_background, 0xff544645);
        background = array.getColor(R.styleable.charging_progress_cgv_background, 0xff463938);
        border_color = array.getColor(R.styleable.charging_progress_cgv_border_color, 0xffb49d7c);
        border_cornor_radius = array.getDimension(R.styleable.charging_progress_cgv_border_cornor_radius, dp2px(2));
        item_count = array.getInt(R.styleable.charging_progress_cgv_item_count, 10);
        
        array.recycle();
```

已经获取了自定义属性的值，那么接下来，我们就来具体绘制这些组合图形。

对于一个自定义view，首先要做的就是测量view的大小，而本项目中view的宽度和高度，宽度是好计算的，我们设置**view的宽度等于item_widht 乘以2** 。但是对于高度的话，因为我们设置了progress的级数，也就是item_count，也设置了item的高度和宽度，所以对于**高度，我们可以通过计算item_count 乘以 item_height，再加上间隔数和顶部矩形的就是整个view的高度**。同时，我们设定，**顶部矩形的高度等于item_height，宽度等于item_widht的一半，中间间隔等于item_height 除以2**

```
/**
     * 测量view的宽和高，
     *
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        
            //总间隔数=(item_count+1)  乘以间隔高度（间隔高度等于item_height的一半）
            //总数=item_count 乘以 item_height + 总间隔数 + 顶部一个矩形（高度等于item的高度，宽度等于item的宽度的一半）
            mHeight = (int) (item_count * item_height + (item_count + 1) * item_height / 2 + item_height);
            mWidth = (int) (2 * item_width);
      
        setMeasuredDimension(mWidth, mHeight);
    }

```


有了上面的设置，接下来我们就可以按部就班的画图了。

对于坐标中心点是设定在左上角，也就是（0，0）处。

### 画顶部矩形

知道了坐标系的原点，那么顶部矩形的坐标就可以计算了。

首先设置画笔。

```
mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(border_width);
        mPaint.setColor((border_color));
```

由于顶部矩形的width等于item_widht的一半，所以它的width等于整个view的width的1/6，


```

        int left = mWidth * 3 / 8;
        int top = 0;
        int right = 5 * mWidth / 8;
        int bottom = (int) item_height / 2;

        
        //顶部的矩形
        RectF topRect = new RectF(left, top, right, bottom);
        canvas.drawRoundRect(topRect, border_cornor_radius, border_cornor_radius, mPaint);
```

接下来绘制底部的矩形，也就是包含进度item的矩形

```
//总的进度背景
        RectF border = new RectF(0, bottom, mWidth, mHeight);
        canvas.drawRoundRect(border, border_cornor_radius, border_cornor_radius, mPaint);

```

接下来绘制每个item的矩形，对于每个item的坐标，实际上是有规律可循的。

```
//绘制所有的进度
        for (int i = 1; i <= item_count; i++) {
            mPaint.setStyle(Paint.Style.FILL);
            mPaint.setColor((item_charging_background));
            RectF backRect = new RectF(mWidth / 4,
                    (i + 1) * item_height / 2 + (i - 1) * item_height,
                    3 * mWidth / 4,
                    item_height / 2 + i * (3 * item_height / 2));
            canvas.drawRoundRect(backRect, border_cornor_radius, border_cornor_radius, mPaint);
        }
```

### 绘制动画

对于交流动画，就是从进度0到100的动画显示，依次显示。其实也是对于坐标的计算而已。接下来最终要的功能就是动画的使用了，我们使用的是属性动画呢？因为，常规的动画它不支持啊，很简单。

对于Android属性动画的学习，可以查看这篇文章，稍微了解一下。《[Android动画了解](http://crazyandcoder.github.io/2016/11/28/android%20%E5%8A%A8%E7%94%BB%E4%BA%86%E8%A7%A3/)》


**1、交流动画**
```
 /**
     * 绘制交流动画
     *
     * @param canvas
     */
    private void drawACAnimaiton(Canvas canvas) {
        int j = getProgress() / item_count;
        //已经充好的进度
        for (int i = item_count; i >= (item_count - j); i--) {
            RectF backRect = new RectF(mWidth / 4,
                    (i + 1) * item_height / 2 + (i - 1) * item_height,
                    3 * mWidth / 4,
                    item_height / 2 + i * (3 * item_height / 2));
            canvas.drawRoundRect(backRect, border_cornor_radius, border_cornor_radius, mPaint);
            mPaint.setStyle(Paint.Style.FILL);
            mPaint.setColor(item_charging_src);
            canvas.drawRoundRect(backRect, border_cornor_radius, border_cornor_radius, mPaint);
        }

    }
```

我们首先获取当前的进度，然后依次给它填充背景，这就是已完成的进度表示。

然后使用动画即可，我们设置进度为100，也就是充满，然后设置动画时间是10秒钟，对于下面的动画执行原理是什么呢？其实很简单，获取当前的进度，然后从0开始，依次绘制进度，知道绘制的进度为100就是总的进度，最后再循环执行动画即可。

```
/**
     * 设置交流动画
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
```


**2、直流动画**

对于直流动画就稍微比较复杂了。当我们设置了进度后，需要我们预先绘制已完成的进度，然后在下一个进度进行闪烁表示动画，那么该如何完成呢？

首先看绘制代码：

```
/**
     * 直流动画
     *
     * @param canvas
     */
    private void drawDCAniamtion(Canvas canvas) {
        int j = getProgress() / item_count;
        //已经充好的进度
        for (int i = item_count; i > (item_count - j); i--) {
            RectF backRect = new RectF(mWidth / 4,
                    (i + 1) * item_height / 2 + (i - 1) * item_height,
                    3 * mWidth / 4,
                    item_height / 2 + i * (3 * item_height / 2));
            canvas.drawRoundRect(backRect, border_cornor_radius, border_cornor_radius, mPaint);
            mPaint.setStyle(Paint.Style.FILL);
            mPaint.setColor(item_charging_src);
            canvas.drawRoundRect(backRect, border_cornor_radius, border_cornor_radius, mPaint);
        }

        //下一个进度，隐藏和显示交替执行动画
        int i = item_count - j;

        if (i > 0) {
            RectF backRect = new RectF(mWidth / 4,
                    (i + 1) * item_height / 2 + (i - 1) * item_height,
                    3 * mWidth / 4,
                    item_height / 2 + i * (3 * item_height / 2));
            mPaint.setStyle(Paint.Style.FILL);
            if (show) {
                mPaint.setColor((item_charging_src));
            } else {
                mPaint.setColor((item_charging_background));
            }
            canvas.drawRoundRect(backRect, border_cornor_radius, border_cornor_radius, mPaint);
        }
    }

```

首先绘制已完成的进度，然后在绘制闪烁的部分。

```
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
```

到这里，就很明了了。对于直流动画，我们使用属性动画中这个ValueAnimator类，它的意思就是从0到1平滑的过渡，在设定的时间内。我们的原理是当达到0.5以上后就设定灰色进度，当小于0.5的话就设置亮色进度，然后在刷新一下view即可。

以上便是主要的原理，对于源码，可以访问《[ChargeProgress](https://github.com/crazyandcoder/ChargeProgress)》。






----------


**关于作者：**

[1. 简书 http://www.jianshu.com/users/18281bdb07ce/latest_articles](http://www.jianshu.com/users/18281bdb07ce/latest_articles)
 

[2. 博客 http://crazyandcoder.github.io/](http://crazyandcoder.github.io/)


[3. github https://github.com/crazyandcoder](https://github.com/crazyandcoder)

[4. 开源中国 https://my.oschina.net/crazyandcoder/blog](https://my.oschina.net/crazyandcoder/blog)

 



