package com.fz.common.view.utils

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import androidx.viewpager2.widget.ViewPager2

/**
 * 获取最后一条展示的位置
 *
 * @return
 */
fun RecyclerView?.getEndPosition(): Int {
    if (this == null) return -1
    layoutManager?.apply {
        return when (this) {
            is LinearLayoutManager -> {
                findLastVisibleItemPosition()
            }
            is StaggeredGridLayoutManager -> {
                val lastPositions = findLastVisibleItemPositions(IntArray(spanCount))
                lastPositions.maxOrNull() ?: -1
            }
            else -> {
                itemCount - 1
            }
        }
    }
    return -1
}

/**
 * 获取第一条展示的位置
 *
 * @return
 */
fun RecyclerView?.getStartPosition(): Int {
    if (this == null) return -1
    layoutManager?.apply {
        return when (this) {
            is LinearLayoutManager -> {
                findFirstVisibleItemPosition()
            }
            is StaggeredGridLayoutManager -> {
                val firstPositions = findFirstVisibleItemPositions(IntArray(spanCount))
                firstPositions.minOrNull() ?: -1
            }
            else -> {
                itemCount - 1
            }
        }
    }
    return -1
}


fun RecyclerView.checkAddItemDecoration(decoration: RecyclerView.ItemDecoration) {
    if (checkItemDecoration(decoration)) {
        addItemDecoration(decoration)
    }
}

fun RecyclerView.checkItemDecoration(decoration: RecyclerView.ItemDecoration): Boolean {
    val count: Int = itemDecorationCount
    for (i in 0 until count) {
        val itemDecoration = getItemDecorationAt(i)
        if (itemDecoration === decoration) {
            return false
        }
    }
    return true
}

fun ViewPager2?.changeOverScrollMode(mode: Int) {
    this?.apply {
        val view = getChildAt(0)
        view?.apply { overScrollMode = mode }
    }
}

fun RecyclerView.measureTwoHalf(limitLines: Float, itemHeight: Int, callback: (Int) -> Unit) {
    post {
        val orientation = when (val layoutManager = layoutManager) {
            is StaggeredGridLayoutManager -> layoutManager.orientation
            is LinearLayoutManager -> layoutManager.orientation
            else -> {
                RecyclerView.VERTICAL
            }
        }
        if (orientation == RecyclerView.HORIZONTAL) {
            var realWidth = 0
            val size = limitLines.toInt()
            val margin = dip2px(16)
            for (i in 0 until size) {
                realWidth += itemHeight
                realWidth += margin
            }
            realWidth += (itemHeight / 2)
            val params = layoutParams
            params.width = realWidth
            layoutParams = params
            callback(realWidth)
        } else {
            var realHeight = 0
            val size = limitLines.toInt()
            val margin = dip2px(16)
            for (i in 0 until size) {
                realHeight += itemHeight
                realHeight += margin
            }
            realHeight += (itemHeight / 2)
            val params = layoutParams
            params.height = realHeight
            layoutParams = params
            callback(realHeight)
        }
    }
}

fun RecyclerView.measureTwoHalf(limitLines: Float, callback: (Int) -> Unit) {
    post {
        val adapter = adapter
        val orientation = when (val layoutManager = layoutManager) {
            is StaggeredGridLayoutManager -> layoutManager.orientation
            is LinearLayoutManager -> layoutManager.orientation
            else -> {
                RecyclerView.VERTICAL
            }
        }
        val count = adapter?.itemCount ?: 0
        if (count > 0) {
            if (orientation == RecyclerView.HORIZONTAL) {
                var realWidth = 0
                val size = if (count > limitLines) limitLines.toInt() else count
                val margin = dip2px(16)
                for (i in 0 until size) {
                    val view: View? = getChildAt(i)
                    realWidth += view?.measuredWidth ?: 0
                    realWidth += margin
                }
                if (count > limitLines) {
                    val view: View? = getChildAt(limitLines.toInt() - 1)
                    val measuredWidth = view?.measuredWidth ?: 0
                    realWidth += (measuredWidth / 2)
                }
                val params = layoutParams
                params.width = realWidth
                layoutParams = params
                callback(realWidth)
            } else {
                var realHeight = 0
                val size = if (count > limitLines) limitLines.toInt() else count
                val margin = dip2px(16)
                for (i in 0 until size) {
                    val view: View? = getChildAt(i)
                    realHeight += view?.measuredHeight ?: 0
                    realHeight += margin
                }
                if (count > limitLines) {
                    val view: View? = getChildAt(limitLines.toInt() - 1)
                    val measuredHeight = view?.measuredHeight ?: 0
                    realHeight += (measuredHeight / 2)
                }
                val params = layoutParams
                params.height = realHeight
                layoutParams = params
                callback(realHeight)
            }
        }
    }
}