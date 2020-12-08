package com.fz.common.utils

import android.net.Uri
import com.fz.common.text.isNonEmpty
import java.net.URLDecoder
import java.net.URLEncoder
import java.util.regex.Pattern

/**
 * url 相关工具类
 *
 * @author dingpeihua
 * @version 1.0
 * @date 2019/5/20 18:06
 */
/**
 * utf8编码 字符集
 */
private val UTF8_PATTERN =
        Pattern.compile("^([\\x00-\\x7f]|[\\xc0-\\xdf][\\x80-\\xbf]|[\\xe0-\\xef][\\x80-\\xbf]{2}|[\\xf0-\\xf7][\\x80-\\xbf]{3}|[\\xf8-\\xfb][\\x80-\\xbf]{4}|[\\xfc-\\xfd][\\x80-\\xbf]{5})+$")

/**
 * 通用字符集(utf-8和GBK)
 */
private val PUBLIC_PATTERN = Pattern.compile("^([\\x01-\\x7f]|[\\xc0-\\xdf][\\x80-\\xbf])+$")

/**
 * 正则方式判断字符编码，默认为UTF-8
 *
 * @param uri
 * @return
 */
fun Any.isDecodeURL(uri: Uri?): Boolean {
    return uri?.toString()?.isDecodeURL() ?: false
}

/**
 * 正则方式判断字符编码，默认为UTF-8
 *
 * @param encodeValue
 * @return 如果字符已经编码过，则返回true，否则false
 */
fun String?.isDecodeURL(): Boolean {
    /**
     * 通用字符集判断
     */
    return this?.let {
        val publicMatcher = PUBLIC_PATTERN.matcher(this)
        if (publicMatcher.matches()) {
            return true
        }
        val matcher = UTF8_PATTERN.matcher(this)
        return matcher.matches()
    } ?: false
}

fun Uri?.decodeUrl(): Uri? {
    return this?.let {
        Uri.parse(it.toString().decodeUrl())
    }
}

fun String?.decodeUrl(): String? {
    return this?.let { it ->
        var tempUrl = it
        if (tempUrl.isNotEmpty()) {
            try {
                tempUrl = URLDecoder.decode(tempUrl, "utf-8")
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        return tempUrl.trim { it <= ' ' }
    }
}

/**
 * url 加密
 *
 * @param url
 * @author dingpeihua
 * @date 2020/4/13 15:49
 * @version 1.0
 */
fun String?.encodeUrl(): String? {
    return this?.let { it ->
        var tempUrl = it
        if (tempUrl.isNotEmpty()) {
            try {
                tempUrl = URLEncoder.encode(tempUrl, "utf-8")
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        tempUrl.trim { it <= ' ' }
    }
}

/**
 * 编码URL中带有的%和#
 *
 * @param url
 * @author dingpeihua
 * @date 2019/12/11 10:54
 * @version 1.0
 */
fun String?.encodeUrlPercentSign(url: String?): String? {
    return this?.let {
        var tempUrl: String = it
        //将url中含有#特殊字符替换为%23
        try {
            tempUrl = tempUrl.replace("#".toRegex(), URLEncoder.encode("#", "utf-8"))
            tempUrl = tempUrl.replace("%".toRegex(), URLEncoder.encode("%", "utf-8"))
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
        tempUrl
    }
}

fun CharSequence?.addUriParameter(key: String, value: String): String? {
    return this?.let {
        val sbUrl: StringBuilder = StringBuilder(it)
        sbUrl.append(if (sbUrl.contains("?")) {
            "&$key=$value"
        } else {
            "?$key=$value"
        })
        sbUrl.toString()
    }
}

/**
 * 获取Uri参数的异常处理
 *
 * @param uri 需要获取指定参数的uri
 * @param key 参数key值
 * @return
 * @author dingpeihua
 * @date 2016/5/28 17:46
 * @version 1.0
 */
fun Uri?.getUriParameter(key: String): String {
    return this?.let {
        val parameter: String? = try {
            getQueryParameter(key)
        } catch (e: Exception) {
            ""
        }
        return if (parameter.isNonEmpty()) parameter else ""
    } ?: ""
}

/**
 * 获取Uri参数的异常处理
 *
 * @param uri 需要获取指定参数的uri
 * @param key 参数key值
 * @return
 * @author dingpeihua
 * @date 2016/5/28 17:46
 * @version 1.0
 */
fun String?.getUriParameter(key: String): String {
    return this?.let {
        try {
            val uri = Uri.parse(it)
            uri.getUriParameter(key)
        } catch (e: java.lang.Exception) {
            null
        }
    } ?: ""
}