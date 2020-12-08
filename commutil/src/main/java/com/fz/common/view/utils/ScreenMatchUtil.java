package com.fz.common.view.utils;

import android.app.Activity;
import android.app.Application;
import android.content.ComponentCallbacks;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.Window;

import com.socks.library.KLog;


/**
 * 屏幕适配工具类，可以以屏幕宽度或高度为基准进行适配
 * <pre>
 *   1.先在application中使用{@link #initialize(Activity)} 或 {@link #initialize(Context)}方法初始化一下,
 *    建议在application中初始化
 *   2.手动在Activity中调用{@link #matchWithWidth(Context, float)}
 *     或 {@link #match(Context, float, int)}
 *     或{@link #matchWithWidth(Context, float)}方法做适配，必须在setContentView()之前,如果是全局激活，
 *     则在application中调用{@link #register(Application, float, int)}或{@link #register(Activity, float, int)}
 *   3.建议使用dp做宽度适配，大多数时候宽度适配才是主流需要
 *   4.写布局的时候，使用dp，如果是使用px，建议转化成dp
 *   5.入侵性很低，不需要改动原来的代码
 *  </pre>
 *
 * @author dingpeihua
 * @version 1.0
 * @date 2019/1/30 15:45
 */
public class ScreenMatchUtil {

    /**
     * 以屏幕宽度为基准适配
     */
    public static final int MATCH_BASE_WIDTH = 0;
    /**
     * 以屏幕高度为基准适配
     */
    public static final int MATCH_BASE_HEIGHT = 1;
    /**
     * app 原始显示信息
     */
    private static DisplayMetrics oriDisplayMetrics;
    /**
     * app 原始上下文
     */
    private static Context oriContext;
    /**
     * app 原始屏幕密度
     */
    private static float oriDensity;
    /**
     * app 原始屏幕按比例缩小的密度
     */
    private static float oriScaledDensity;
    private static float newDensity;
    /**
     * Activity 的生命周期监测
     */
    private static Application.ActivityLifecycleCallbacks mActivityLifecycleCallback;

    private ScreenMatchUtil() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    /**
     * 初始化显示信息
     *
     * @param activity 当前activity
     * @author dingpeihua
     * @date 2018/11/26 17:38
     * @version 1.0
     */
    public static void initialize(@NonNull final Activity activity) {
        if (activity == null) {
            return;
        }
        initialize(activity.getApplication());
    }

    /**
     * 初始化
     *
     * @param application 需要在application中初始化
     */
    public static void initialize(@NonNull Context application) {
        if (application == null) {
            return;
        }
        if (oriDisplayMetrics == null) {
            application = application.getApplicationContext();
            Resources resources = application.getResources();
            //注意这个是获取系统的displayMetrics
            final DisplayMetrics displayMetrics = resources.getDisplayMetrics();
            Configuration configuration = new Configuration();
            configuration.setTo(resources.getConfiguration());
            oriContext = application.createConfigurationContext(configuration);
            oriDensity = displayMetrics.density;
            oriScaledDensity = displayMetrics.scaledDensity;
            oriDisplayMetrics = new DisplayMetrics();
            // 记录系统的原始值
            oriDisplayMetrics.setTo(displayMetrics);
            // 添加字体变化的监听
            // 调用 Application#registerComponentCallbacks 注册下 onConfigurationChanged 监听即可。
            application.registerComponentCallbacks(new ComponentCallbacks() {
                @Override
                public void onConfigurationChanged(Configuration newConfig) {
                    // 字体改变后,将 appScaledDensity 重新赋值
                    if (newConfig != null && newConfig.fontScale > 0) {
                        float scaledDensity = displayMetrics.scaledDensity;
                        oriDisplayMetrics.scaledDensity = scaledDensity;
                    }
                }

                @Override
                public void onLowMemory() {

                }
            });
        }
    }

    /**
     * 在 activity 中全局激活适配（也可单独使用 match() 方法在指定页面中配置适配）
     * 在第一个activity中即可
     *
     * @param activity   当前activity
     * @param designSize 设计图的尺寸 （单位: dp）
     * @param matchBase  适配基准{@link #MATCH_BASE_WIDTH} or {@link #MATCH_BASE_HEIGHT}
     */
    @RequiresApi(api = Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    public static void register(@NonNull final Activity activity, final float designSize, final int matchBase) {
        if (activity != null) {
            register(activity.getApplication(), designSize, matchBase);
        }
    }

    /**
     * 在 application 中全局激活适配（也可单独使用 match() 方法在指定页面中配置适配）
     *
     * @param application
     * @param designSize  设计图的尺寸 （单位: dp）
     */
    @RequiresApi(api = Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    public static void register(@NonNull final Application application, final float designSize) {
        register(application, designSize, MATCH_BASE_WIDTH);
    }

    /**
     * 在 application 中全局激活适配（也可单独使用 match() 方法在指定页面中配置适配）
     *
     * @param application
     * @param designSize  设计图的尺寸 （单位: dp）
     * @param matchBase   适配基准{@link #MATCH_BASE_WIDTH} or {@link #MATCH_BASE_HEIGHT}
     */
    @RequiresApi(api = Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    public static void register(@NonNull final Application application, final float designSize, final int matchBase) {
        if (application == null) {
            return;
        }
        match(application, designSize, matchBase);
        if (mActivityLifecycleCallback == null) {
            mActivityLifecycleCallback = new Application.ActivityLifecycleCallbacks() {
                @Override
                public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
                    if (activity != null) {
                        match(activity, designSize, matchBase);
                    }
                }

                @Override
                public void onActivityStarted(Activity activity) {

                }

                @Override
                public void onActivityResumed(Activity activity) {

                }

                @Override
                public void onActivityPaused(Activity activity) {

                }

                @Override
                public void onActivityStopped(Activity activity) {

                }

                @Override
                public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

                }

                @Override
                public void onActivityDestroyed(Activity activity) {

                }
            };
            application.registerActivityLifecycleCallbacks(mActivityLifecycleCallback);
        }
    }


    /**
     * 全局取消所有的适配
     *
     * @param application
     */
    @RequiresApi(api = Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    public static void unregister(@NonNull final Application application) {
        if (mActivityLifecycleCallback != null) {
            application.unregisterActivityLifecycleCallbacks(mActivityLifecycleCallback);
            mActivityLifecycleCallback = null;
        }
        cancelMatch(application);
    }


    /**
     * 适配屏幕（放在 Activity 的 setContentView() 之前执行）
     *
     * @param context    上下文
     * @param designSize 设计图的尺寸 （单位: dp）
     */
    public static void matchWithWidth(@NonNull final Context context, final float designSize) {
        match(context, designSize, MATCH_BASE_WIDTH);
    }

    /**
     * 适配屏幕（放在 Activity 的 setContentView() 之前执行）
     *
     * @param context    上下文
     * @param designSize 设计图的尺寸 （单位: dp）
     * @param matchBase  适配基准{@link #MATCH_BASE_WIDTH} or {@link #MATCH_BASE_HEIGHT}
     */
    public static void match(@NonNull final Context context, final float designSize, int matchBase) {
        if (designSize == 0) {
            throw new UnsupportedOperationException("The designSize cannot be equal to 0");
        }
        matchByDP(context, designSize, matchBase);
    }

    /**
     * 重置适配信息，取消适配
     *
     * @param context 上下文
     */
    public static void cancelMatch(@NonNull final Context context) {
        if (oriDisplayMetrics != null) {
            final DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
            if (displayMetrics.density != oriDisplayMetrics.density) {
                displayMetrics.density = oriDisplayMetrics.density;
            }
            if (displayMetrics.densityDpi != oriDisplayMetrics.densityDpi) {
                displayMetrics.densityDpi = oriDisplayMetrics.densityDpi;
            }
            if (displayMetrics.scaledDensity != oriDisplayMetrics.scaledDensity) {
                displayMetrics.scaledDensity = oriDisplayMetrics.scaledDensity;
            }
        }
    }

    /**
     * 使用 dp 作为适配单位（适合在新项目中使用，在老项目中使用会对原来既有的 dp 值产生影响）
     * <br>
     * <ul>
     * dp 与 px 之间的换算:
     * <li> px = density * dp </li>
     * <li> density = dpi / 160 </li>
     * <li> px = dp * (dpi / 160) </li>
     * </ul>
     *
     * @param context    上下文
     * @param designSize 设计图的宽/高（单位: dp）
     * @param base       适配基准{@link #MATCH_BASE_WIDTH} or {@link #MATCH_BASE_HEIGHT}
     */
    private static void matchByDP(@NonNull final Context context, final float designSize, int base) {
        if (context == null) {
            return;
        }
        final int width = oriDisplayMetrics.widthPixels;
        final int height = oriDisplayMetrics.heightPixels;
        final int targetPixel;
        if (base == MATCH_BASE_HEIGHT) {
            targetPixel = Math.max(width, height);
        } else {
            targetPixel = Math.min(width, height);
        }
        final float targetDensity = targetPixel * 1f / designSize;
        KLog.d("ScreenMatch>>>>>designSize:" + designSize);
        KLog.d("ScreenMatch>>>>>targetPixel:" + targetPixel);
        KLog.d("ScreenMatch>>>>>targetDensity:" + targetDensity);
        KLog.d("ScreenMatch>>>>>heightPixels:" + height);
        KLog.d("ScreenMatch>>>>>widthPixels:" + width);
        final int targetDensityDpi = (int) (targetDensity * 160);
        final float targetScaledDensity = targetDensity * (oriDisplayMetrics.scaledDensity / oriDisplayMetrics.density);
        final DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        displayMetrics.density = targetDensity;
        displayMetrics.densityDpi = targetDensityDpi;
        displayMetrics.scaledDensity = targetScaledDensity;
    }

    /**
     * 获取系统状态栏高度
     *
     * @param context 当前上下文
     * @author dingpeihua
     * @date 2019/9/2 19:06
     * @version 1.0
     */
    public static int getStatusHeight(Context context) {
        int statusHeight = getSysResourceDimensionPixelSize(context, "status_bar_height");
        Log.i("TAG", "statusBarHeight:" + statusHeight);
        return statusHeight <= 0 ? ScreenMatchUtil.dip2px(24) : statusHeight;
    }

    /**
     * 获取底部导航栏高度
     *
     * @param context 当前上下文
     * @author dingpeihua
     * @date 2019/9/2 18:59
     * @version 1.0
     */
    public static int getNavigationBarHeight(Context context) {
        int navigationBarHeight = getSysResourceDimensionPixelSize(context, "navigation_bar_height");
        Log.i("TAG", "navigationBarHeight:" + navigationBarHeight);
        return navigationBarHeight <= 0 ? ScreenMatchUtil.dip2px(48) : navigationBarHeight;
    }


    /**
     * 获取系统资源id
     *
     * @author dingpeihua
     * @date 2019/9/2 19:04
     * @version 1.0
     */
    public static int getSystemIdentifier(Resources resources, String name) {
        return resources.getIdentifier(name, "dimen", "android");
    }

    /**
     * 获取系统资源
     *
     * @author dingpeihua
     * @date 2019/9/2 19:02
     * @version 1.0
     */
    public static int getSysResourceDimensionPixelSize(Context context, String name) {
        try {
            Resources resources = context.getResources();
            return resources.getDimensionPixelSize(getSystemIdentifier(resources, name));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    /**
     * dp转换成px
     *
     * @param dp 独立像素单位
     * @return dp对应的像素
     */
    public static int dp2px(int dp) {
        return (int) (dp * oriDensity + 0.5f);
    }

    /**
     * dip转换成px
     *
     * @param dip dip单位
     * @return dp对应的像素
     */
    public static int dip2px(int dip) {
        return dp2px(dip);
    }

    /**
     * px转换成dp
     *
     * @param px 像素单位
     * @return 像素px对应的独立像素dp
     */
    public static int px2dp(int px) {
        return (int) (px / oriDensity + 0.5f);
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     *
     * @param dip dip单位
     * @return dp对应的像素单位px
     */
    public static int dip2px(float dip) {
        return (int) (dip * oriDensity + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     *
     * @param px 像素单位
     * @return 像素px对应的独立像素dp
     */
    public static int px2dip(float px) {
        return (int) (px / oriDensity + 0.5f);
    }

    /**
     * px值转换为sp值，保证文字大小不变
     *
     * @param px 像素单位
     * @return px对应的字体大小单位
     */
    public static int px2sp(float px) {
        return (int) (px / oriDensity + 0.5f);
    }

    /**
     * sp值转换为px值，保证文字大小不变
     *
     * @param sp 字体大小单位
     * @return sp对应的像素
     */
    public static int sp2px(float sp) {
        return (int) (sp * oriScaledDensity + 0.5f);
    }

    /**
     * 获取屏幕可视区域，不包括底部导航栏
     *
     * @param rect
     * @param context
     * @author dingpeihua
     * @date 2019/11/12 15:51
     * @version 1.0
     */
    public static Rect getVisibleDisplayFrame(Context context, Rect rect) {
        if (rect == null) {
            rect = new Rect();
        }
        if (context instanceof Activity) {
            Window window = ((Activity) context).getWindow();
            if (window != null) {
                View decorView = window.getDecorView();
                decorView.getWindowVisibleDisplayFrame(rect);
            }
        }
        return rect;
    }
}
