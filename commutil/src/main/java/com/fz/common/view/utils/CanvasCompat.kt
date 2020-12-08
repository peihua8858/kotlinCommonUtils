package com.fz.common.view.utils

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.os.Build

/**
 * Canvas 兼容处理
 *
 * @author dingpeihua
 * @version 1.0
 * @date 2019/12/19 18:52
 */
fun Canvas.saveLayerAlphaCompat(bounds: RectF?, alpha: Int): Int {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) saveLayerAlpha(
        bounds, alpha
    ) else saveLayerAlpha(bounds, alpha, Canvas.ALL_SAVE_FLAG)
}

fun Canvas.saveLayerAlphaCompat(
    left: Float, top: Float, right: Float, bottom: Float, alpha: Int
): Int {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) saveLayerAlpha(
        left, top, right, bottom, alpha
    ) else saveLayerAlpha(left, top, right, bottom, alpha, Canvas.ALL_SAVE_FLAG)
}

fun Canvas.saveLayerCompat(bounds: RectF?, paint: Paint?): Int {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) saveLayer(
        bounds, paint
    ) else saveLayer(bounds, paint, Canvas.ALL_SAVE_FLAG)
}