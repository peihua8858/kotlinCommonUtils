package com.peihua8858.android.tools.decoration

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import com.peihua8858.tools.utils.isAppRtl

/**
 * 商品列表间距
 *
 * @author dingpeihua
 * @version 1.0
 * @date 2019/12/12 11:54
 */
class ProductItemDecoration : ItemDecoration {
    private var offset: Int
    private var offset2: Int
    private var firstGoodsPosition = -1

    /**
     * @param offset
     */
    constructor(offset: Int) {
        this.offset = offset
        offset2 = offset
    }

    /**
     * @param offset  横轴
     * @param offset2 竖轴
     */
    constructor(offset: Int, offset2: Int) {
        this.offset = offset
        this.offset2 = offset2
    }

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val viewHolder = parent.getChildViewHolder(view)
        val itemPosition = viewHolder.adapterPosition
        //        final int type = viewHolder.getItemViewType();
        val spanCount = 2
        if (firstGoodsPosition < 0) {
            firstGoodsPosition = itemPosition
        }
        val curPosition = itemPosition - firstGoodsPosition
        if (firstGoodsPosition == 0 && curPosition < spanCount) {
            outRect.top = offset2
        }
        outRect.bottom = offset2
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
    }

    private fun lastSpan(position: Int, spanCount: Int): Int {
        return position % spanCount
    }
}