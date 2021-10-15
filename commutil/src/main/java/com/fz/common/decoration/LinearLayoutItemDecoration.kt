package com.fz.common.decoration

import android.graphics.Rect
import android.view.View
import androidx.annotation.Px
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration

/**
 * 线性布局间距
 *
 * @author dingpeihua
 * @version 1.0
 * @date 2019/12/12 12:52
 */
class LinearLayoutItemDecoration @JvmOverloads constructor(
    /**
     * item 间距
     */
    private val mRect: Rect,
    /**
     * 四周撑满父组件
     */
    private val isFullParent: Boolean = false
) : ItemDecoration() {

    /**
     * 构造方法
     *
     * @param offset px
     */
    constructor(@Px offset: Int) : this(Rect(offset, offset, offset, offset), false) {}

    /**
     * 设置item 矩阵
     *
     * @param left   item 左边距
     * @param top    item 上边距
     * @param right  item 右边距
     * @param bottom item 下边距
     * @author dingpeihua
     * @date 2016/6/23 11:01
     * @version 1.0
     */
    constructor(left: Int, top: Int, right: Int, bottom: Int) : this(
        Rect(left, top, right, bottom),
        false
    ) {
    }

    /**
     * 设置item 矩阵
     *
     * @param left   item 左边距
     * @param top    item 上边距
     * @param right  item 右边距
     * @param bottom item 下边距
     * @author dingpeihua
     * @date 2016/6/23 11:01
     * @version 1.0
     */
    constructor(left: Int, top: Int, right: Int, bottom: Int, isFullParent: Boolean) : this(
        Rect(
            left,
            top,
            right,
            bottom
        ), isFullParent
    ) {
    }

    constructor(@Px offset: Int, isFullParent: Boolean) : this(
        Rect(offset, offset, offset, offset),
        isFullParent
    ) {
    }

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val layoutManager = parent.layoutManager
        if (layoutManager is LinearLayoutManager) {
            val orientation = layoutManager.orientation
            val viewHolder = parent.getChildViewHolder(view)
            val position = viewHolder.bindingAdapterPosition
            val adapter = parent.adapter
            val itemCount = adapter?.itemCount ?: 0
            if (orientation == LinearLayoutManager.HORIZONTAL) {
                if (isFullParent) {
                    outRect[if (position != 0) mRect.left shr 1 else 0, 0, if (itemCount - 1 > position) mRect.right shr 1 else 0] =
                        0
                } else {
                    outRect[if (position != 0) mRect.left shr 1 else mRect.left, mRect.top, if (itemCount - 1 > position) mRect.right shr 1 else mRect.right] =
                        mRect.bottom
                }
            } else {
                if (isFullParent) {
                    outRect[0, if (position != 0) mRect.top shr 1 else 0, 0] =
                        if (itemCount - 1 > position) mRect.bottom shr 1 else 0
                } else {
                    outRect[mRect.left, if (position != 0) mRect.top shr 1 else mRect.top, mRect.right] =
                        if (itemCount - 1 > position) mRect.bottom shr 1 else mRect.bottom
                }
            }
            return
        }
        super.getItemOffsets(outRect, view, parent, state)
    }
}