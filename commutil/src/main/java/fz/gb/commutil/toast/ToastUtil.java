package fz.gb.commutil.toast;

import android.app.Application;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.view.View;
import android.widget.Toast;

/**
 * 优化后的不重复显示的Toast
 *
 * @author Copyright(C)2011-2016 Shyky Studio.
 * @version 1.4
 * @email sj1510706@163.com
 * @date 2016/4/6
 * @since 1.0
 */
public final class ToastUtil {
    private static Toast toast;
    private static Context mContext;
    /**
     * 记录是否已经调用过
     */
    private static boolean isInvoked;

    /**
     * 构造方法私有化
     */
    private ToastUtil() {

    }

    public static void init(Context context){
        mContext=context;
    }

    /**
     * 将Toast对象置空
     */
    private static void setToastNull() {
        // 解决先调用了 void showShortToastMessage()，再调用此方法是报空指针错误
        if (isInvoked)
            toast = null;
    }

    /**
     * 显示Toast提示
     *
     * @param message  提示文本
     * @param duration 停留时间
     */
    private static void showToast(String message, int duration) {
        setToastNull();
        if (toast == null)
            toast = ToastCompat.makeText(mContext, message, duration);
        else
            toast.setText(message);
        toast.show(); // 显示Toast
        isInvoked = false;
    }

    /**
     * 显示Toast提示
     *
     * @param resId    提示文本资源ID
     * @param duration 停留时间
     */
    private static void showToast(int resId, int duration) {
        setToastNull();
        if (toast == null)
            toast = ToastCompat.makeText(mContext, resId, duration);
        else
            toast.setText(resId);
        toast.show(); // 显示Toast提示框
        isInvoked = false;
    }

    /**
     * 短时间Toast提示
     *
     * @param message 要提示的信息
     */
    public static void showShortMessage(@NonNull String message) {
        showToast(message, Toast.LENGTH_SHORT);
    }

    /**
     * 短时间Toast提示
     *
     * @param resId 资源ID，在res/string.xml中配置的字符ID
     */
    public static void showShortMessage(@StringRes int resId) {
        showToast(resId, Toast.LENGTH_SHORT);
    }

    /**
     * 长时间Toast提示
     *
     * @param message 要提示的信息
     */
    public static void showLongMessage(@NonNull String message) {
        showToast(message, Toast.LENGTH_LONG);
    }

    /**
     * 长时间Toast提示
     *
     * @param resId 资源ID
     */
    public static void showLongMessage(@StringRes int resId) {
        showToast(resId, Toast.LENGTH_LONG);
    }

    /**
     * 自定义Toast提示停留时间
     *
     * @param message  要提示的信息
     * @param duration 停留时间毫秒数，以毫秒为单位
     */
    public static void showMessage(@NonNull String message, int duration) {
        showToast(message, duration);
    }

    /**
     * 自定义Toast提示停留时间
     *
     * @param resId    要提示的信息，字符串资源ID
     * @param duration 停留时间毫秒数，以毫秒为单位
     */
    public static void showMessage(@StringRes int resId, int duration) {
        showToast(resId, duration);
    }

    public static void showMessage(View view, int gravity, int offsetY) {
        final Toast toast = new Toast(mContext);
        toast.setGravity(gravity, 0, offsetY);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(view);
        toast.show();
    }

    /**
     * 销毁Toast提示框，解决Activity销毁后还提示问题
     */
    public static void cancel() {
        if (toast != null) {
            toast.cancel();
            toast = null;
        }
    }
}