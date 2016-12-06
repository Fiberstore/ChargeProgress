package com.framelibrary.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.TextPaint;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.AbsListView;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;


/**
 * @2Do:
 * @Author M2
 * @Version v 1.0
 * @Date [2016/3/22 0022]
 */
public class M2ViewUtil {
    
    /**  UI设计的基准宽度. */
    public static int uiWidth = 720;
    
    /**  UI设计的基准高度. */
    public static int uiHeight = 1280;
    
    public static int getColor(int id, Activity activity) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return activity.getResources().getColor(id);
        }
        else {
            return activity.getResources().getColor(id, activity.getTheme());
        }
    }
    
    public static void setBackgroundDrawable(View view, Drawable drawable) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            view.setBackground(drawable);
        }
        else {
            view.setBackgroundDrawable(drawable);
        }
    }
    
    /**
     * 无效值
     */
    public static final int INVALID = Integer.MIN_VALUE;
    
    /**
     * 描述：重置AbsListView的高度. item 的最外层布局要用
     * RelativeLayout,如果计算的不准，就为RelativeLayout指定一个高度
     *
     * @param absListView
     *            the abs list view
     * @param lineNumber
     *            每行几个 ListView一行一个item
     * @param verticalSpace
     *            the vertical space
     */
    public static void setAbsListViewHeight(AbsListView absListView, int lineNumber, int verticalSpace) {
        
        int totalHeight = getAbsListViewHeight(absListView, lineNumber, verticalSpace);
        ViewGroup.LayoutParams params = absListView.getLayoutParams();
        params.height = totalHeight;
        ((ViewGroup.MarginLayoutParams) params).setMargins(0, 0, 0, 0);
        absListView.setLayoutParams(params);
    }
    
    /**
     * 描述：获取AbsListView的高度.
     *
     * @param absListView            the abs list view
     * @param lineNumber            每行几个 ListView一行一个item
     * @param verticalSpace            the vertical space
     * @return the abs list view height
     */
    public static int getAbsListViewHeight(AbsListView absListView, int lineNumber, int verticalSpace) {
        int totalHeight = 0;
        int w = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        int h = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        absListView.measure(w, h);
        ListAdapter mListAdapter = absListView.getAdapter();
        if (mListAdapter == null) {
            return totalHeight;
        }
        
        int count = mListAdapter.getCount();
        if (absListView instanceof ListView) {
            for (int i = 0; i < count; i++) {
                View listItem = mListAdapter.getView(i, null, absListView);
                listItem.measure(w, h);
                totalHeight += listItem.getMeasuredHeight();
            }
            if (count == 0) {
                totalHeight = verticalSpace;
            }
            else {
                totalHeight = totalHeight + (((ListView) absListView).getDividerHeight() * (count - 1));
            }
            
        }
        else if (absListView instanceof GridView) {
            int remain = count % lineNumber;
            if (remain > 0) {
                remain = 1;
            }
            if (mListAdapter.getCount() == 0) {
                totalHeight = verticalSpace;
            }
            else {
                View listItem = mListAdapter.getView(0, null, absListView);
                listItem.measure(w, h);
                int line = count / lineNumber + remain;
                totalHeight = line * listItem.getMeasuredHeight() + (line - 1) * verticalSpace;
            }
            
        }
        return totalHeight;
        
    }
    
    /**
     * 测量这个view，最后通过getMeasuredWidth()获取宽度和高度.
     *
     * @param v
     *            要测量的view
     * @return 测量过的view
     */
    public static void measureView(View v) {
        ViewGroup.LayoutParams p = v.getLayoutParams();
        if (p == null) {
            p = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }
        
        int childWidthSpec = ViewGroup.getChildMeasureSpec(0, 0 + 0, p.width);
        int lpHeight = p.height;
        int childHeightSpec;
        if (lpHeight > 0) {
            childHeightSpec = View.MeasureSpec.makeMeasureSpec(lpHeight, View.MeasureSpec.EXACTLY);
        }
        else {
            childHeightSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        }
        v.measure(childWidthSpec, childHeightSpec);
    }
    
    /**
     * 从父亲布局中移除自己
     * @param v
     */
    public static void removeSelfFromParent(View v) {
        ViewParent parent = v.getParent();
        if (parent != null) {
            if (parent instanceof ViewGroup) {
                ((ViewGroup) parent).removeView(v);
            }
        }
    }
    
    /**
     * 描述：根据屏幕大小缩放.
     *
     * @param context the context
     * @param value the px value
     * @return the int
     */
    public static int scale(Context context, float value) {
        DisplayMetrics mDisplayMetrics = M2AppUtils.getDisplayMetrics(context);
        return scale(mDisplayMetrics.widthPixels, mDisplayMetrics.heightPixels, value);
    }
    
    /**
     * 描述：根据屏幕大小缩放.
     *
     * @param displayWidth the display width
     * @param displayHeight the display height
     * @param pxValue the px value
     * @return the int
     */
    public static int scale(int displayWidth, int displayHeight, float pxValue) {
        if (pxValue == 0) {
            return 0;
        }
        float scale = 1;
        try {
            
            float scaleWidth = (float) displayWidth / uiWidth;
            float scaleHeight = (float) displayHeight / uiHeight;
            scale = Math.min(scaleWidth, scaleHeight);
        }
        catch (Exception e) {
        }
        return Math.round(pxValue * scale + 0.5f);
    }
    
    /**
     *
     * 描述：View树递归调用做适配.
     * AbAppConfig.uiWidth = 1080;
     * AbAppConfig.uiHeight = 700;
     * scaleContentView((RelativeLayout)findViewById(R.id.rootLayout));
     * 要求布局中的单位都用px并且和美工的设计图尺寸一致，包括所有宽高，Padding,Margin,文字大小
     * @param contentView
     */
    public static void scaleContentView(ViewGroup contentView) {
        scaleView(contentView);
        if (contentView.getChildCount() > 0) {
            for (int i = 0; i < contentView.getChildCount(); i++) {
                if (contentView.getChildAt(i) instanceof ViewGroup) {
                    scaleContentView((ViewGroup) (contentView.getChildAt(i)));
                }
                else {
                    scaleView(contentView.getChildAt(i));
                }
            }
        }
    }
    
    /**
     * 按比例缩放View，以布局中的尺寸为基准
     * @param view
     */
    public static void scaleView(View view) {
        if (view instanceof TextView) {
            TextView textView = (TextView) view;
            setTextSize(textView, textView.getTextSize());
        }
        
        ViewGroup.LayoutParams params = (ViewGroup.LayoutParams) view.getLayoutParams();
        if (null != params) {
            int width = INVALID;
            int height = INVALID;
            if (params.width != ViewGroup.LayoutParams.WRAP_CONTENT
                    && params.width != ViewGroup.LayoutParams.MATCH_PARENT) {
                width = params.width;
            }
            
            if (params.height != ViewGroup.LayoutParams.WRAP_CONTENT
                    && params.height != ViewGroup.LayoutParams.MATCH_PARENT) {
                height = params.height;
            }
            
            //size
            setViewSize(view, width, height);
            
            // Padding
            setPadding(view,
                    view.getPaddingLeft(),
                    view.getPaddingTop(),
                    view.getPaddingRight(),
                    view.getPaddingBottom());
        }
        
        // Margin
        if (view.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams mMarginLayoutParams = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
            if (mMarginLayoutParams != null) {
                setMargin(view,
                        mMarginLayoutParams.leftMargin,
                        mMarginLayoutParams.topMargin,
                        mMarginLayoutParams.rightMargin,
                        mMarginLayoutParams.bottomMargin);
            }
        }
        
    }
    
    /**
     * 缩放文字大小
     * @param textView button
     * @param size sp值
     * @return
     */
    public static void setSPTextSize(TextView textView, float size) {
        float scaledSize = scale(textView.getContext(), size);
        textView.setTextSize(scaledSize);
    }
    
    /**
     * 缩放文字大小,这样设置的好处是文字的大小不和密度有关，
     * 能够使文字大小在不同的屏幕上显示比例正确
     * @param textView button
     * @param sizePixels
     *                  px值
     * @return
     */
    public static void setTextSize(TextView textView, float sizePixels) {
        float scaledSize = scale(textView.getContext(), sizePixels);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, scaledSize);
    }
    
    /**
     * 缩放文字大小
     * @param context
     * @param textPaint
     * @param sizePixels
     *                  px值
     * @return
     */
    public static void setTextSize(Context context, TextPaint textPaint, float sizePixels) {
        float scaledSize = scale(context, sizePixels);
        textPaint.setTextSize(scaledSize);
    }
    
    /**
     * 缩放文字大小
     * @param context
     * @param paint
     * @param sizePixels px值
     * @return
     */
    public static void setTextSize(Context context, Paint paint, float sizePixels) {
        float scaledSize = scale(context, sizePixels);
        paint.setTextSize(scaledSize);
    }
    
    /**
     * 设置View的PX尺寸
     * @param view  如果是代码new出来的View，需要设置一个适合的LayoutParams
     * @param widthPixels
     * @param heightPixels
     */
    public static void setViewSize(View view, int widthPixels, int heightPixels) {
        int scaledWidth = scale(view.getContext(), widthPixels);
        int scaledHeight = scale(view.getContext(), heightPixels);
        ViewGroup.LayoutParams params = view.getLayoutParams();
        if (params == null) {
            HZlog.e("setViewSize出错,如果是代码new出来的View，需要设置一个适合的LayoutParams");
            return;
        }
        if (widthPixels != INVALID) {
            params.width = scaledWidth;
        }
        if (heightPixels != INVALID) {
            params.height = scaledHeight;
        }
        view.setLayoutParams(params);
    }
    
    /**
     * 设置PX padding.
     *
     * @param view the view
     * @param left the left padding in pixels
     * @param top the top padding in pixels
     * @param right the right padding in pixels
     * @param bottom the bottom padding in pixels
     */
    public static void setPadding(View view, int left, int top, int right, int bottom) {
        int scaledLeft = scale(view.getContext(), left);
        int scaledTop = scale(view.getContext(), top);
        int scaledRight = scale(view.getContext(), right);
        int scaledBottom = scale(view.getContext(), bottom);
        view.setPadding(scaledLeft, scaledTop, scaledRight, scaledBottom);
    }
    
    /**
     * 设置 PX margin.
     *
     * @param view the view
     * @param left the left margin in pixels
     * @param top the top margin in pixels
     * @param right the right margin in pixels
     * @param bottom the bottom margin in pixels
     */
    public static void setMargin(View view, int left, int top, int right, int bottom) {
        int scaledLeft = scale(view.getContext(), left);
        int scaledTop = scale(view.getContext(), top);
        int scaledRight = scale(view.getContext(), right);
        int scaledBottom = scale(view.getContext(), bottom);
        
        if (view.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams mMarginLayoutParams = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
            if (mMarginLayoutParams != null) {
                if (left != INVALID) {
                    mMarginLayoutParams.leftMargin = scaledLeft;
                }
                if (right != INVALID) {
                    mMarginLayoutParams.rightMargin = scaledRight;
                }
                if (top != INVALID) {
                    mMarginLayoutParams.topMargin = scaledTop;
                }
                if (bottom != INVALID) {
                    mMarginLayoutParams.bottomMargin = scaledBottom;
                }
                view.setLayoutParams(mMarginLayoutParams);
            }
        }
        
    }
    
}
