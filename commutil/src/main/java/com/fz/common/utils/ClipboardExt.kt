package com.fz.common.utils

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import com.fz.common.text.isNonEmpty

fun CharSequence?.copyTextToClipboard(context: Context): Boolean {
    if (this == null) {
        return false
    }
    val c = context.clipboardManager
    val clipData = ClipData.newPlainText("text", this)
    try {
        c?.setPrimaryClip(clipData)
        return true
    } catch (e: Throwable) {
        e.printStackTrace()
    }
    return false
}

fun Context.pasteIntent(): Intent? {
    return pasteIntent(0)
}

fun Context.pasteText(): CharSequence {
    return pasteText(0)
}

/**
 * 实现粘贴功能
 *
 * @param [this]
 * @author dingpeihua
 * @date 2019/10/16 18:22
 * @version 1.0
 */
fun Context.pasteText(index: Int): CharSequence {
    val cm = clipboardManager
    try {
        if (cm != null && cm.hasPrimaryClip()) {
            val primaryClip = cm.primaryClip
            val result = primaryClip?.getItemAt(index)?.text
            if (result.isNonEmpty()) {
                return result
            }

        }
    } catch (e: Throwable) {
        e.printStackTrace()
    }
    return ""
}


/**
 * 粘贴到系统剪贴板
 */
fun Context?.copyToClipBoard(lazyContent: () -> CharSequence) {
    copyToClipBoard(lazyContent) {}
}

/**
 * 粘贴到系统剪贴板
 */
fun Context?.copyToClipBoard(lazyContent: () -> CharSequence, callback: ((Boolean) -> Unit)? = null) {
    val content = lazyContent()
    val context = checkContext(this)
    if (context != null) context.copyToClipBoard(
        content,
        callback
    ) else callback?.invoke(false).eLog { "Context  is null." }
}

/**
 * 粘贴到系统剪贴板
 */
@JvmOverloads
fun Context.copyToClipBoard(content: CharSequence, callback: ((Boolean) -> Unit)? = null) {
    val cm = clipboardManager
    val clipData = ClipData.newPlainText("text", content)
    cm?.let {
        callback?.let {
            cm.addPrimaryClipChangedListener(object :
                ClipboardManager.OnPrimaryClipChangedListener {
                override fun onPrimaryClipChanged() {
                    cm.removePrimaryClipChangedListener(this)
                    callback(true)
                }
            })
        }
        try {
            cm.setPrimaryClip(clipData)
        } catch (e: Throwable) {
            e.printStackTrace()
            callback?.invoke(false)
        }
    }
}

@JvmOverloads
fun Context.copyToClipBoard(intent: Intent, callback: ((Boolean) -> Unit)? = null) {
    val cm = clipboardManager
    val clipData = ClipData.newIntent("Intent", intent)
    cm?.let {
        callback?.let {
            cm.addPrimaryClipChangedListener(object :
                ClipboardManager.OnPrimaryClipChangedListener {
                override fun onPrimaryClipChanged() {
                    cm.removePrimaryClipChangedListener(this)
                    callback(true)
                }
            })
        }
        try {
            cm.setPrimaryClip(clipData)
        } catch (e: Throwable) {
            e.printStackTrace()
            callback?.invoke(false)
        }
    }
}

@JvmOverloads
fun Context.copyToClipBoard(uri: Uri, callback: ((Boolean) -> Unit)? = null) {
    val cm = clipboardManager
    val clipData = ClipData.newUri(contentResolver, "URI", uri)
    cm?.let {
        callback?.let {
            cm.addPrimaryClipChangedListener(object :
                ClipboardManager.OnPrimaryClipChangedListener {
                override fun onPrimaryClipChanged() {
                    cm.removePrimaryClipChangedListener(this)
                    callback(true)
                }
            })
        }
        try {
            cm.setPrimaryClip(clipData)
        } catch (e: Throwable) {
            e.printStackTrace()
            callback?.invoke(false)
        }
    }
}

fun Context.pasteIntent(index: Int): Intent? {
    val cm = clipboardManager
    try {
        if (cm != null && cm.hasPrimaryClip()) {
            val primaryClip = cm.primaryClip
            return primaryClip?.getItemAt(index)?.intent
        }
    } catch (e: Throwable) {
        e.printStackTrace()
    }
    return null
}

fun Context.pasteUri(): Uri? {
    return pasteUri(0)
}

fun Context.pasteUri(index: Int): Uri? {
    val cm = clipboardManager
    try {
        if (cm != null && cm.hasPrimaryClip()) {
            val primaryClip = cm.primaryClip
            return primaryClip?.getItemAt(index)?.uri
        }
    } catch (e: Throwable) {
        e.printStackTrace()
    }
    return null
}
