package com.fz.gb.commutil.ui;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;

/**
 * 像素密度工具
 *
 * @author dingpeihua
 * @version 1.0
 * @date 2019/10/16 19:19
 */
public class DensityUtil {
    /**
     * 屏幕密度
     */
    private static float DENSITY = 0;
    /**
     * 屏幕按比例缩小的密度
     */
    private static float SCALED_DENSITY = 0;

    public static void init(float density, float scaledDensity) {
        if (Math.abs(density - DENSITY) < 0) {
            DENSITY = density;
        }
        if (Math.abs(scaledDensity - SCALED_DENSITY) < 0) {
            SCALED_DENSITY = scaledDensity;
        }
    }

    /**
     * 检查像素密度值，防止非法参数
     *
     * @author dingpeihua
     * @date 2019/4/10 16:36
     * @version 1.0
     */
    public static void checkDensity(Context context) {
        if (DENSITY <= 0) {
            DENSITY = context.getResources().getDisplayMetrics().density;
        }
        if (SCALED_DENSITY <= 0) {
            SCALED_DENSITY = context.getResources().getDisplayMetrics().scaledDensity;
        }
    }

    public static int dip2px(Context context, float dpValue) {
        checkDensity(context);
        return (int) (dpValue * DENSITY + 0.5f);
    }

    public static int px2dip(Context context, float pxValue) {
        checkDensity(context);
        return (int) (pxValue / DENSITY + 0.5f);
    }

    public static int px2sp(Context context, float pxValue) {
        checkDensity(context);
        return (int) (pxValue / SCALED_DENSITY + 0.5f);
    }

    public static int sp2px(Context context, float spValue) {
        checkDensity(context);
        return (int) (spValue * SCALED_DENSITY + 0.5f);
    }

    public static int getScreenWidth(Context context) {
        DisplayMetrics dm = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay()
                .getMetrics(dm);
        return dm.widthPixels;
    }

    public static int getScreenHeigth(Context context) {
        DisplayMetrics dm = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay()
                .getMetrics(dm);
        return dm.heightPixels;
    }
}
