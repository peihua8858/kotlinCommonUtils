package com.fz.common.view.utils

import android.graphics.RectF
import android.view.View
import androidx.core.text.TextUtilsCompat
import androidx.core.view.ViewCompat
import com.fz.common.listener.OnNoDoubleClickListener
import java.util.*

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
