@file:JvmName("DrawableUtils")
package com.fz.common.utils

import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.PixelFormat
import android.graphics.Rect
import android.graphics.drawable.*
import android.util.StateSet
import androidx.annotation.ColorInt
import androidx.core.graphics.drawable.DrawableCompat

fun Drawable?.setTint(@ColorInt color: Int): Drawable? {
    if (this != null) {
        DrawableCompat.setTint(this, color)
    }
    return this
}

/**
 * 设置图片可绘制大小
 * @author dingpeihua
 * @date 2021/2/24 10:37
 * @version 1.0
 */
fun Drawable?.setDrawableBounds(width: Int, height: Int): Drawable? {
    if (this != null) {
        val scale = intrinsicHeight.toDouble() / intrinsicWidth.toDouble()
        setBounds(0, 0, width, height)
        val bounds: Rect = bounds
        //高宽只给一个值时，自适应
        if (bounds.right != 0 || bounds.bottom != 0) {
            if (bounds.right == 0) {
                bounds.right = ((bounds.bottom / scale).toInt())
                this.bounds = bounds
            }
            if (bounds.bottom == 0) {
                bounds.bottom = ((bounds.right * scale).toInt())
                this.bounds = bounds
            }
        }
    }
    return this
}

/**
 * 获取旋转指定角度后的Drawable
 *
 * @param d     源Drawable
 * @param angle 角度
 * @return 旋转指定角度后的Drawable
 */
fun Drawable?.getRotateDrawable(angle: Float): Drawable? {
    if (this == null) {
        return null
    }
    val arD = arrayOf(this)
    val bound = this.bounds
    return object : LayerDrawable(arD) {
        override fun draw(canvas: Canvas) {
            canvas.save()
            canvas.rotate(angle, bound.width() / 2.toFloat(), bound.height() / 2.toFloat())
            super.draw(canvas)
            canvas.restore()
        }
    }
}

/**
 * 获取旋转指定角度后的Drawable
 *
 * @param d     源Drawable
 * @param angle 角度
 * @param x     旋转中心x
 * @param y     旋转中心y
 * @return 旋转指定角度后的Drawable
 */
fun Drawable?.getRotateDrawable(angle: Float, x: Float, y: Float): Drawable? {
    if (this == null) {
        return null
    }
    val arD = arrayOf(this)
    return object : LayerDrawable(arD) {
        override fun draw(canvas: Canvas) {
            canvas.save()
            canvas.rotate(angle, x, y)
            super.draw(canvas)
            canvas.restore()
        }
    }
}

/**
 * 获取平移后的Drawable
 *
 * @param d 源Drawable
 * @param x 水平平移量
 * @param y 垂直平移量
 * @return 平移后的Drawable
 */
fun Drawable?.getTranslateDrawable(x: Float, y: Float): Drawable? {
    if (this == null) {
        return null
    }
    val arD = arrayOf(this)
    return object : LayerDrawable(arD) {
        override fun draw(canvas: Canvas) {
            canvas.save()
            canvas.translate(x, y)
            super.draw(canvas)
            canvas.restore()
        }
    }
}

/**
 * 对drawable平滑tint
 *
 * @param drawable   drawable
 * @param startColor 起始颜色
 * @param endColor   结束颜色
 * @param duration   过渡时长
 */
fun Drawable?.tintSmoothly(@ColorInt startColor: Int, @ColorInt endColor: Int, duration: Long) {
    if (this != null) {
        val anim = ValueAnimator()
        anim.setIntValues(startColor, endColor)
        anim.setEvaluator(ArgbEvaluator())
        anim.addUpdateListener { valueAnimator: ValueAnimator -> DrawableCompat.setTint(this, (valueAnimator.animatedValue as Int)) }
        anim.duration = duration
        anim.start()
    }
}

fun Any?.getStateListBgDrawable(normalColor: String?, pressedColor: String?): Drawable {
    val normalColorInt = getColorInt(normalColor, Color.WHITE)
    val pressedColorInt = getColorInt(pressedColor, Color.WHITE)
    return if (normalColorInt != Int.MAX_VALUE && pressedColorInt != Int.MAX_VALUE) {
        DrawableUtil.getStateListBgDrawable(normalColorInt, pressedColorInt)
    } else ColorDrawable(Color.WHITE)
}

/**
 * @author caolz
 * @date 2018/11/16
 * @des Drawable工具类
 */
object DrawableUtil {
    /**
     * 获取旋转指定角度后的Drawable
     *
     * @param d     源Drawable
     * @param angle 角度
     * @return 旋转指定角度后的Drawable
     */
    @JvmStatic
    fun getRotateDrawable(d: Drawable, angle: Float): Drawable {
        val arD = arrayOf(d)
        val bound = d.bounds
        return object : LayerDrawable(arD) {
            override fun draw(canvas: Canvas) {
                canvas.save()
                canvas.rotate(angle, bound.width() / 2.toFloat(), bound.height() / 2.toFloat())
                super.draw(canvas)
                canvas.restore()
            }
        }
    }

    /**
     * 获取旋转指定角度后的Drawable
     *
     * @param d     源Drawable
     * @param angle 角度
     * @param x     旋转中心x
     * @param y     旋转中心y
     * @return 旋转指定角度后的Drawable
     */
    @JvmStatic
    fun getRotateDrawable(d: Drawable, angle: Float, x: Float, y: Float): Drawable {
        val arD = arrayOf(d)
        return object : LayerDrawable(arD) {
            override fun draw(canvas: Canvas) {
                canvas.save()
                canvas.rotate(angle, x, y)
                super.draw(canvas)
                canvas.restore()
            }
        }
    }

    /**
     * 获取平移后的Drawable
     *
     * @param d 源Drawable
     * @param x 水平平移量
     * @param y 垂直平移量
     * @return 平移后的Drawable
     */
    @JvmStatic
    fun getTranslateDrawable(d: Drawable, x: Float, y: Float): Drawable {
        val arD = arrayOf(d)
        return object : LayerDrawable(arD) {
            override fun draw(canvas: Canvas) {
                canvas.save()
                canvas.translate(x, y)
                super.draw(canvas)
                canvas.restore()
            }
        }
    }

    /**
     * 对drawable平滑tint
     *
     * @param drawable   drawable
     * @param startColor 起始颜色
     * @param endColor   结束颜色
     * @param duration   过渡时长
     */
    @JvmStatic
    fun tintSmoothly(drawable: Drawable?, @ColorInt startColor: Int, @ColorInt endColor: Int, duration: Long) {
        if (drawable != null) {
            val anim = ValueAnimator()
            anim.setIntValues(startColor, endColor)
            anim.setEvaluator(ArgbEvaluator())
            anim.addUpdateListener { valueAnimator: ValueAnimator -> DrawableCompat.setTint(drawable, (valueAnimator.animatedValue as Int)) }
            anim.duration = duration
            anim.start()
        }
    }

    @JvmStatic
    fun getStateListBgDrawable(normalColor: String?, pressedColor: String?): Drawable {
        val normalColorInt = getColorInt(normalColor, Color.WHITE)
        val pressedColorInt = getColorInt(pressedColor, Color.WHITE)
        return if (normalColorInt != Int.MAX_VALUE && pressedColorInt != Int.MAX_VALUE) {
            getStateListBgDrawable(normalColorInt, pressedColorInt)
        } else ColorDrawable(Color.WHITE)
    }

    //    public static StateListDrawable getStateListBgDrawable(@ColorInt int normalColor, @ColorInt int pressedColor) {
    //        return getStateListBgDrawable(new ColorDrawable(normalColor), new ColorDrawable(pressedColor));
    //    }
    @JvmStatic
    fun getStateListBgDrawable(@ColorInt normalColor: Int, @ColorInt pressedColor: Int): StateListDrawable {
        //选中效果
//        StateListDrawable stateListDrawable = new StateListDrawable();
//        LayerDrawable layerDrawable = (LayerDrawable) ResourceUtil.getDrawable(R.drawable.layer_for_bg_selector).mutate();
//        GradientDrawable gradientDrawable = (GradientDrawable) layerDrawable.findDrawableByLayerId(R.id.default_color);
//        gradientDrawable.setColor(pressedColor);
//        LayerDrawable layerDrawable2 = (LayerDrawable) ResourceUtil.getDrawable(R.drawable.layer_for_bg_selector).mutate();
//        GradientDrawable gradientDrawable2 = (GradientDrawable) layerDrawable2.findDrawableByLayerId(R.id.default_color);
//        gradientDrawable2.setColor(normalColor);
//        //加载背景
//        stateListDrawable.addState(new int[]{android.R.attr.state_selected}, layerDrawable);
//        stateListDrawable.addState(new int[]{-android.R.attr.state_selected}, layerDrawable2);
        val stateListDrawable = StateListDrawable()
        val drawable = GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, intArrayOf(Color.RED))
        stateListDrawable.addState(intArrayOf(android.R.attr.state_selected), drawable)
        stateListDrawable.addState(StateSet.WILD_CARD, ColorDrawable(Color.YELLOW))
        return stateListDrawable
    }

    /**
     * Copies various properties from one drawable to the other.
     *
     * @param to drawable to copy properties to
     * @param from drawable to copy properties from
     */
    @JvmStatic
    fun copyProperties(to: Drawable?, from: Drawable?) {
        if (from == null || to == null || to === from) {
            return
        }
        to.bounds = from.bounds
        to.changingConfigurations = from.changingConfigurations
        to.level = from.level
        to.setVisible(from.isVisible,  /* restart */false)
        to.state = from.state
    }

    /**
     * Multiplies the color with the given alpha.
     *
     * @param color color to be multiplied
     * @param alpha value between 0 and 255
     * @return multiplied color
     */
    @JvmStatic
    fun multiplyColorAlpha(color: Int, alpha: Int): Int {
        var alpha = alpha
        if (alpha == 255) {
            return color
        }
        if (alpha == 0) {
            return color and 0x00FFFFFF
        }
        alpha = alpha + (alpha shr 7) // make it 0..256
        val colorAlpha = color ushr 24
        val multipliedAlpha = colorAlpha * alpha shr 8
        return multipliedAlpha shl 24 or (color and 0x00FFFFFF)
    }

    /**
     * Gets the opacity from a color. Inspired by Android ColorDrawable.
     *
     * @param color
     * @return opacity expressed by one of PixelFormat constants
     */
    @JvmStatic
    fun getOpacityFromColor(color: Int): Int {
        val colorAlpha = color ushr 24
        return if (colorAlpha == 255) {
            PixelFormat.OPAQUE
        } else if (colorAlpha == 0) {
            PixelFormat.TRANSPARENT
        } else {
            PixelFormat.TRANSLUCENT
        }
    }
}