package com.fz.common.view.utils

import android.graphics.Rect
import android.view.View
import androidx.annotation.Px
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
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

fun RecyclerView.measureTwoHalf(
    limitLines: Float,
    itemHeight: Int,
    @Px offset: Int = 0,
    callback: (Int) -> Unit
) {
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
            for (i in 0 until size) {
                realWidth += itemHeight
                realWidth += offset
            }
            realWidth += (itemHeight / 2)
            val params = layoutParams
            params.width = realWidth
            layoutParams = params
            callback(realWidth)
        } else {
            var realHeight = 0
            val size = limitLines.toInt()
            for (i in 0 until size) {
                realHeight += itemHeight
                realHeight += offset
            }
            realHeight += (itemHeight / 2)
            val params = layoutParams
            params.height = realHeight
            layoutParams = params
            callback(realHeight)
        }
    }
}

fun RecyclerView.measureTwoHalf(limitLines: Float, @Px offset: Int = 0, callback: (Int) -> Unit) {
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
                for (i in 0 until size) {
                    val view: View? = getChildAt(i)
                    realWidth += view?.measuredWidth ?: 0
                    realWidth += offset
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
                for (i in 0 until size) {
                    val view: View? = getChildAt(i)
                    realHeight += view?.measuredHeight ?: 0
                    realHeight += offset
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
    //将点击的position转换为当前屏幕上可见的item的位置以便于计算距离顶部的高度，从而进行移动居中
    if (centerView != null) {
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
    var curView = getChildAt(position)
    if (curView == null) {
        scrollToPosition(position)
        curView = getChildAt(position)
    }
    if (curView != null) {
        smoothScrollToCenter(curView, position)
    }
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
    var curView = getChildAt(position)
    if (curView == null) {
        scrollToPosition(position)
        curView = getChildAt(position)
    }
    if (curView != null) {
        scrollToCenter(curView, position)
    }
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
    if (centerView != null) {
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
}

/**
 * 获取列表列数
 */
val RecyclerView.spanCount: Int
    get() {
        val manager = layoutManager
        if (manager is GridLayoutManager) {
            return manager.spanCount
        } else if (manager is StaggeredGridLayoutManager) {
            return manager.spanCount
        }
        return 1
    }

/**
 * 获取最后一条完全展示的位置
 *
 * @return
 */
val RecyclerView?.lastCompletelyPosition: Int
    get() {
        if (this == null) return -1
        layoutManager?.apply {
            return when (this) {
                is LinearLayoutManager -> {
                    findLastCompletelyVisibleItemPosition()
                }

                is StaggeredGridLayoutManager -> {
                    val lastPositions = findLastCompletelyVisibleItemPositions(IntArray(spanCount))
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
 * 获取第一条完全展示的位置
 *
 * @return
 */
val RecyclerView?.firstCompletelyPosition: Int
    get() {
        if (this == null) return -1
        layoutManager?.apply {
            return when (this) {
                is LinearLayoutManager -> {
                    findFirstCompletelyVisibleItemPosition()
                }

                is StaggeredGridLayoutManager -> {
                    val firstPositions =
                        findFirstCompletelyVisibleItemPositions(IntArray(spanCount))
                    firstPositions.minOrNull() ?: -1
                }

                else -> {
                    itemCount - 1
                }
            }
        }
        return -1
    }

/**
 * 获取列表行数
 */
val RecyclerView.lines: Int
    get() {
        val itemCount = adapter?.itemCount ?: 0
        if (itemCount == 0) {
            return 0
        }
        val spanCount = spanCount
        var lines = itemCount / spanCount
        if (itemCount % spanCount != 0) {
            lines++
        }
        return lines
    }

/**
 * recyclerView 实现上一页或下一页操作
 * @param slideCount 上一页缓存滚动的具体位置
 * @param isNextPage 是否下一页
 * @param isSmoothScroll 是否平滑滚动
 * @param callback 回调
 * @return 返回当前页滚动的具体位置
 */
fun RecyclerView.pageScroll(
    slideCount: Int,
    isNextPage: Boolean = true,
    isSmoothScroll: Boolean = true,
    callback: (Int, Int) -> Unit
) {
    var tempSlideCount = slideCount
    if (isSmoothScroll) {
        addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    removeOnScrollListener(this)
                    post {
                        callback(tempSlideCount, firstCompletelyPosition)
                    }
                }
            }
        })
    }
    val firstChild = getChildAt(0)
    val manager = layoutManager
    val orientation =
        if (manager is LinearLayoutManager) manager.orientation
        else if (manager is StaggeredGridLayoutManager) manager.orientation
        else RecyclerView.VERTICAL
    val params = firstChild.layoutParams as RecyclerView.LayoutParams
    val firstPosition = firstCompletelyPosition
    val lastPosition = lastCompletelyPosition
    val itemSize = lastPosition - firstPosition
    val spanCount = spanCount
    var lines = itemSize / spanCount
    if (itemSize % spanCount != 0) {
        lines++
    }
    if (orientation == RecyclerView.VERTICAL) {
        val height = firstChild.height + params.topMargin + params.bottomMargin
        var slideHeight = if (isNextPage) lines * height else -(lines * height)
        tempSlideCount += slideHeight
        val countHeight = height * this.lines
        if (countHeight - tempSlideCount <= 0) {
            slideHeight = 0 - countHeight
            tempSlideCount = 0
        }
        if (isSmoothScroll) {
            smoothScrollBy(0, slideHeight)
        } else {
            scrollBy(0, slideHeight)
        }
    } else if (orientation == RecyclerView.HORIZONTAL) {
        val width = firstChild.width + params.marginStart + params.marginEnd
        var slideWidth = if (isNextPage) lines * width else -(lines * width)
        tempSlideCount += slideWidth
        val countHeight = width * this.lines
        if (countHeight - tempSlideCount <= 0) {
            slideWidth = 0 - countHeight
            tempSlideCount = 0
        }
        if (isSmoothScroll) {
            smoothScrollBy(slideWidth, 0)
        } else {
            scrollBy(slideWidth, 0)
        }
    }
    if (!isSmoothScroll) {
        post { callback(tempSlideCount, firstCompletelyPosition) }
    }
}