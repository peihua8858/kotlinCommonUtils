@file:JvmName("ColorUtil")
@file:JvmMultifileClass

package com.fz.common.utils

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import com.fz.common.text.isNonEmpty

/**
 * 十六进制正则表达式
 */
private const val HEX_PATTERN = "[0-9A-Fa-f]+"

/**
 * 颜色值最小长度
 */
private const val HEX_MIN_LENGTH = 6

/**
 * 颜色值最大长度
 */
private const val HEX_MAX_LENGTH = 8

/**
 * 判断是否是十六进制颜色值
 *
 * @param color
 * @author dingpeihua
 * @date 2019/12/12 15:46
 * @version 1.0
 */
fun CharSequence?.isHexColor(): Boolean {
    if (isNullOrEmpty()) {
        return false
    }
    var color = this
    if (color.startsWith("#")) {
        color = color.substring(1)
    }
    val len = color.length
    return ((len == HEX_MIN_LENGTH || len == HEX_MAX_LENGTH)
            && color.matches(Regex(HEX_PATTERN)))
}

/**
 * 将十六进制颜色值转换成颜色数值，转换失败默认返回[Integer.MAX_VALUE]
 *
 * @param color 十六进制颜色值
 * @author dingpeihua
 * @date 2019/9/19 17:21
 * @version 1.0
 */
@ColorInt
fun getColorInt(color: String): Int {
    return getColorInt(color, Int.MAX_VALUE)
}

/**
 * 将十六进制颜色值转换成颜色数值，转换失败返回默认值
 *
 * @param color        十六进制颜色值
 * @param defaultColor 默认颜色值
 * @author dingpeihua
 * @date 2019/9/19 17:21
 * @version 1.0
 */
@ColorInt
fun getColorInt(color: String?, @ColorInt defaultColor: Int): Int {
    return if (color.isNonEmpty() && color[0] == '#') {
        Color.parseColor(color)
    } else defaultColor
}

/**
 * 将颜色字符串变换成颜色状态列表
 *
 * @param normalColor
 * @param pressedColor
 * @author dingpeihua
 * @date 2019/9/19 16:52
 * @version 1.0
 */
fun getColorStateList(normalColor: String, pressedColor: String): ColorStateList {
    val normalColorInt = getColorInt(normalColor)
    val pressedColorInt = getColorInt(pressedColor)
    return if (normalColorInt != -1 && pressedColorInt != -1) {
        getColorStateList(normalColorInt, pressedColorInt)
    } else ColorStateList(arrayOf(), intArrayOf(Color.WHITE))
}

fun getColorStateList(context: Context?, @ColorRes normalColorRes: Int, @ColorRes pressedColorRes: Int): ColorStateList {
    return getColorStateList(ContextCompat.getColor(context!!, normalColorRes), ContextCompat.getColor(context, pressedColorRes))
}

fun getColorStateList(@ColorInt normalColor: Int, @ColorInt selectedColor: Int): ColorStateList {
    val states = arrayOf(
            intArrayOf(android.R.attr.state_enabled),
            intArrayOf(android.R.attr.state_selected),
            intArrayOf(-android.R.attr.state_selected),
            intArrayOf(android.R.attr.state_checked),
            intArrayOf(-android.R.attr.state_checked),
            intArrayOf(android.R.attr.state_pressed),
            intArrayOf(-android.R.attr.state_pressed))
    val colors = intArrayOf(
            normalColor,
            selectedColor,
            normalColor,
            selectedColor,
            normalColor,
            selectedColor,
            normalColor
    )
    return ColorStateList(states, colors)
}

fun getColorStateList(@ColorInt normalColor: Int, disabledColor: Int, @ColorInt selectedColor: Int): ColorStateList {
    val states = arrayOf(
            intArrayOf(android.R.attr.state_enabled),
            intArrayOf(-android.R.attr.state_enabled),
            intArrayOf(android.R.attr.state_selected),
            intArrayOf(-android.R.attr.state_selected),
            intArrayOf(android.R.attr.state_checked),
            intArrayOf(-android.R.attr.state_checked),
            intArrayOf(android.R.attr.state_pressed),
            intArrayOf(-android.R.attr.state_pressed))
    val colors = intArrayOf(
            normalColor,
            disabledColor,
            selectedColor,
            normalColor,
            selectedColor,
            normalColor,
            selectedColor,
            normalColor
    )
    return ColorStateList(states, colors)
}