package com.framelibrary.utils;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.os.Build;
import android.support.annotation.NonNull;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;


import java.lang.reflect.Method;

/**
 * 获取屏幕信息
 * [功能详细描述]
 * @author ljl
 * @version [2014年10月28日 下午2:40:37] 
 */
public class HZDisplayUtil {
    public static int width = 480, height = 800, index = 0;
    
    public static float density = 1.5f;
    
    private static boolean ischeck = false;
    
    private static TypedValue mTmpValue = new TypedValue();


    /**
     * 获取屏幕宽度
     * @param ctx
     * @return
     */
    public static int getWidth(Context ctx) {
        if (!ischeck) {
            init(ctx);
        }
        return width;
    }
    
    /** 
     * @author yu
     * @Title: getResolution 
     * @Description: 获取屏幕分辨率
     * @param @param ctx
     * @param @return 
     * @return String 类似480x800
     * @throws 
     */
    public static String getResolution(Context ctx) {
        if (!ischeck) {
            init(ctx);
        }
        return width + "x" + height;
    }

    /**
     * 获取屏幕宽和高
     * @param ctx
     */
    public static void init(Context ctx) {
        if (ctx instanceof Activity) {
            Activity activ = (Activity) ctx;
            Display dis = activ.getWindowManager().getDefaultDisplay();
            DisplayMetrics dm = activ.getResources().getDisplayMetrics();
            density = dm.density;
            width = dis.getWidth();
            height = dis.getHeight();
            ischeck = true;
        }
    }

    /**
     * 获取屏幕高度
     * @param ctx
     * @return
     */
    public static int getHeight(Context ctx) {
        if (!ischeck) {
            init(ctx);
        }
        return height;
    }
    
    /**
    * 描述：dip转换为px.
    *
    * @param context the context
    * @param dipValue the dip value
    * @return px值
    */
    public static float dip2px(Context context, float dipValue) {
        DisplayMetrics mDisplayMetrics = HZAppUtils.getDisplayMetrics(context);
        return applyDimension(TypedValue.COMPLEX_UNIT_DIP, dipValue, mDisplayMetrics);
    }
    
    /**
     * 描述：px转换为dip.
     *
     * @param context the context
     * @param pxValue the px value
     * @return dip值
     */
    public static float px2dip(Context context, float pxValue) {
        DisplayMetrics mDisplayMetrics = HZAppUtils.getDisplayMetrics(context);
        return pxValue / mDisplayMetrics.density;
    }
    
    /**
     * 描述：sp转换为px.
     *
     * @param context the context
     * @param spValue the sp value
     * @return sp值
     */
    public static float sp2px(Context context, float spValue) {
        DisplayMetrics mDisplayMetrics = HZAppUtils.getDisplayMetrics(context);
        return applyDimension(TypedValue.COMPLEX_UNIT_SP, spValue, mDisplayMetrics);
    }
    
    /**
     * 描述：px转换为sp.
     *
     * @param context the context
     * @param pxValue the px value
     * @return sp值
     */
    public static float px2sp(Context context, float pxValue) {
        DisplayMetrics mDisplayMetrics = HZAppUtils.getDisplayMetrics(context);
        return pxValue / mDisplayMetrics.scaledDensity;
    }
    
    /**
     * TypedValue官方源码中的算法，任意单位转换为PX单位
     * @param unit  TypedValue.COMPLEX_UNIT_DIP
     * @param value 对应单位的值
     * @param metrics 密度
     * @return px值
     */
    public static float applyDimension(int unit, float value, DisplayMetrics metrics) {
        switch (unit) {
            case TypedValue.COMPLEX_UNIT_PX:
                return value;
            case TypedValue.COMPLEX_UNIT_DIP:
                return value * metrics.density;
            case TypedValue.COMPLEX_UNIT_SP:
                return value * metrics.scaledDensity;
            case TypedValue.COMPLEX_UNIT_PT:
                return value * metrics.xdpi * (1.0f / 72);
            case TypedValue.COMPLEX_UNIT_IN:
                return value * metrics.xdpi;
            case TypedValue.COMPLEX_UNIT_MM:
                return value * metrics.xdpi * (1.0f / 25.4f);
        }
        return 0;
    }
    
    /**
     * 描述：获取xml中定义大小
     *
     * @param context the context
     * @param id 控件ID
     * @return 对应单位的值
     */
    public static int getXmlDef(Context context, int id) {
        synchronized (mTmpValue) {
            TypedValue value = mTmpValue;
            context.getResources().getValue(id, value, true);
            return (int) TypedValue.complexToFloat(value.data);
        }
    }

    /**
     * 设置全屏
     * @param window
     */
    public static void setFullWindow(Window window) {
        if (HZSdkViewsion.AFTER_LOLLIPOP) {
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                    | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION | 128);
        }
        else {
            window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
    }
    
    public static void setFullWindowWithoutNavigationBar(Window window, Context context) {
        if (HZSdkViewsion.AFTER_LOLLIPOP && hasNavigationBar(context)) {
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }
    }
    
    public static boolean hasNavigationBar(Context context) {
        boolean hasNavigationBar = false;
        Resources rs = context.getResources();
        int id = rs.getIdentifier("config_showNavigationBar", "bool", "android");
        if (id > 0) {
            hasNavigationBar = rs.getBoolean(id);
        }
        try {
            Class systemPropertiesClass = Class.forName("android.os.SystemProperties");
            Method m = systemPropertiesClass.getMethod("get", String.class);
            String navBarOverride = (String) m.invoke(systemPropertiesClass, "qemu.hw.mainkeys");
            if ("1".equals(navBarOverride)) {
                hasNavigationBar = false;
            }
            else if ("0".equals(navBarOverride)) {
                hasNavigationBar = true;
            }
        }
        catch (Exception e) {
            HZlog.e(e);
        }
        return hasNavigationBar;
    }
    
    public static int getNavigationBarHeight(Context context) {
        int navigationBarHeight = 0;
        Resources rs = context.getResources();
        int id = rs.getIdentifier("navigation_bar_height", "dimen", "android");
        if (id > 0 && hasNavigationBar(context)) {
            navigationBarHeight = rs.getDimensionPixelSize(id);
        }
        return navigationBarHeight;
    }
    
    /**
     * set status bar translucency
     *
     * @param on
     */
    protected void setTranslucentStatus(@NonNull Activity activity, boolean on) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window win = activity.getWindow();
            WindowManager.LayoutParams winParams = win.getAttributes();
            final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
            if (on) {
                winParams.flags |= bits;
            }
            else {
                winParams.flags &= ~bits;
            }
            win.setAttributes(winParams);
        }
    }
}
