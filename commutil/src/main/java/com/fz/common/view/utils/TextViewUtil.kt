@file:JvmName("TextViewUtil")
@file:JvmMultifileClass
package com.fz.common.view.utils

import android.graphics.drawable.Drawable
import android.text.Editable
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import com.fz.common.listener.EditTextWatcher
import com.fz.common.listener.IControlEnabledListener
import com.fz.common.utils.eLog
import com.google.android.material.textfield.TextInputLayout

fun TextView?.setDrawableStart(@DrawableRes start: Int): Drawable? {
    if (this == null) {
        return null
    }
    val drawables: Array<Drawable> = this.compoundDrawablesRelative
    val startDrawable: Drawable? =
            if (start != 0) ContextCompat.getDrawable(context, start) else null.eLog { "start  is 0." }
    this.setCompoundDrawablesRelativeWithIntrinsicBounds(
            startDrawable, drawables[1],
            drawables[2], drawables[3]
    )
    return startDrawable
}

fun TextView?.setDrawableTop(@DrawableRes top: Int): Drawable? {
    if (this == null) {
        return null
    }
    val drawables: Array<Drawable> = this.compoundDrawablesRelative
    val topDrawable: Drawable? = if (top != 0) ContextCompat.getDrawable(context, top) else null.eLog { "top  is 0." }
    this.setCompoundDrawablesRelativeWithIntrinsicBounds(
            drawables[0], topDrawable,
            drawables[2], drawables[3]
    )
    return topDrawable
}

fun TextView?.setDrawableEnd(@DrawableRes end: Int): Drawable? {
    if (this == null) {
        return null
    }
    val drawables: Array<Drawable> = this.compoundDrawablesRelative
    val endDrawable: Drawable? = if (end != 0) ContextCompat.getDrawable(context, end) else null.eLog { "end  is 0." }
    this.setCompoundDrawablesRelativeWithIntrinsicBounds(
            drawables[0], drawables[1],
            endDrawable, drawables[3]
    )
    return endDrawable
}

fun TextView?.setDrawableBottom(@DrawableRes bottom: Int): Drawable? {
    if (this == null) {
        return null
    }
    val drawables: Array<Drawable> = this.compoundDrawablesRelative
    val bottomDrawable: Drawable? =
            if (bottom != 0) ContextCompat.getDrawable(context, bottom) else null.eLog { "bottom  is 0." }
    this.setCompoundDrawablesRelativeWithIntrinsicBounds(
            drawables[0], drawables[1], drawables[2],
            bottomDrawable
    )
    return bottomDrawable
}

fun Any?.setAfterTextChanged(listener: IControlEnabledListener?, vararg views: EditText?) {
    for (view in views) {
        view.setAfterTextChanged(listener)
    }
}

fun EditText?.setAfterTextChanged(listener: IControlEnabledListener?) {
    this?.addTextChangedListener(AfterEditTextWatcher(listener, this))
}

private class AfterEditTextWatcher(
        val view: EditText?,
        listeners: Array<out IControlEnabledListener?>
) :
        EditTextWatcher {
    private val mListeners = listeners

    constructor(listener: IControlEnabledListener?, view: EditText?) : this(view, arrayOf(listener))

    override fun afterTextChanged(s: Editable?) {
        for (listener in mListeners) {
            listener?.onControlEnabled()
        }
        if (!s.isNullOrEmpty()) {
            val parent = view?.parent?.parent
            if (parent is TextInputLayout) {
                parent.error = null
                parent.isErrorEnabled = false
            }
        }
    }
}

fun EditText?.setAfterTextChanged(vararg listeners: IControlEnabledListener?) {
    this?.addTextChangedListener(AfterEditTextWatcher(this, listeners))
}

fun Any?.setOnFocusChangeListener(listener: IControlEnabledListener?, vararg views: EditText?) {
    for (view in views) {
        view.setOnFocusChangeListener(listener)
    }
}

fun EditText?.setOnFocusChangeListener(listener: IControlEnabledListener?) {
    this?.onFocusChangeListener = FocusChangeListener(listener, this)
}

fun EditText?.setOnFocusChangeListener(vararg listeners: IControlEnabledListener?) {
    this?.onFocusChangeListener = FocusChangeListener(this, listeners)
}

private class FocusChangeListener(
        val view: EditText?,
        listeners: Array<out IControlEnabledListener?>
) : View.OnFocusChangeListener {
    private val mListeners = listeners

    constructor(listener: IControlEnabledListener?, view: EditText?) : this(view, arrayOf(listener))

    override fun onFocusChange(v: View?, hasFocus: Boolean) {
        if (hasFocus) {
            val parent = view?.parent?.parent
            if (parent is TextInputLayout) {
                parent.error = null
                parent.isErrorEnabled = false
            }
        } else {
            for (l in mListeners) {
                l?.onControlEnabled()
            }
        }
    }
}