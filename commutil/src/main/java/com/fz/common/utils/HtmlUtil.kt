package com.fz.common.utils

import android.os.Build
import android.text.Html
import android.text.Spanned

/**
 * @author Shyky
 * @version 1.1
 * @email sj1510706@163.com
 * @date 2016/4/13
 * @since 1.0
 */
fun String?.fromHtml(): Spanned {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        Html.fromHtml(this, Html.FROM_HTML_MODE_LEGACY)
    } else {
        Html.fromHtml(this)
    }
}