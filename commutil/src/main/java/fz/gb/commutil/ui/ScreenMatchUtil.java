package fz.gb.commutil.ui;

import android.app.Activity;
import android.app.Application;
import android.content.ComponentCallbacks;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.util.DisplayMetrics;

import fz.gb.commutil.network.NetworkType;

/**
 * 屏幕适配工具类，可以以屏幕宽度或高度为基准进行适配
 * <pre>
 * 1.先在application中使用{@link #initialize(Activity)} 或 {@link #initialize(Application)}方法初始化一下,
 *   建议在application中初始化
 * 2.手动在Activity中调用{@link #match(Context, float)}
 *   或 {@link #match(Context, float, int)}
 *   或{@link #match(Context, float)}方法做适配，必须在setContentView()之前,如果是全局激活，
 *   则在application中调用{@link #register(Application, float, int)}或{@link #register(Activity, float, int)}
 * 3.建议使用dp做宽度适配，大多数时候宽度适配才是主流需要
 * 4.写布局的时候，使用dp，如果是使用px，建议转化成dp
 * 5.入侵性很低，不需要改动原来的代码
 * </pre>
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
    public static void initialize(@NonNull final Application application) {
        if (application == null) {
            return;
        }
        if (oriDisplayMetrics == null) {
            //注意这个是获取系统的displayMetrics
            final DisplayMetrics displayMetrics = application.getResources().getDisplayMetrics();
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
    public static void match(@NonNull final Context context, final float designSize) {
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
        final float targetDensity;
        if (base == MATCH_BASE_WIDTH) {
            targetDensity = oriDisplayMetrics.widthPixels * 1f / designSize;
        } else if (base == MATCH_BASE_HEIGHT) {
            targetDensity = oriDisplayMetrics.heightPixels * 1f / designSize;
        } else {
            targetDensity = oriDisplayMetrics.widthPixels * 1f / designSize;
        }
        final int targetDensityDpi = (int) (targetDensity * 160);
        final float targetScaledDensity = targetDensity * (oriDisplayMetrics.scaledDensity / oriDisplayMetrics.density);
        final DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        displayMetrics.density = targetDensity;
        displayMetrics.densityDpi = targetDensityDpi;
        displayMetrics.scaledDensity = targetScaledDensity;

    }
}
