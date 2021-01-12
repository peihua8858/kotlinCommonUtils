@file:JvmName("BaseViewHolderUtil")
@file:JvmMultifileClass
package com.fz.common.utils

import android.graphics.Typeface
import android.text.util.Linkify
import android.view.View
import android.view.ViewGroup
import android.widget.Checkable
import android.widget.ProgressBar
import android.widget.RatingBar
import android.widget.TextView
import androidx.annotation.IdRes
import com.chad.library.adapter.base.BaseViewHolder

/**
 * 设置选中状态
 * @param id
 * @param isChecked
 * @author dingpeihua
 * @date 2020/10/18 13:57
 * @version 1.0
 */
fun BaseViewHolder?.setChecked(@IdRes id: Int, isChecked: Boolean): BaseViewHolder? {
    val checkable: Checkable? = this?.getView(id)
    checkable?.isChecked = isChecked
    return this
}

/**
 * 设置选中状态
 * @param id
 * @param isSelected
 * @author dingpeihua
 * @date 2020/10/18 13:57
 * @version 1.0
 */
fun BaseViewHolder?.setSelected(@IdRes id: Int, isSelected: Boolean): BaseViewHolder? {
    val view: View? = this?.getView(id)
    view?.isSelected = isSelected
    return this
}

fun BaseViewHolder?.setAlpha(@IdRes viewId: Int, value: Float): BaseViewHolder? {
    this?.getView<View>(viewId)?.alpha = value
    return this
}

fun BaseViewHolder?.setTypeface(@IdRes viewId: Int, typeface: Typeface?): BaseViewHolder? {
    val view: TextView? = this?.getView(viewId)
    view?.typeface = typeface
    view?.paintFlags = view?.paintFlags?.or(128) ?: 0
    return this
}

fun BaseViewHolder?.setTypeface(typeface: Typeface?, vararg viewIds: Int): BaseViewHolder? {
    val var4 = viewIds.size
    for (var5 in 0 until var4) {
        val viewId = viewIds[var5]
        val view: TextView? = this?.getView(viewId)
        view?.typeface = typeface
        view?.paintFlags = view?.paintFlags?.or(128) ?: 0
    }
    return this
}

fun BaseViewHolder?.setProgress(@IdRes viewId: Int, progress: Int): BaseViewHolder? {
    val view: ProgressBar? = this?.getView(viewId)
    view?.progress = progress
    return this
}

fun BaseViewHolder?.setProgress(@IdRes viewId: Int, progress: Int, max: Int): BaseViewHolder? {
    val view: ProgressBar? = this?.getView(viewId)
    view?.max = max
    view?.progress = progress
    return this
}

fun BaseViewHolder?.setMax(@IdRes viewId: Int, max: Int): BaseViewHolder? {
    val view: ProgressBar? = this?.getView(viewId)
    view?.max = max
    return this
}

fun BaseViewHolder?.setRating(@IdRes viewId: Int, rating: Float): BaseViewHolder? {
    val view: RatingBar? = this?.getView(viewId)
    view?.rating = rating
    return this
}

fun BaseViewHolder?.setRating(@IdRes viewId: Int, rating: Float, max: Int): BaseViewHolder? {
    val view: RatingBar? = this?.getView(viewId)
    view?.max = max
    view?.rating = rating
    return this
}

fun BaseViewHolder?.setMiddleLines(@IdRes viewId: Int) {
    val view: TextView? = this?.getView(viewId)
    view?.paint?.flags = 8
    view?.paint?.isAntiAlias = true
    view?.paint?.flags = 17
}

fun BaseViewHolder?.linkify(@IdRes viewId: Int): BaseViewHolder? {
    val view: TextView? = this?.getView(viewId)
    if (view != null) {
        Linkify.addLinks(view, Linkify.ALL)
    }
    return this
}

fun BaseViewHolder?.setMargins(
    @IdRes viewId: Int,
    left: Int,
    top: Int,
    right: Int,
    bottom: Int
): BaseViewHolder? {
    val view: View? = this?.getView(viewId)
    val params = view?.layoutParams
    if (params is ViewGroup.MarginLayoutParams) {
        params.setMargins(left, top, right, bottom)
        view.layoutParams = params
    }
    return this
}

fun BaseViewHolder?.setMargins(@IdRes viewId: Int, margin: Int): BaseViewHolder? {
    return setMargins(viewId, margin, margin, margin, margin)
}

fun BaseViewHolder?.setMarginsRelative(
    @IdRes viewId: Int, left: Int,
    top: Int,
    right: Int,
    bottom: Int
): BaseViewHolder? {
    val view: View? = this?.getView(viewId)
    val params = view?.layoutParams
    if (params is ViewGroup.MarginLayoutParams) {
        params.marginStart = left
        params.marginEnd = right
        params.topMargin = top
        params.bottomMargin = bottom
        view.layoutParams = params
    }
    return this
}

fun BaseViewHolder?.setMarginsRelative(@IdRes viewId: Int, margin: Int): BaseViewHolder? {
    return setMarginsRelative(viewId, margin, margin, margin, margin)
}

fun BaseViewHolder?.setMarginEnd(@IdRes viewId: Int, end: Int): BaseViewHolder? {
    val view: View? = this?.getView(viewId)
    val params = view?.layoutParams
    if (params is ViewGroup.MarginLayoutParams) {
        params.marginEnd = end
        view.layoutParams = params
    }
    return this
}

fun BaseViewHolder?.setMarginStart(@IdRes viewId: Int, start: Int): BaseViewHolder? {
    val view: View? = this?.getView(viewId)
    val params = view?.layoutParams
    if (params is ViewGroup.MarginLayoutParams) {
        params.marginStart = start
        view.layoutParams = params
    }
    return this
}

fun BaseViewHolder?.setMarginStartAndEnd(
    @IdRes viewId: Int,
    start: Int,
    end: Int
): BaseViewHolder? {
    val view: View? = this?.getView(viewId)
    val params = view?.layoutParams
    if (params is ViewGroup.MarginLayoutParams) {
        params.marginStart = start
        params.marginEnd = end
        view.layoutParams = params
    }
    return this
}

fun BaseViewHolder?.setMarginBottom(@IdRes viewId: Int, bottom: Int): BaseViewHolder? {
    val view: View? = this?.getView(viewId)
    val params = view?.layoutParams
    if (params is ViewGroup.MarginLayoutParams) {
        params.bottomMargin = bottom
        view.layoutParams = params
    }
    return this
}

fun BaseViewHolder?.setMarginTop(@IdRes viewId: Int, top: Int): BaseViewHolder? {
    val view: View? = this?.getView(viewId)
    val params = view?.layoutParams
    if (params is ViewGroup.MarginLayoutParams) {
        params.topMargin = top
        view.layoutParams = params
    }
    return this
}

fun BaseViewHolder?.setMarginTopAndBottom(
    @IdRes viewId: Int,
    top: Int,
    bottom: Int
): BaseViewHolder? {
    val view: View? = this?.getView(viewId)
    val params = view?.layoutParams
    if (params is ViewGroup.MarginLayoutParams) {
        params.topMargin = top
        params.bottomMargin = bottom
        view.layoutParams = params
    }
    return this
}

fun BaseViewHolder?.setViewSize(@IdRes viewId: Int, width: Int, height: Int): BaseViewHolder? {
    val view: View? = this?.getView(viewId)
    val params = view?.layoutParams
    params?.width = width
    params?.height = height
    view?.layoutParams = params
    return this
}

fun BaseViewHolder?.setOnClickListener(
    @IdRes viewId: Int,
    listener: View.OnClickListener?
): BaseViewHolder? {
    val view: View? = this?.getView(viewId)
    view?.setOnClickListener(listener)
    return this
}
