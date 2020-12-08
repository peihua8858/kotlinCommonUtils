package com.fz.common.listener

import android.view.View

/**
 * 处理连续单击，默认两次单击时间间隔是500ms
 * @author dingpeihua
 * @date 2020/11/25 20:55
 * @version 1.0
 */
abstract class OnNoDoubleClickListener(timeMillis: Long = 500) : View.OnClickListener {
    constructor() : this(500)

    /**
     * 两次单击时间间隔，默认500ms
     */
    private var defaultDelayTime = timeMillis
    private var mLastClickTime: Long = 0

    /**
     * 上一次点击的时间
     */

    override fun onClick(v: View) {
        val currentTime = System.currentTimeMillis()
        if (currentTime - mLastClickTime > defaultDelayTime) {
            mLastClickTime = currentTime
            onNoDoubleClick(v)
        }
    }

    abstract fun onNoDoubleClick(view: View?)
}