@file:JvmName("UrlUtil")
@file:JvmMultifileClass
package com.fz.common.utils

import android.net.Uri
import androidx.annotation.DrawableRes
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
//    /**
//     * utf8编码 字符集
//     */
//    private static final Pattern UTF8_PATTERN = Pattern.compile("^([\\x00-\\x7f]|[\\xc0-\\xdf][\\x80-\\xbf]|[\\xe0-\\xef][\\x80-\\xbf]{2}|[\\xf0-\\xf7][\\x80-\\xbf]{3}|[\\xf8-\\xfb][\\x80-\\xbf]{4}|[\\xfc-\\xfd][\\x80-\\xbf]{5})+$");
//    /**
//     * 通用字符集(utf-8和GBK)
//     */
//    private static final Pattern PUBLIC_PATTERN = Pattern.compile("^([\\x01-\\x7f]|[\\xc0-\\xdf][\\x80-\\xbf])+$");
//    /**
//     * 正则方式判断字符编码，默认为UTF-8
//     *
//     * @param uri
//     * @return
//     */
//    public static boolean isDecodeURL(Uri uri) {
//        if (uri != null) {
//            return isDecodeURL(uri.toString());
//        }
//        return false;
//    }
//
//    /**
//     * 正则方式判断字符编码，默认为UTF-8
//     *
//     * @param encodeValue
//     * @return 如果字符已经编码过，则返回true，否则false
//     */
//    public static boolean isDecodeURL(String encodeValue) {
//        /**
//         * 通用字符集判断
//         */
//        Matcher publicMatcher = PUBLIC_PATTERN.matcher(encodeValue);
//        if (publicMatcher.matches()) {
//            return true;
//        }
//        Matcher matcher = UTF8_PATTERN.matcher(encodeValue);
//        return matcher.matches();
//    }
//
//    public static Uri decodeUrl(Uri uri) {
//        if (uri != null) {
//            return Uri.parse(decodeUrl(uri.toString()));
//        }
//        return null;
//    }
//
//    public static String decodeUrl(String url) {
//        if (TextUtil.isNotEmpty(url)) {
//            try {
//                url = URLDecoder.decode(url, "utf-8");
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//        return url.trim();
//    }
//
//    /**
//     * url 加密
//     *
//     * @param url
//     * @author dingpeihua
//     * @date 2020/4/13 15:49
//     * @version 1.0
//     */
//    public static String encodeUrl(String url) {
//        if (TextUtil.isNotEmpty(url)) {
//            try {
//                url = URLEncoder.encode(url, "utf-8");
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//            return url.trim();
//        }
//        return "";
//    }
/**
 * 编码URL中带有的%、#和+
 *
 * @param url
 * @author dingpeihua
 * @date 2019/12/11 10:54
 * @version 1.0
 */
fun Any?.encodeUrlPercentSign(url: String?): String? {
    if (url.isNonEmpty()) {
        //将url中含有特殊字符需要替换
        //% => %25  # => %23  + => %2B
        try {
            //必须先编码%号，如果先编码 + 和 # 会导致# 和 + 在取参数之后无法还原
            return url.replace("%".toRegex(), URLEncoder.encode("%", "utf-8"))
                    .replace("\\+".toRegex(), URLEncoder.encode("+", "utf-8"))
                    .replace("#".toRegex(), URLEncoder.encode("#", "utf-8"))
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }
    return url
}

/**
 * 编码URL中带有的%和#
 *
 * @param url
 * @author dingpeihua
 * @date 2019/12/11 10:54
 * @version 1.0
 */
fun String?.encodeUrlPercentSign(): String? {
    return this?.let {
        encodeUrlPercentSign(it)
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

fun Any?.getResourceUrl(@DrawableRes resId: Int): String {
    val context = checkContext(this)
    checkNotNull(context, "Context is null.")
    val packageName = context?.packageName
    checkNotNull(packageName, "Context is null.")
    return getResourceUrl(packageName!!, resId)
}

fun Any?.getResourceUrl(packageName: String, @DrawableRes resId: Int): String {
    return "android.resource://$packageName/$resId"
}