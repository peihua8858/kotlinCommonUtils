package com.peihua8858.android.tools.decoration

import android.graphics.Rect
import android.view.View
import androidx.annotation.IntRange
import androidx.annotation.Px
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.peihua8858.tools.utils.isAppRtl

/**
 * 瀑布流布局列表间距,理论上支持3列以上
 *
 * @author dingpeihua
 * @version 1.0
 * @date 2019/12/12 12:52
 */
open class GridLayoutItemDecoration(@Px offset: Int) : ItemDecoration() {
    /**
     * item之间的边距
     */
    @JvmField
    @Px
    protected var offset = 0

    /**
     * 瀑布流起始位置
     */
    @JvmField
    protected var startPosition = 0

    /**
     * 第一行设置顶部间距
     */
    private var firstRowTopMargin = true

    constructor(@Px offset: Int, @IntRange(from = -1) startPosition: Int) : this(offset) {
        setStartPosition(startPosition)
    }

    fun setStartPosition(@IntRange(from = -1) startPosition: Int): GridLayoutItemDecoration {
        this.startPosition = startPosition
        return this
    }

    fun setOffset(@Px offset: Int): GridLayoutItemDecoration {
        this.offset = offset
        return this
    }

    fun setFirstRowTopMargin(firstRowTopMargin: Boolean): GridLayoutItemDecoration {
        this.firstRowTopMargin = firstRowTopMargin
        return this
    }

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val layoutManager = parent.layoutManager
        val viewHolder = parent.getChildViewHolder(view)
        val itemType = viewHolder.itemViewType
        val position = viewHolder.layoutPosition
        //获取当前布局列数
        var spanCount = 2
        spanCount = if (layoutManager is GridLayoutManager) {
            layoutManager.spanCount
        } else if (layoutManager is StaggeredGridLayoutManager) {
            layoutManager.spanCount
        } else {
            return
        }
        if (checkItemType(itemType, position, view)) {
            outRect[0, 0, 0] = 0
            return
        }
        val offset = offset
        val curPosition = position - Math.max(startPosition, 0)
        //获取当前item的列索引
        val columnIndex = lastSpan(curPosition, spanCount)
        if (isAppRtl()) {
            // spacing - column * ((1f / spanCount) * spacing)
            outRect.right = offset - columnIndex * offset / spanCount
            // (column + 1) * ((1f / spanCount) * spacing)
            outRect.left = (columnIndex + 1) * offset / spanCount
        } else {
            // spacing - column * ((1f / spanCount) * spacing)
            outRect.left = offset - columnIndex * offset / spanCount
            // (column + 1) * ((1f / spanCount) * spacing)
            outRect.right = (columnIndex + 1) * offset / spanCount
        }
        if (curPosition < spanCount && firstRowTopMargin) {
            outRect.top = offset
        }
        outRect.bottom = offset
    }

    protected fun lastSpan(position: Int, spanCount: Int): Int {
        return position % spanCount
    }

    open fun checkItemType(itemType: Int, position: Int, view: View?): Boolean {
        return false
    }

    fun setFullSpan(view: View) {
        val vLayoutParams = view.layoutParams
        if (vLayoutParams is StaggeredGridLayoutManager.LayoutParams) {
            val layoutParams = vLayoutParams
            layoutParams.isFullSpan = true
            layoutParams.marginStart = 0
            layoutParams.marginEnd = 0
            view.layoutParams = layoutParams
        }
    }

    /**
     * 构造方法
     *
     * @param offset px
     */
    init {
        setOffset(offset)
    }
}