@file:JvmName("ViewUtil")

package com.fz.common.view.utils

import android.content.Context
import android.graphics.RectF
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.*
import androidx.core.content.ContextCompat
import androidx.core.text.TextUtilsCompat
import androidx.core.view.ViewCompat
import com.fz.common.listener.OnNoDoubleClickListener
import com.fz.common.utils.checkContext
import com.fz.common.utils.getDimens
import com.fz.common.utils.getResourceId
import com.fz.common.utils.resolveAttribute
import java.util.*

/**
 * 扩展方法，用于获取View
 * @receiver ViewGroup parent
 * @param layoutResId Int
 * @return View
 */
fun ViewGroup.getItemView(@LayoutRes layoutResId: Int): View {
    return LayoutInflater.from(this.context).inflate(layoutResId, this, false)
}

/**
 * 布局方向是从右到左
 *
 * @author dingpeihua
 * @date 2017/8/29 15:11
 * @version 1.0
 */
fun Any?.isAppRtl(): Boolean {
    return TextUtilsCompat.getLayoutDirectionFromLocale(Locale.getDefault()) == ViewCompat.LAYOUT_DIRECTION_RTL
}

fun View?.setVisible(isVisible: Boolean) {
    if (this == null) {
        return
    }
    this.visibility = if (isVisible) View.VISIBLE else View.GONE
}

fun View?.setGone(isGone: Boolean) {
    if (this == null) {
        return
    }
    this.visibility = if (isGone) View.GONE else View.VISIBLE
}

/**
 * 设置多个视图监听器，避免连续单击问题
 * @param listener
 * @author dingpeihua
 * @date 2020/11/29 17:11
 * @version 1.0
 */
fun Any?.setOnNoDoubleClickListener(listener: View.OnClickListener?, vararg views: View?) {
    for (view in views) {
        view.setOnNoDoubleClickListener(listener)
    }
}

/**
 * 设置视图监听器，处理连续单击问题
 * @param listener
 * @author dingpeihua
 * @date 2020/11/25 20:56
 * @version 1.0
 */
fun View?.setOnNoDoubleClickListener(listener: View.OnClickListener?) {
    if (this == null) {
        return
    }
    setOnClickListener(object : OnNoDoubleClickListener() {
        override fun onNoDoubleClick(view: View?) {
            listener?.onClick(view)
        }
    })
}

fun Any?.contains(view: View, x: Float, y: Float): Boolean {
    val rectF = calcViewScreenLocation(view)
    return rectF.contains(x, y)
}

/**
 * 计算指定的 View 在屏幕中的坐标。
 */
fun Any?.calcViewScreenLocation(view: View): RectF {
    val location = IntArray(2)
    // 获取控件在屏幕中的位置，返回的数组分别为控件左顶点的 x、y 的值
    view.getLocationOnScreen(location)
    return RectF(
            location[0].toFloat(), location[1]
            .toFloat(), (location[0] + view.width).toFloat(),
            (location[1] + view.height).toFloat()
    )
}

@ColorInt
fun View.getColor(@ColorRes colorRes: Int): Int {
    val context: Context = checkContext(this) ?: return 0
    return ContextCompat.getColor(context, colorRes)
}

fun View?.getDrawable(@DrawableRes drawableRes: Int): Drawable? {
    val context: Context = checkContext(this) ?: return null
    return ContextCompat.getDrawable(context, drawableRes)
}

fun View?.getDimens(@DimenRes resId: Int): Int {
    val context: Context = checkContext(this) ?: return 0
    return context.getDimens(resId)
}

/**
 * 获取资源id
 *
 * @param attrId 属性id
 * @return drawable对象
 */
fun View?.getResourceId(attrId: Int): Int {
    val context: Context = checkContext(this) ?: return 0
    return context.getResourceId(attrId)
}

/**
 * 解析当前上下文主题，获取主题样式
 *
 * @param context    当前上下文
 * @param resId      资源ID
 * @param defaultRes 默认主题样式
 * @author dingpeihua
 * @date 2020/7/7 10:31
 * @version 1.0
 */
fun View?.resolveAttribute(resId: Int, @StyleRes defaultRes: Int): Int {
    val context: Context = checkContext(this) ?: return 0
    return context.resolveAttribute(resId, defaultRes)
}