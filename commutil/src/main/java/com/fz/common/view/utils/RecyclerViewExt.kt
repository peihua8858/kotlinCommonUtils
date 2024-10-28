package com.fz.common.view.utils

import android.graphics.Rect
import android.view.View
import androidx.annotation.Px
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.OnScrollListener
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.fz.common.R
import com.fz.common.utils.dLog
import com.fz.common.utils.showToast
import kotlin.math.abs

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

var RecyclerView.spanCount: Int
    get() {
        val manager = layoutManager
        if (manager is GridLayoutManager) {
            return manager.spanCount
        } else if (manager is StaggeredGridLayoutManager) {
            return manager.spanCount
        }
        return 1
    }
    set(value) {
        val manager = layoutManager
        if (manager is GridLayoutManager) {
            manager.spanCount = value
        } else if (manager is StaggeredGridLayoutManager) {
            manager.spanCount = value
        }
    }

/**
 * 获取第一条完全展示的位置
 *
 * @return
 */
val RecyclerView?.firstVisiblePosition: Int
    get() {
        if (this == null) return -1
        layoutManager?.apply {
            return when (this) {
                is LinearLayoutManager -> {
                    findFirstVisibleItemPosition()
                }

                is StaggeredGridLayoutManager -> {
                    val firstPositions =
                        findFirstVisibleItemPositions(IntArray(spanCount))
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
 * 获取最后一条完全展示的位置
 *
 * @return
 */
val RecyclerView?.lastVisiblePosition: Int
    get() {
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

fun RecyclerView?.findViewRect(position: Int): Rect {
    if (this == null) return Rect()
    return layoutManager?.let {
        return it.findViewByPosition(position)?.let { view ->
            val rect = Rect()
            view.getGlobalVisibleRect(rect)
            rect
        } ?: Rect()
    } ?: Rect()
}

/**
 * recyclerView 实现上一页或下一页操作
 * @param isNextPage 是否下一页
 * @param isSmoothScroll 是否平滑滚动
 * @param callback 回调
 * @return 返回当前页滚动的具体位置
 */
fun RecyclerView.pageScroll(
    isNextPage: Boolean = true,
    isSmoothScroll: Boolean = true,
    itemMargin: Int = 0,
    callback: (Int) -> Unit
) {
    if (isSmoothScroll) {
        addOnScrollListener(object : OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    removeOnScrollListener(this)
                    post {
                        callback(firstCompletelyPosition)
                    }
                }
            }
        })
    }
    val manager = layoutManager
    val orientation =
        if (manager is LinearLayoutManager) manager.orientation
        else if (manager is StaggeredGridLayoutManager) manager.orientation
        else RecyclerView.VERTICAL
    val firstVisiblePosition = firstVisiblePosition
    val lastVisiblePosition = lastVisiblePosition
    val firstCompletelyPosition = firstCompletelyPosition
    val lastCompletelyPosition = lastCompletelyPosition
    val visibleViewRect =
        if (isNextPage) findViewRect(lastVisiblePosition) else findViewRect(firstVisiblePosition)
    val rvRect = Rect()
    getGlobalVisibleRect(rvRect) // 获取 RecyclerView 在屏幕中的可见区域
    val viewCompletelyRect = if (isNextPage) findViewRect(lastCompletelyPosition) else findViewRect(
        firstCompletelyPosition
    )
    if (orientation == RecyclerView.VERTICAL) {
        val rvBottom = rvRect.bottom
        val rvTop = rvRect.top
        dLog {  "pageScroll visibleViewRect: $visibleViewRect,rvRect: $rvRect"}
        if (isNextPage) {
            dLog {
                "pageScroll viewCompletelyRect: $viewCompletelyRect,rvBottom: $rvBottom"
            }
            val offset = rvBottom - visibleViewRect.top + itemMargin
            rvRect.bottom -= offset
        } else {
            if (firstVisiblePosition != firstCompletelyPosition) {
                dLog {
                    "pageScroll viewCompletelyRect: $viewCompletelyRect,rvTop: $rvTop"
                }
                val offset = rvTop - visibleViewRect.bottom
                rvRect.top += abs(offset)
            }
        }
        val visibleHeight: Int = rvRect.height()
        dLog { "pageScroll visibleHeight:$visibleHeight"}
        val slideHeight = if (isNextPage) visibleHeight else -(visibleHeight)
        if (isNextPage) {
            val isLastPage = viewCompletelyRect.bottom == rvBottom
            dLog {  "pageScroll isLastPage:$isLastPage,rvBottom: $rvBottom,viewCompletelyRect: $viewCompletelyRect"}
            if (isLastPage) {
                showToast(R.string.text_last_page)
                return
            }
        } else {
            val isFirstPage = viewCompletelyRect.top == rvTop+itemMargin
            dLog {  "pageScroll isFirstPage:$isFirstPage,rvTop: $rvTop,viewCompletelyRect: $viewCompletelyRect"}
            if (isFirstPage) {
                showToast(R.string.text_first_page)
                return
            }
        }
        if (isSmoothScroll) {
            smoothScrollBy(0, slideHeight)
        } else {
            scrollBy(0, slideHeight)
        }
        dLog {  "pageScroll height: $visibleHeight, slideHeight: $slideHeight"}
    } else if (orientation == RecyclerView.HORIZONTAL) {
        val rvRight = rvRect.right
        val rvLeft = rvRect.left
        if (isNextPage) {
            val offset = rvRight - visibleViewRect.left + itemMargin
            rvRect.right -= offset
        } else {
            if (firstVisiblePosition != firstCompletelyPosition) {
                val offset = rvLeft - visibleViewRect.right
                rvRect.left += abs(offset)
            }
        }
        val visibleWidth: Int = rvRect.width()
        dLog {  "pageScroll visibleHeight:$visibleWidth"}
        val slideWidth = if (isNextPage) visibleWidth else -(visibleWidth)
        if (isNextPage) {
            val isLastPage = viewCompletelyRect.right == rvRight
            dLog { "pageScroll isLastPage:$isLastPage,rvRight: $rvRight,viewCompletelyRect: $viewCompletelyRect"}
            if (isLastPage) {
                showToast(R.string.text_last_page)
                return
            }
        } else {
            val isFirstPage = viewCompletelyRect.left == rvLeft
            dLog { "pageScroll isFirstPage:$isFirstPage,rvLeft: $rvLeft,viewCompletelyRect: $viewCompletelyRect"}
            if (isFirstPage) {
                showToast(R.string.text_first_page)
                return
            }
        }
        if (isSmoothScroll) {
            smoothScrollBy(slideWidth, 0)
        } else {
            scrollBy(slideWidth, 0)
        }
        dLog { "pageScroll width: $visibleWidth, slideWidth: $slideWidth"}
    }
    if (!isSmoothScroll) {
        post { callback(this.firstCompletelyPosition) }
    }
}