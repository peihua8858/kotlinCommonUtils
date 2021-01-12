@file:JvmName("KeyboardUtil")
@file:JvmMultifileClass
package com.fz.common.utils

import android.content.Context
import android.inputmethodservice.InputMethodService
import android.view.View
import android.view.inputmethod.InputMethodManager

/**
 * 显示软键盘
 *
 * @param view 控件
 */
fun View?.showSoftKeyboard() {
    if (this.isNull()) {
        return
    }
    showSoftKeyboard(context, this)
}

/**
 * 显示软键盘
 *
 * @param context 应用程序上下文
 * @param view    控件
 */
fun Any?.showSoftKeyboard(context: Context?, view: View?) {
    if (context != null && view != null) {
        try {
            val imm:InputMethodManager =
                    context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(view,0)
        } catch (e: Throwable) {
            e.printStackTrace()
        }
    }
}

/**
 * 隐藏软键盘
 *
 * @param view 控件
 */
fun View?.hideSoftKeyboard() {
    if (this == null) {
        return
    }
    hideSoftKeyboard(context, this)
}

/**
 * 隐藏软键盘
 *
 * @param context 当前Activity
 * @param view    控件
 */
fun Any?.hideSoftKeyboard(context: Context?, view: View?) {
    if (context != null && view != null) {
        try {
            val imm =
                    context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        } catch (e: Throwable) {
            e.printStackTrace()
        }
    }
}