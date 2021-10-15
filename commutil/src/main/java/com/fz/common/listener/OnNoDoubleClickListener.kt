package com.fz.common.listener

import android.view.View

/**
 * 处理连续单击，默认两次单击时间间隔是500ms
 * @author dingpeihua
 * @date 2020/11/25 20:55
 * @version 1.0
 */

open class OnNoDoubleClickListener @JvmOverloads constructor(private val listener: View.OnClickListener? = null, timeMillis: Long = 500) : View.OnClickListener {

    /**
     * 两次单击时间间隔，默认500ms
     */
    private var defaultDelayTime = timeMillis
    private var mLastClickTime: Long = 0

    /**
     * 上一次点击的时间
     */

    final override fun onClick(v: View) {
        val currentTime = System.currentTimeMillis()
        if (currentTime - mLastClickTime > defaultDelayTime) {
            mLastClickTime = currentTime
            if (listener != null) {
                listener.onClick(v)
            } else {
                onNoDoubleClick(v)
            }
        }
    }

    open fun onNoDoubleClick(view: View?) {}
}