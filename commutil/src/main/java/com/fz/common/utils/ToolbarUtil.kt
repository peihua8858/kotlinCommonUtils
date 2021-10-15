@file:JvmName("ToolbarUtil")
@file:JvmMultifileClass
package com.fz.common.utils

import android.graphics.drawable.Drawable
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import com.fz.common.R
import com.google.android.material.appbar.AppBarLayout

/**
 * 控制除ProductFragment外，其它fragment获取焦点时清除scroll flag设置
 */
fun Toolbar?.clearAppBarScrollFlags() {
    if (this != null) {
        val layoutParams: ViewGroup.LayoutParams = this.layoutParams
        if (layoutParams is AppBarLayout.LayoutParams) {
            layoutParams.scrollFlags = 0
        }
        val parent = this.parent
        if (parent is AppBarLayout) {
            parent.setExpanded(true, true)
        }
    }
}

/**
 * ProductFragment获取焦点时设置scroll flag
 */
fun Toolbar?.setAppBarScrollFlags() {
    if (this != null) {
        val layoutParams: ViewGroup.LayoutParams = this.layoutParams
        if (layoutParams is AppBarLayout.LayoutParams) {
            layoutParams.scrollFlags = (AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL
                    or AppBarLayout.LayoutParams.SCROLL_FLAG_ENTER_ALWAYS
                    or AppBarLayout.LayoutParams.SCROLL_FLAG_SNAP)
        }
    }
}

fun Toolbar?.setToolbarLogo(@DrawableRes drawableRes: Int) {
    this?.setToolbarLogo(ContextCompat.getDrawable(this.context, drawableRes))
}

/**
 * 设置[Toolbar] 徽章
 * @param drawable 徽章
 * @author dingpeihua
 * @date 2021/1/14 10:07
 * @version 1.0
 */
fun Toolbar?.setToolbarLogo(drawable: Drawable?) {
    if (this != null) {
        logo = drawable
        title = ""
    }
}