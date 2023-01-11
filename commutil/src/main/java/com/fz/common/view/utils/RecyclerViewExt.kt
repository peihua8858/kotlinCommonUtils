package com.fz.common.view.utils

import android.graphics.Rect
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

/**
 * 查找指定类型的item 位置索引
 * @return 返回查当前类型item索引位置，如果未找到则返回-1
 * @author dingpeihua
 * @date 2023/1/11 16:00
 * @version 1.0
 */
fun RecyclerView.Adapter<*>?.findPositionByType(type: Int): Int {
    if (this == null) {
        return -1
    }
    val itemCount = itemCount
    for (index in 0 until itemCount) {
        val itemType = getItemViewType(index)
        if (itemType == type) {
            return index
        }
    }
    return -1
}

/**
 * 滚动到具有给定偏移量的指定适配器位置
 * @param position item 位置索引
 * @param offset 偏移量
 * @author dingpeihua
 * @date 2023/1/11 15:53
 * @version 1.0
 */
fun RecyclerView?.scrollToPositionWithOffset(position: Int, offset: Int) {
    if (this == null) return
    val manager = layoutManager
    if (manager is LinearLayoutManager) {
        manager.scrollToPositionWithOffset(position, offset)
    } else if (manager is StaggeredGridLayoutManager) {
        manager.scrollToPositionWithOffset(position, offset)
    }
}

/**
 *滚动指定位置索引到居中位置
 * @param view 当前item view
 * @param position 当前item 所在的位置索引
 * @author dingpeihua
 * @date 2023/1/11 15:58
 * @version 1.0
 */
fun RecyclerView?.smoothScrollToCenter(view: View, position: Int) {
    if (this == null) return
    val rect = Rect()
    getGlobalVisibleRect(rect)
    val layoutManager = layoutManager
    val firstPosition = getStartPosition()
    val centerView = getChildAt(position - firstPosition)
    val orientation = when (layoutManager) {
        is LinearLayoutManager -> {
            layoutManager.orientation
        }
        is StaggeredGridLayoutManager -> {
            layoutManager.orientation
        }
        else -> RecyclerView.VERTICAL
    }
    //将点击的position转换为当前屏幕上可见的item的位置以便于计算距离顶部的高度，从而进行移动居中
    val childAt = getChildAt(position - firstPosition)
    if (childAt != null) {
        if (orientation == RecyclerView.VERTICAL) {
            val reHeight = rect.bottom - rect.top - view.height
            val top = centerView.top
            val half = reHeight / 2
            smoothScrollBy(0, top - half)
        } else if (orientation == RecyclerView.HORIZONTAL) {
            val reWidth = rect.right - rect.left - view.width
            val left = centerView.left
            val half = reWidth / 2
            smoothScrollBy(left - half, 0)
        }
    }
}

/**
 * 滚动指定位置索引到居中位置
 * @param position 当前item 所在的位置索引
 * @author dingpeihua
 * @date 2023/1/11 15:55
 * @version 1.0
 */
fun RecyclerView?.smoothScrollToCenter(position: Int) {
    if (this == null) return
    val curView = getChildAt(position)
    smoothScrollToCenter(curView, position)
}

/**
 * 滚动指定位置索引到居中位置
 * @param position 当前item 所在的位置索引
 * @author dingpeihua
 * @date 2023/1/11 15:55
 * @version 1.0
 */
fun RecyclerView?.scrollToCenter(position: Int) {
    if (this == null) return
    val curView = getChildAt(position)
    scrollToCenter(curView, position)
}

/**
 * 滚动指定位置索引到居中位置
 * @param view 当前item view
 * @param position 当前item 所在的位置索引
 * @author dingpeihua
 * @date 2023/1/11 15:55
 * @version 1.0
 */
fun RecyclerView?.scrollToCenter(view: View, position: Int) {
    if (this == null) return
    val rect = Rect()
    getGlobalVisibleRect(rect)
    val layoutManager = layoutManager
    val firstPosition = getStartPosition()
    val centerView = getChildAt(position - firstPosition)
    val orientation = when (layoutManager) {
        is LinearLayoutManager -> {
            layoutManager.orientation
        }
        is StaggeredGridLayoutManager -> {
            layoutManager.orientation
        }
        else -> RecyclerView.VERTICAL
    }
    if (orientation == RecyclerView.VERTICAL) {
        val reHeight = rect.bottom - rect.top - view.height
        val top = centerView.top
        val half = reHeight / 2
        scrollBy(0, top - half)
    } else if (orientation == RecyclerView.HORIZONTAL) {
        val reWidth = rect.right - rect.left - view.width
        val left = centerView.left
        val half = reWidth / 2
        scrollBy(left - half, 0)
    }
}