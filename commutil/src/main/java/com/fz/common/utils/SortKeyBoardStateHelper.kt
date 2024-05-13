package com.fz.common.utils

import android.app.Activity
import android.graphics.Rect
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.view.WindowManager
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner

/**
 *
 * 监听键盘显示和隐藏
 * @date 2024/5/13 14:13
 **/
class SortKeyBoardStateHelper @JvmOverloads constructor(
    val activity: FragmentActivity,
    private val listener: OnKeyBoardStateListener? = null
) : ViewTreeObserver.OnGlobalLayoutListener,
    DefaultLifecycleObserver {


    private var wasOpened = false

    init {
        activity.lifecycle.addObserver(this)
    }

    override fun onCreate(owner: LifecycleOwner) {
        checkSortInputMode(activity)
        val activityRoot = getActivityRoot(activity)
        activityRoot.viewTreeObserver.addOnGlobalLayoutListener(this)
    }

    override fun onGlobalLayout() {
        //监听键盘弹起
        val isOpen = isKeyboardVisible(activity)
        if (isOpen == wasOpened) {
            // keyboard state has not changed
            return
        }
        wasOpened = isOpen
        listener?.onKeyBoardStateChange(isOpen)
    }

    override fun onDestroy(owner: LifecycleOwner) {
        val activityRoot = getActivityRoot(activity)
        activityRoot.viewTreeObserver.removeOnGlobalLayoutListener(this)
    }

    fun isShowKeyBoard(): Boolean {
        return isKeyboardVisible(activity)
    }

    interface OnKeyBoardStateListener {
        fun onKeyBoardStateChange(isShow: Boolean)
    }

    companion object {
        private const val KEYBOARD_MIN_HEIGHT_RATIO = 0.15

        @JvmStatic
        fun isKeyboardVisible(activity: Activity): Boolean {
            checkSortInputMode(activity)
            val r = Rect()
            val activityRoot = getActivityRoot(activity)
            activityRoot.getWindowVisibleDisplayFrame(r)
            val location = IntArray(2)
            getContentRoot(activity).getLocationOnScreen(location)
            val screenHeight = activityRoot.rootView.height
            val heightDiff = screenHeight - r.height() - location[1]
            return heightDiff > screenHeight * KEYBOARD_MIN_HEIGHT_RATIO
        }

        private fun getActivityRoot(activity: Activity): View {
            return getContentRoot(activity).rootView
        }

        private fun getContentRoot(activity: Activity): ViewGroup {
            return activity.findViewById(android.R.id.content)
        }

        private fun checkSortInputMode(activity: Activity) {
            val softInputAdjust =
                activity.window.attributes.softInputMode and WindowManager.LayoutParams.SOFT_INPUT_MASK_ADJUST

            // fix for #37 and #38.
            // The window will not be resized in case of SOFT_INPUT_ADJUST_NOTHING
            val isNotAdjustNothing =
                softInputAdjust and WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING != WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING
            require(isNotAdjustNothing) { "Parameter:activity window SoftInputMethod is SOFT_INPUT_ADJUST_NOTHING. In this case window will not be resized" }
        }
    }
}