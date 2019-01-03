package fz.gb.commutil.ui;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Point;
import android.os.Build;
import android.util.TypedValue;
import android.view.Display;
import android.view.WindowManager;

/**
 * @author Administrator
 * 
 */
public class ScreenHelper {

    private static int mWidth;
    private static int mHeight;

    public static int getScreenWidth(Context context) {
	if (mWidth == 0) {
	    calculateScreenDimensions(context);
	}
	return mWidth;
    }

    public static int getScreenHeight(Context context) {
	if (mHeight == 0) {
	    calculateScreenDimensions(context);
	}
	return mHeight;
    }

    @SuppressWarnings("deprecation")
    private static void calculateScreenDimensions(Context context) {
	WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
	Display display = wm.getDefaultDisplay();

	if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
	    final Point point = new Point();
	    display.getSize(point);
	    mWidth = point.x;
	    mHeight = point.y;
	} else {
	    mWidth = display.getWidth();
	    mHeight = display.getHeight();
	}
    }

    public static int dpToPx(Context context, int dpValue) {
	final float scale = context.getResources().getDisplayMetrics().density;
	return (int) (dpValue * scale + 0.5f);

    }

    public static int pxToDp(Context context, int pxValue) {
	final float scale = context.getResources().getDisplayMetrics().density;
	return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 将px值转换为sp值
     */
    public static int px2sp(Context context, float pxValue) {
	final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
	return (int) (pxValue / fontScale + 0.5f);
    }

    /**
     * 将sp值转换为px值
     * 
     */
    public static int sp2px(Context context, float spValue) {
	final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
	return (int) (spValue * fontScale + 0.5f);
    }

    public static final int dpToPx(float dp, Resources res) {
	return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, res.getDisplayMetrics());
    }

}
