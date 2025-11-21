package com.peihua8858.android.tools.decoration

import android.content.Context
import android.graphics.Canvas
import android.graphics.Rect
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.view.View
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import com.peihua8858.tools.utils.dLog
import com.peihua8858.tools.utils.dip2px
import com.peihua8858.tools.utils.getColor
import com.peihua8858.tools.utils.getDrawableCompat

/**
 * recycleView 列表分割线
 *
 * @author dingpeihua
 * @version 1.0
 * @date 2017/11/8 10:58
 */
class DividerItemDecoration : ItemDecoration {
    /**
     * 分割线
     */
    private var mDivider: Drawable?
    private var mOrientation = VERTICAL_LIST
    private var dividerSize = 0
    private var noDrawDividerPosition = -1

    constructor(context: Context) {
        val a = context.obtainStyledAttributes(ATTRS)
        mDivider = a.getDrawable(0)
        a.recycle()
        setOrientation(VERTICAL_LIST)
    }

    constructor(context: Context, orientation: Int) {
        val a = context.obtainStyledAttributes(ATTRS)
        mDivider = a.getDrawable(0)
        a.recycle()
        setOrientation(orientation)
    }


    fun setOrientation(orientation: Int): DividerItemDecoration {
        require(!(orientation != HORIZONTAL_LIST && orientation != VERTICAL_LIST)) { "invalid orientation" }
        mOrientation = orientation
        return this
    }

    fun setNoDrawDividerPosition(noDrawDividerPosition: Int): DividerItemDecoration {
        this.noDrawDividerPosition = noDrawDividerPosition
        return this
    }

    fun setDividerSize(dividerSize: Int): DividerItemDecoration {
        this.dividerSize = dividerSize
        return this
    }

    fun setDivider(drawable: Drawable): DividerItemDecoration {
        this.mDivider = drawable
        return this
    }

    constructor(@DrawableRes drawable: Int) {
        mDivider = getDrawableCompat(drawable)
        dividerSize = dip2px(1)
    }

    constructor(@ColorRes color: Int, dividerHeight: Int, orientation: Int) {
        mDivider = ColorDrawable(getColor(color))
        mOrientation = orientation
        dividerSize = dividerHeight
    }

    constructor(@ColorInt color: Int, dividerHeight: Int) {
        mDivider = ColorDrawable(color)
        dividerSize = dividerHeight
    }

    override fun getItemOffsets(
            outRect: Rect,
            view: View,
            parent: RecyclerView,
            state: RecyclerView.State
    ) {
        mDivider?.let {
            val childCount = parent.childCount
            val itemPosition = parent.getChildAdapterPosition(view)
            if (mOrientation == VERTICAL_LIST) {
                val intrinsicHeight = it.intrinsicHeight
                outRect[0, 0, 0] =
                        if (itemPosition < childCount - 1) if (intrinsicHeight > 0) intrinsicHeight else dividerSize else 0
            } else {
                val intrinsicWidth = it.intrinsicWidth
                outRect[0, 0, if (itemPosition < childCount - 1) if (intrinsicWidth > 0) intrinsicWidth else dividerSize else 0] =
                        0
            }
        }
    }

    override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        mDivider?.let {
            if (mOrientation == LinearLayoutCompat.VERTICAL) {
                drawVertical(it, c, parent)
            } else {
                drawHorizontal(it, c, parent)
            }
        }
    }

    private fun drawVertical(drawable: Drawable, c: Canvas?, parent: RecyclerView) {
        val left = parent.paddingLeft
        val right = parent.width - parent.paddingRight
        val childCount = parent.childCount
        for (i in 0 until childCount - 1) {
            if (i == noDrawDividerPosition) {
                continue
            }
            val child = parent.getChildAt(i)
            val params = child.layoutParams as RecyclerView.LayoutParams
            val top = child.bottom + params.bottomMargin
            val intrinsicHeight = drawable.intrinsicHeight
            val bottom = top + if (intrinsicHeight > 0) intrinsicHeight else dividerSize
            drawable.setBounds(left, top, right, bottom)
            drawable.draw(c!!)
        }
    }

    private fun drawHorizontal(drawable: Drawable, c: Canvas?, parent: RecyclerView) {
        val top = parent.paddingTop
        val bottom = parent.height - parent.paddingBottom
        val childCount = parent.childCount
        for (i in 0 until childCount) {
            val child = parent.getChildAt(i)
            val params = child.layoutParams as RecyclerView.LayoutParams
            val left = child.right + params.rightMargin
            val right = left + drawable.intrinsicHeight
            dLog { "LockCart>>>>left=$left,top=$top,right=$right,bottom=$bottom" }
            drawable.setBounds(left, top, right, bottom)
            drawable.draw(c!!)
        }
    }

    companion object {
        private val ATTRS = intArrayOf(
            android.R.attr.listDivider
        )
        const val HORIZONTAL_LIST = LinearLayoutManager.HORIZONTAL
        const val VERTICAL_LIST = LinearLayoutManager.VERTICAL
    }
}