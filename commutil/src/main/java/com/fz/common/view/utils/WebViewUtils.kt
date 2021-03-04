@file:JvmName("WebViewUtils")
package com.fz.common.view.utils

import android.webkit.WebView

/**
 * 处理webview漏洞，删除危险API
 *
 * @param webView
 */
fun WebView?.dealJavascriptLeak() {
    if (this == null) {
        return
    }
    try {
        removeJavascriptInterface("searchBoxJavaBridge_")
        removeJavascriptInterface("accessibility")
        removeJavascriptInterface("accessibilityTraversal")
    } catch (e: Exception) {
        e.printStackTrace()
    }
}