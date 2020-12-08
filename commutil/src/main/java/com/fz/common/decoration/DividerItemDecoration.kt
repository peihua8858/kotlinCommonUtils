package com.fz.common.decoration

import android.content.Context
import android.graphics.Canvas
import android.graphics.Rect
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.view.View
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import com.fz.common.utils.getColor
import com.fz.common.utils.getDrawable
import com.fz.common.view.utils.dip2px
import com.socks.library.KLog

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
    var mOrientation = 0
    private var dividerSize = 0

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

    private var noDrawDividerPosition = -1
    fun setOrientation(orientation: Int) {
        require(!(orientation != HORIZONTAL_LIST && orientation != VERTICAL_LIST)) { "invalid orientation" }
        mOrientation = orientation
    }

    fun setNoDrawDividerPosition(noDrawDividerPosition: Int) {
        this.noDrawDividerPosition = noDrawDividerPosition
    }

    constructor(@DrawableRes drawable: Int, orientation: Int) {
        mDivider = getDrawable(drawable)
        mOrientation = orientation
        dividerSize = dip2px(1)
    }

    constructor(@ColorRes color: Int, dividerHeight: Int, orientation: Int) {
        mDivider = ColorDrawable(getColor(color))
        mOrientation = orientation
        dividerSize = dividerHeight
    }

    override fun getItemOffsets(
            outRect: Rect,
            view: View,
            parent: RecyclerView,
            state: RecyclerView.State
    ) {
        val childCount = parent.childCount
        val itemPosition = parent.getChildAdapterPosition(view)
        if (mOrientation == VERTICAL_LIST) {
            val intrinsicHeight = mDivider!!.intrinsicHeight
            outRect[0, 0, 0] =
                    if (itemPosition < childCount - 1) if (intrinsicHeight > 0) intrinsicHeight else dividerSize else 0
        } else {
            val intrinsicWidth = mDivider!!.intrinsicWidth
            outRect[0, 0, if (itemPosition < childCount - 1) if (intrinsicWidth > 0) intrinsicWidth else dividerSize else 0] =
                    0
        }
    }

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDraw(c, parent, state)
    }

    override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        if (mOrientation == LinearLayoutCompat.VERTICAL) {
            drawVertical(c, parent)
        } else {
            drawHorizontal(c, parent)
        }
    }

    fun drawVertical(c: Canvas?, parent: RecyclerView) {
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
            val intrinsicHeight = mDivider!!.intrinsicHeight
            val bottom = top + if (intrinsicHeight > 0) intrinsicHeight else dividerSize
            mDivider!!.setBounds(left, top, right, bottom)
            mDivider!!.draw(c!!)
        }
    }

    fun drawHorizontal(c: Canvas?, parent: RecyclerView) {
        val top = parent.paddingTop
        val bottom = parent.height - parent.paddingBottom
        val childCount = parent.childCount
        for (i in 0 until childCount) {
            val child = parent.getChildAt(i)
            val params = child.layoutParams as RecyclerView.LayoutParams
            val left = child.right + params.rightMargin
            val right = left + mDivider!!.intrinsicHeight
            KLog.d("LockCart>>>>left=$left,top=$top,right=$right,bottom=$bottom")
            mDivider!!.setBounds(left, top, right, bottom)
            mDivider!!.draw(c!!)
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