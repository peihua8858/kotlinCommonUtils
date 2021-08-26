@file:JvmName("HtmlUtil")

package com.fz.common.utils

import android.os.Build
import android.text.Html
import android.text.SpannableString
import android.text.Spanned

/**
 * @return Spanned
 * @date 2016/4/13
 * @since 1.0
 */
fun String?.fromHtml(): Spanned {
    return this?.let {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Html.fromHtml(this, Html.FROM_HTML_MODE_LEGACY)
        } else {
            Html.fromHtml(this)
        }
    } ?: SpannableString("")
}