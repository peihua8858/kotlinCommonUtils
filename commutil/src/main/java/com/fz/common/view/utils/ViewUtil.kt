@file:JvmName("ViewUtil")
@file:JvmMultifileClass

package com.fz.common.view.utils

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.RectF
import android.graphics.drawable.Drawable
import android.util.Size
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.*
import androidx.core.content.ContextCompat
import androidx.core.text.TextUtilsCompat
import androidx.core.view.ViewCompat
import androidx.core.view.ViewPropertyAnimatorListener
import androidx.core.view.marginBottom
import androidx.core.view.marginEnd
import androidx.core.view.marginStart
import androidx.core.view.marginTop
import androidx.interpolator.view.animation.FastOutSlowInInterpolator
import com.fz.common.listener.OnNoDoubleClickListener
import com.fz.common.utils.checkContext
import com.fz.common.utils.dLog
import com.fz.common.utils.getDimen
import com.fz.common.utils.getDimenPixel
import com.fz.common.utils.getDimenPixelOffset
import com.fz.common.utils.getDimens
import com.fz.common.utils.getResourceId
import com.fz.common.utils.getStringArray
import com.fz.common.utils.isAppRtl
import com.fz.common.utils.isLandScape
import com.fz.common.utils.isN
import com.fz.common.utils.resolveAttribute
import java.util.*
import kotlin.math.max

fun View.getDimensionPixelSize(id: Int): Int {
    return resources.getDimensionPixelSize(id)
}

fun View.getDimension(id: Int): Float {
    return resources.getDimension(id)
}

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
 * 扩展方法，用于获取View
 * @receiver ViewGroup parent
 * @param layoutResId Int
 * @return View
 */
fun Context.getItemView(@LayoutRes layoutResId: Int): View {
    return LayoutInflater.from(this).inflate(layoutResId, null)
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

@Deprecated(
    "Please use androidx.core.view.isVisible, This method will be removed in the next version",
    replaceWith = ReplaceWith("androidx.core.view.isVisible")
)
fun View?.setVisible(isVisible: Boolean) {
    if (this == null) {
        return
    }
    this.visibility = if (isVisible) View.VISIBLE else View.GONE
}

@Deprecated(
    "Please use androidx.core.view.isVisible, This method will be removed in the next version",
    replaceWith = ReplaceWith("androidx.core.view.isVisible")
)
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
    listener?.let {
        setOnClickListener(OnNoDoubleClickListener(it))
    }
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

fun View?.getString(@StringRes resourceId: Int, vararg formatArgs: Any?): String {
    val context: Context = checkContext(this) ?: return ""
    return context.getString(resourceId, *formatArgs)
}

fun View?.getString(@StringRes resourceId: Int): String {
    val context: Context = checkContext(this) ?: return ""
    return context.getString(resourceId)
}

fun View.getDimen(id: Int): Float {
    val context: Context = checkContext(this) ?: return 0f
    return context.getDimen(id)
}

fun View.getDimenPixel(id: Int): Int {
    val context: Context = checkContext(this) ?: return 0
    return context.getDimenPixel(id)
}

fun View.getDimenPixelOffset(id: Int): Int {
    val context: Context = checkContext(this) ?: return 0
    return context.getDimenPixelOffset(id)
}

fun View.getDimenPixelSize(id: Int): Int {
    val context: Context = checkContext(this) ?: return 0
    return context.getDimenPixelOffset(id)
}

fun View.getStringArray(id: Int): Array<String> {
    val context: Context = checkContext(this) ?: return arrayOf()
    return context.getStringArray(id)
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

fun View?.setMarginBottom(@Px bottom: Int) {
    if (this == null) return
    (layoutParams as? ViewGroup.MarginLayoutParams)?.bottomMargin = bottom
}

fun View?.setMarginBottomByDp(bottom: Int) {
    if (this == null) return
    (layoutParams as? ViewGroup.MarginLayoutParams)?.bottomMargin = dip2px(bottom)
}

fun View?.setMarginTop(@Px top: Int) {
    if (this == null) return
    (layoutParams as? ViewGroup.MarginLayoutParams)?.topMargin = top
}

fun View?.setMarginTopByDp(top: Int) {
    if (this == null) return
    (layoutParams as? ViewGroup.MarginLayoutParams)?.topMargin = dip2px(top)
}

fun View?.setMarginEnd(@Px end: Int) {
    if (this == null) return
    (layoutParams as? ViewGroup.MarginLayoutParams)?.marginEnd = end
}

fun View?.setMarginEndByDp(end: Int) {
    if (this == null) return
    (layoutParams as? ViewGroup.MarginLayoutParams)?.marginEnd = dip2px(end)
}

fun View?.setMarginStart(@Px start: Int) {
    if (this == null) return
    (layoutParams as? ViewGroup.MarginLayoutParams)?.marginStart = start
}

fun View?.setMarginStartByDp(start: Int) {
    if (this == null) return
    (layoutParams as? ViewGroup.MarginLayoutParams)?.marginStart = dip2px(start)
}

fun View?.setMargin(@Px start: Int, @Px top: Int, @Px end: Int, @Px bottom: Int) {
    if (this == null) return
    val layoutParams = layoutParams as ViewGroup.MarginLayoutParams
    layoutParams.marginStart = start
    layoutParams.topMargin = top
    layoutParams.marginEnd = end
    layoutParams.bottomMargin = bottom
    this.layoutParams = layoutParams
}

fun View?.setMarginByDp(start: Int, top: Int, end: Int, bottom: Int) {
    if (this == null) return
    val layoutParams = layoutParams as ViewGroup.MarginLayoutParams
    layoutParams.marginStart = dip2px(start)
    layoutParams.topMargin = dip2px(top)
    layoutParams.marginEnd = dip2px(end)
    layoutParams.bottomMargin = dip2px(bottom)
    this.layoutParams = layoutParams
}

/**
 * 设置控件内边距
 *
 * @param view          目标视视图
 * @param paddingStart  左边距
 * @param paddingTop    上边距
 * @param paddingEnd    右边距
 * @param paddingBottom 下边距
 * @author dingpeihua
 * @date 2018/12/7 18:09
 * @version 1.0
 */
fun View?.setViewPaddingPx(
    @Px paddingStart: Int,
    @Px paddingTop: Int,
    @Px paddingEnd: Int,
    @Px paddingBottom: Int,
) {
    if (this == null) return
    setPaddingRelative(
        max(paddingStart, 0),
        max(paddingTop, 0),
        max(paddingEnd, 0),
        max(paddingBottom, 0)
    )
}

/**
 * 设置控件内边距
 *
 * @param view          目标视视图
 * @param paddingStart  左边距
 * @param paddingTop    上边距
 * @param paddingEnd    右边距
 * @param paddingBottom 下边距
 * @author dingpeihua
 * @date 2018/12/7 18:09
 * @version 1.0
 */
fun View?.setViewPaddingDp(
    paddingStart: Int,
    paddingTop: Int,
    paddingEnd: Int,
    paddingBottom: Int,
) {
    if (this == null) return
    setViewPaddingPx(
        dip2px(paddingStart),
        dip2px(paddingTop),
        dip2px(paddingEnd),
        dip2px(paddingBottom)
    )
}


fun View?.setPaddingBottom(@Px bottom: Int) {
    if (this == null) return
    setViewPaddingPx(paddingStart, paddingTop, paddingEnd, bottom)
}

fun View?.setPaddingTop(@Px top: Int) {
    if (this == null) return
    setViewPaddingPx(paddingStart, top, paddingEnd, paddingBottom)
}

fun View?.setPaddingEnd(@Px end: Int) {
    if (this == null) return
    setViewPaddingPx(paddingStart, paddingTop, end, paddingBottom)
}

fun View?.setPaddingStart(@Px start: Int) {
    if (this == null) return
    setViewPaddingPx(start, paddingTop, paddingEnd, paddingBottom)
}


fun View?.setPaddingBottomByDp(bottom: Int) {
    if (this == null) return
    setViewPaddingPx(paddingStart, paddingTop, paddingEnd, dip2px(bottom))
}

fun View?.setPaddingTopByDp(top: Int) {
    if (this == null) return
    setViewPaddingPx(paddingStart, dip2px(top), paddingEnd, paddingBottom)
}

fun View?.setPaddingEndByDp(end: Int) {
    if (this == null) return
    setViewPaddingPx(paddingStart, paddingTop, dip2px(end), paddingBottom)
}

fun View?.setPaddingStartByDp(start: Int) {
    if (this == null) return
    setViewPaddingPx(dip2px(start), paddingTop, paddingEnd, paddingBottom)
}

/**
 * [View]宽度展开折叠动画
 * @param   isExpend  true:展开  false:收起
 * @param   width     展开时的宽度
 * @param duration 动画时长
 * @author dingpeihua
 * @date 2022/5/14 19:12
 * @version 1.0
 */
fun View.animationWidth(isExpend: Boolean, width: Int, duration: Long = 300) {
    val viewWrapper = ViewWrapper(this)
    val animation = if (isExpend) ObjectAnimator.ofInt(
        viewWrapper, "width", 0, width
    )
    else ObjectAnimator.ofInt(viewWrapper, "width", width, 0)
    animation.duration = duration
    animation.addListener(object : AnimatorListenerAdapter() {
        override fun onAnimationStart(animation: Animator) {
            if (isExpend) this@animationWidth.visibility = View.VISIBLE
        }

        override fun onAnimationEnd(animation: Animator) {
            if (!isExpend) this@animationWidth.visibility = View.GONE
        }
    })
    animation.addUpdateListener {
        viewWrapper.setWidth(it.animatedValue as Int)
    }
    animation.start()
}

fun View?.setPadding(view: View) {
    this?.setPadding(view.paddingStart, view.paddingTop, view.paddingEnd, view.paddingBottom)
}

fun View?.setMargin(view: View) {
    setMargin(view.marginStart, view.marginTop, view.marginEnd, view.marginBottom)
}

/**
 * [View]宽度展开折叠动画
 * @param   isExpend  true:展开  false:收起
 * @param   width     展开时的宽度
 * @param duration 动画时长
 * @author dingpeihua
 * @date 2022/5/14 19:12
 * @version 1.0
 */
fun View.animationWidth(
    isExpend: Boolean, width: Int, duration: Long = 300,
    model: (AnimatorListenerModel<Animator>.() -> Unit)? = null,
) {
    val viewWrapper = ViewWrapper(this)
    val animation = if (isExpend) ObjectAnimator.ofInt(
        viewWrapper, "width", 0, width
    )
    else ObjectAnimator.ofInt(viewWrapper, "width", width, 0)
    animation.duration = duration
    val listener = if (model != null) AnimatorListenerModel<Animator>().apply(model) else null
    animation.addListener(object : InternalAnimatorListenerAdapter(listener) {
        override fun onAnimationStart(animation: Animator) {
            super.onAnimationStart(animation)
            if (isExpend) this@animationWidth.visibility = View.VISIBLE
        }

        override fun onAnimationEnd(animation: Animator) {
            super.onAnimationEnd(animation)
            if (!isExpend) this@animationWidth.visibility = View.GONE
        }
    })
    animation.addUpdateListener {
        viewWrapper.setWidth(it.animatedValue as Int)
    }
    animation.start()
}

internal open class InternalAnimatorListenerAdapter(private val model: AnimatorListenerModel<Animator>?) :
    AnimatorListenerAdapter() {

    @CallSuper
    override fun onAnimationStart(animation: Animator) {
        model?.invokeAnimationStart(animation)
    }

    @CallSuper
    override fun onAnimationEnd(animation: Animator) {
        model?.invokeAnimationEnd(animation)
    }

    @CallSuper
    override fun onAnimationCancel(animation: Animator) {
        model?.invokeAnimationCancel(animation)
    }

    @CallSuper
    override fun onAnimationRepeat(animation: Animator) {
        model?.invokeAnimationRepeat(animation)
    }

    @CallSuper
    override fun onAnimationPause(animation: Animator) {
        model?.invokeAnimationPause(animation)
    }

    @CallSuper
    override fun onAnimationResume(animation: Animator) {
        model?.invokeAnimationResume(animation)
    }
}

private class ViewWrapper(val view: View) {

    @Keep
    fun setWidth(width: Int) {
        view.layoutParams.width = width
        view.requestLayout() //必须调用，否则宽度改变但UI没有刷新
    }

    fun getWidth(): Int {
        return view.layoutParams.width
    }
}

fun View.measureSize(): Size {
    val widthSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
    val heightSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
    measure(widthSpec, heightSpec)
    return Size(measuredWidth, measuredHeight)
}

fun View.measureHeight(maxWidth: Int): Int {
    val widthSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
    val heightSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
    layoutParams.width = maxWidth
    measure(widthSpec, heightSpec)
    return measuredHeight
}

fun View.isRtl(): Boolean {
    if (isN) {
        return resources.configuration.locales.get(0).isAppRtl()
    }
    return resources.configuration.locale.isAppRtl()
}

/**
 * 弹出动画
 *
 */
fun View.animateOut(
    isVertical: Boolean = false,
    offset: Int = if (isVertical) this.height else this.width,
    duration: Long = 300,
    model: (AnimatorListenerModel<View>.() -> Unit)? = null,
) {
    try {
        var tempOffset = offset
        if (tempOffset == 0) {
            measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)
            tempOffset = if (isVertical) measuredHeight else measuredWidth
        }
        val isRtl: Boolean = isRtl()
        val animate = ViewCompat.animate(this)
        if (isVertical) {
            animate.translationY((tempOffset + marginBottom).toFloat())
        } else {
            val offsetO = (tempOffset + marginEnd).toFloat()
            animate.translationX((if (isRtl) -offsetO else +offsetO))
        }
        if (model != null) {
            val listener = AnimatorListenerModel<View>().apply(model)
            animate.setListener(InternalViewPropertyAnimatorListener(listener))
        }
        animate.duration = duration
        animate.setInterpolator(FastOutSlowInInterpolator()).withLayer()
            .start()
    } catch (e: java.lang.Exception) {
        e.printStackTrace()
    }
}

/**
 * 弹入动画
 *
 */
fun View.animateIn(
    isVertical: Boolean = false,
    duration: Long = 300,
    model: (AnimatorListenerModel<View>.() -> Unit)? = null,
) {
    dLog { "newState>>>>animateIn" }
    visibility = View.VISIBLE
    val animate = ViewCompat.animate(this)
    if (isVertical) {
        animate.translationY(0f)
    } else {
        animate.translationX(0f)
    }
    if (model != null) {
        val listener = AnimatorListenerModel<View>().apply(model)
        animate.setListener(InternalViewPropertyAnimatorListener(listener))
    }
    animate.duration = duration
    animate.setInterpolator(FastOutSlowInInterpolator()).withLayer()
        .start()
}

/**
 * 透明度动画
 *
 */
fun View.animateAlpha(
    isVisible: Boolean = true,
    duration: Long = 300,
    model: (AnimatorListenerModel<View>.() -> Unit)? = null,
) {
    dLog { "newState>>>>animateAlpha" }
    visibility = View.VISIBLE
    val animate = ViewCompat.animate(this)
    animate.alpha(if (isVisible) 1f else 0f)
    if (model != null) {
        val listener = AnimatorListenerModel<View>().apply(model)
        animate.setListener(InternalViewPropertyAnimatorListener(listener))
    }
    animate.duration = duration
    animate.setInterpolator(FastOutSlowInInterpolator()).withLayer()
        .start()
}

internal class InternalViewPropertyAnimatorListener(private val model: AnimatorListenerModel<View>) :
    ViewPropertyAnimatorListener {
    @CallSuper
    override fun onAnimationStart(view: View) {
        model.invokeAnimationStart(view)
    }

    @CallSuper
    override fun onAnimationEnd(view: View) {
        model.invokeAnimationEnd(view)
    }

    @CallSuper
    override fun onAnimationCancel(view: View) {
        model.invokeAnimationCancel(view)
    }

}

class AnimatorListenerModel<T> {
    private var onAnimationStart: ((T) -> Unit)? = null
    private var onAnimationEnd: ((T) -> Unit)? = null
    private var onAnimationCancel: ((T) -> Unit)? = null
    private var onAnimationRepeat: ((T) -> Unit)? = null
    private var onAnimationPause: ((T) -> Unit)? = null
    private var onAnimationResume: ((T) -> Unit)? = null
    infix fun onAnimationStart(onStart: ((T) -> Unit)?): AnimatorListenerModel<T> {
        this.onAnimationStart = onStart
        return this
    }

    infix fun onAnimationEnd(onEnd: ((T) -> Unit)?): AnimatorListenerModel<T> {
        this.onAnimationEnd = onEnd
        return this
    }

    infix fun onAnimationCancel(onCancel: ((T) -> Unit)?): AnimatorListenerModel<T> {
        this.onAnimationCancel = onCancel
        return this
    }

    infix fun onAnimationRepeat(onRepeat: ((T) -> Unit)?): AnimatorListenerModel<T> {
        this.onAnimationRepeat = onRepeat
        return this
    }

    infix fun onAnimationPause(onPause: ((T) -> Unit)?): AnimatorListenerModel<T> {
        this.onAnimationPause = onPause
        return this
    }

    infix fun onAnimationResume(onResume: ((T) -> Unit)?): AnimatorListenerModel<T> {
        this.onAnimationResume = onResume
        return this
    }

    fun invokeAnimationStart(view: T) {
        this.onAnimationStart?.invoke(view)
    }

    fun invokeAnimationEnd(view: T) {
        this.onAnimationEnd?.invoke(view)
    }

    fun invokeAnimationCancel(view: T) {
        this.onAnimationCancel?.invoke(view)
    }

    fun invokeAnimationRepeat(view: T) {
        this.onAnimationRepeat?.invoke(view)
    }

    fun invokeAnimationPause(view: T) {
        this.onAnimationPause?.invoke(view)
    }

    fun invokeAnimationResume(view: T) {
        this.onAnimationResume?.invoke(view)
    }
}

val View.isLandScape: Boolean
    get() = context.isLandScape
