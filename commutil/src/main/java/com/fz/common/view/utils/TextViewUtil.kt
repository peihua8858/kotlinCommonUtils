@file:JvmName("TextViewUtil")

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

fun TextView?.setDrawableStart(start: Drawable?): Drawable? {
    if (this == null) {
        return null
    }
    val drawables: Array<Drawable> = this.compoundDrawablesRelative
    if (start != null && start.bounds.isEmpty) {
        this.setCompoundDrawablesRelativeWithIntrinsicBounds(
                start, drawables[1],
                drawables[2], drawables[3]
        )
    } else {
        this.setCompoundDrawablesRelative(
                start, drawables[1],
                drawables[2], drawables[3]
        )
    }
    return start
}

fun TextView?.setDrawableStart(@DrawableRes start: Int): Drawable? {
    if (this == null) {
        return null
    }
    return setDrawableStart(if (start != 0) ContextCompat.getDrawable(context, start) else null.eLog { "start  is 0." })
}

fun TextView?.setDrawableTop(top: Drawable?): Drawable? {
    if (this == null) {
        return null
    }
    val drawables: Array<Drawable> = this.compoundDrawablesRelative
    if (top != null && top.bounds.isEmpty) {
        this.setCompoundDrawablesRelativeWithIntrinsicBounds(
                drawables[0], top,
                drawables[2], drawables[3]
        )
    } else {
        this.setCompoundDrawablesRelative(
                drawables[0], top,
                drawables[2], drawables[3]
        )
    }
    return top
}

fun TextView?.setDrawableTop(@DrawableRes top: Int): Drawable? {
    if (this == null) {
        return null
    }
    return setDrawableTop(if (top != 0) ContextCompat.getDrawable(context, top) else null.eLog { "top  is 0." })
}

fun TextView?.setDrawableEnd(end: Drawable?): Drawable? {
    if (this == null) {
        return null
    }
    val drawables: Array<Drawable> = this.compoundDrawablesRelative
    if (end != null && end.bounds.isEmpty) {
        this.setCompoundDrawablesRelativeWithIntrinsicBounds(
                drawables[0], drawables[1],
                end, drawables[3]
        )
    } else {
        this.setCompoundDrawablesRelative(
                drawables[0], drawables[1],
                end, drawables[3]
        )
    }
    return end
}

fun TextView?.setDrawableEnd(@DrawableRes end: Int): Drawable? {
    if (this == null) {
        return null
    }
    return setDrawableEnd(if (end != 0) ContextCompat.getDrawable(context, end) else null.eLog { "end  is 0." })
}

fun TextView?.setDrawableBottom(bottom: Drawable?): Drawable? {
    if (this == null) {
        return null
    }
    val drawables: Array<Drawable> = this.compoundDrawablesRelative
    if (bottom != null && bottom.bounds.isEmpty) {
        this.setCompoundDrawablesRelativeWithIntrinsicBounds(
                drawables[0], drawables[1], drawables[2],
                bottom
        )
    } else {
        this.setCompoundDrawablesRelative(
                drawables[0], drawables[1], drawables[2],
                bottom
        )
    }
    return bottom
}

fun TextView?.setDrawableBottom(@DrawableRes bottom: Int): Drawable? {
    if (this == null) {
        return null
    }
    return setDrawableBottom(if (bottom != 0) ContextCompat.getDrawable(context, bottom) else null.eLog { "bottom  is 0." })
}

fun Any?.setAfterTextChanged(listener: IControlEnabledListener?, vararg views: EditText?) {
    for (view in views) {
        view.setAfterTextChanged(listener)
    }
}

fun EditText?.setAfterTextChanged(block: (CharSequence) -> Unit) {
    this?.addTextChangedListener(EditTextAfterWatcher(this, block))
}

fun EditText?.setAfterTextChanged(listener: IControlEnabledListener?) {
    this?.addTextChangedListener(AfterEditTextWatcher(listener, this))
}

private class EditTextAfterWatcher(
        val view: EditText,
        val block: (CharSequence) -> Unit,
) : EditTextWatcher {
    override fun afterTextChanged(s: Editable?) {
        block(if (s.isNullOrEmpty()) "" else s)
    }
}

private class AfterEditTextWatcher(
        val view: EditText,
        listeners: Array<out IControlEnabledListener?>,
) :
        EditTextWatcher {
    private val mListeners = listeners

    constructor(listener: IControlEnabledListener?, view: EditText) : this(view, arrayOf(listener))

    override fun afterTextChanged(s: Editable?) {
        for (listener in mListeners) {
            listener?.onControlEnabled()
        }
        if (!s.isNullOrEmpty()) {
            val parent = view.parent?.parent
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
        listeners: Array<out IControlEnabledListener?>,
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