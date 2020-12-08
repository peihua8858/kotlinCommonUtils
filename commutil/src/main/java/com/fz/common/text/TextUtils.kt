package com.fz.common.text

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.util.Base64
import android.util.Patterns
import android.widget.EditText
import java.io.UnsupportedEncodingException
import java.net.URLEncoder
import java.util.regex.Matcher
import java.util.regex.Pattern
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract

/**
 * 获取input的字符串
 *
 * @param editText 输入框
 * @return 输入字符串
 */
fun EditText?.getEditToString(): String? {
    return this?.text?.toString()?.trim { it <= ' ' }
}

/**
 * 获取input的字符串
 *
 * @param editText 输入框
 * @return 输入字符串
 */
fun Any?.getEditToString(editText: EditText?): String? {
    return editText?.text?.toString()?.trim { it <= ' ' }
}

fun CharSequence?.copyTextToClipboard(context: Context): Boolean {
    if (this == null) {
        return false
    }
    val c = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    val clipData = ClipData.newPlainText("text label", this)
    c.setPrimaryClip(clipData)
    return true
}

/**
 * 实现粘贴功能
 *
 * @param context
 * @author dingpeihua
 * @date 2019/10/16 18:22
 * @version 1.0
 */
fun Any?.pasteText(context: Context): CharSequence? {
    // 得到剪贴板管理器
    val cmb = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    val clip = cmb.primaryClip
    return if (clip != null && clip.itemCount > 0) {
        clip.getItemAt(0).coerceToText(context)
    } else null
}

fun String?.toBase64(): ByteArray? {
    return if (null == this) {
        null
    } else Base64.decode(toByteArray(), Base64.NO_WRAP)
}

/**
 * 验证是否是IP地址
 *
 * @param target 要验证的文本
 * @return 是返回true, 否则返回false
 */
fun CharSequence?.isIpAddress(): Boolean {
    return !this.isNullOrEmpty() && Patterns.IP_ADDRESS.matcher(this).matches()
}

/**
 * 验证是否是邮箱
 *
 * @param target 要验证的文本
 * @return 是返回true, 否则返回false
 */
fun CharSequence?.isEmail(): Boolean {
    return this != null && Patterns.EMAIL_ADDRESS.matcher(this).matches()
}

@OptIn(ExperimentalContracts::class)
fun CharSequence?.isNonEmpty(): Boolean {
    contract {
        returns(true) implies (this@isNonEmpty != null)
    }
    return this != null && length > 0 && isNotBlank()
}

@OptIn(ExperimentalContracts::class)
fun Any?.isNonEmpty(text: CharSequence?): Boolean {
    contract {
        returns(true) implies (this@isNonEmpty != null)
    }
    return text != null && text.isNotEmpty() && text.isNotBlank()
}

fun conValidate(con: CharSequence?): Boolean {
    if (null != con && "" != con) {
        if ((con.isChinese() || con.matches(Regex("^[A-Za-z]+$")))
                && con.length <= 10
        ) {
            return true
        }
    }
    return false
}

/**
 * 判断当前字符串是不是包含中文
 * @author dingpeihua
 * @date 2020/11/26 9:10
 * @version 1.0
 */
fun CharSequence?.isChinese(): Boolean {
    if (this == null) {
        return false
    }
    val pattern = Pattern.compile("[\\u4e00-\\u9fa5]")
    val matcher: Matcher = pattern.matcher(this)
    return matcher.find()
}

/**
 * 编码中文
 *
 * @param url
 * @author dingpeihua
 * @date 2019/7/6 18:47
 * @version 1.0
 */
fun CharSequence.encodeChinese(): CharSequence {
    var tempUrl = this
    try {
        val matcher: Matcher = Pattern.compile("[\\u4e00-\\u9fa5]").matcher(tempUrl)
        while (matcher.find()) {
            val tmp: String = matcher.group()
            tempUrl = tempUrl.replace(tmp.toRegex(), URLEncoder.encode(tmp, "UTF-8"))
        }
    } catch (e: UnsupportedEncodingException) {
        e.printStackTrace()
    }
    return tempUrl
}

/**
 * 验证文本是否是正整数
 *
 * @param text 要验证的文本
 * @return 是返回true, 否则返回false
 */
fun CharSequence?.isPositiveInteger(): Boolean {
    return isNonEmpty() && matches("^[1-9]d*$".toRegex())
}

/**
 * 验证文本是否是负整数
 *
 * @param text 要验证的文本
 * @return 是返回true, 否则返回false
 */
fun CharSequence?.isNegativeInteger(): Boolean {
    return isNonEmpty() && matches("^-[1-9]d*$".toRegex())
}

/**
 * 验证文本是否是浮点数
 *
 * @param text 要验证的文本
 * @return 是返回true, 否则返回false
 */
fun CharSequence?.isFloatNumber(): Boolean {
    return this.isNonEmpty() && matches("^-?([1-9]d*.d*|0.d*[1-9]d*|0?.0+|0)$".toRegex())
}

/**
 * 验证文本是否是双精度浮点数
 *
 * @param text 要验证的文本
 * @return 是返回true, 否则返回false
 */
fun CharSequence?.isDoubleNumber(): Boolean {
    return this.isNonEmpty() && matches("^\\d{0,8}\\.{0,1}(\\d{1,2})?$".toRegex())
}

/**
 * 验证文本是否是数字
 *
 * @param text 要验证的文本
 * @return 是返回true, 否则返回false
 */
fun CharSequence?.isNumber(): Boolean {
    return isNonEmpty() && matches("^[0-9]*$".toRegex())
}

/**
 * 验证文本是否是整数
 *
 * @param text 要验证的文本
 * @return 是返回true, 否则返回false
 */
fun CharSequence?.isInteger(): Boolean {
    return isNonEmpty() && matches("^-?[1-9]d*$".toRegex())
}

/**
 * 验证是否包含数字
 *
 * @param target 要验证的文本
 * @return 是返回true, 否则返回false
 */
fun CharSequence?.isExistNumber(): Boolean {
    return isNonEmpty() && this.matches(".*\\d+.*".toRegex())
}

/**
 * 验证是否包含字母
 *
 * @param target 要验证的文本
 * @return 是返回true, 否则返回false
 */
fun CharSequence?.isExistChar(): Boolean {
    return isNonEmpty() && this.matches(".*[a-zA-Z]+.*".toRegex())
}