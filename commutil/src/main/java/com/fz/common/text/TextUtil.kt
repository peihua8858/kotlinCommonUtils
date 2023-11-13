@file:JvmName("TextUtil")
@file:JvmMultifileClass

package com.fz.common.text

import android.content.ClipData
import android.content.Context
import android.text.TextUtils
import android.util.Base64
import android.util.Patterns
import android.widget.EditText
import android.widget.TextView
import com.fz.common.utils.clipboardManager
import java.io.UnsupportedEncodingException
import java.net.URLEncoder
import java.util.*
import java.util.regex.Matcher
import java.util.regex.Pattern
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract

/**
 * 获取input的字符串
 *
 * @return 输入字符串
 */
fun EditText?.editToString(block: (CharSequence) -> Unit) {
    block(editTextToString())
}

/**
 * 获取input的字符串
 *
 * @return 输入字符串
 */
fun EditText?.getEditToString(block: (CharSequence) -> Unit) {
    block(editTextToString())
}

/**
 * 获取input的字符串
 *
 * @param editText 输入框
 * @return 输入字符串
 */
fun EditText?.editTextToString(): String {
    return this?.text?.toString()?.trim { it <= ' ' } ?: ""
}

/**
 * 获取input的字符串
 *
 * @param editText 输入框
 * @return 输入字符串
 */
fun EditText?.getEditToString(): String {
    return editTextToString()
}

/**
 * 获取input的字符串
 *
 * @param editText 输入框
 * @return 输入字符串
 */
fun Any?.getEditToString(editText: EditText?): String {
    return editText.editTextToString()
}

fun TextView?.textViewToString(block: (CharSequence) -> Unit) {
    block(textViewToString())
}

/**
 * 获取[TextView]的字符串
 *
 * @param textView 输入框
 * @return 输入字符串
 */
fun Any.getTextToString(textView: TextView?): String {
    return textView.textViewToString()
}

fun TextView?.textViewToString(): String {
    return this?.text?.toString()?.trim { it <= ' ' } ?: ""
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

/**
 * 判断字符串是否为空
 * @author dingpeihua
 * @date 2020/8/7 8:58
 * @version 1.0
 */
@OptIn(ExperimentalContracts::class)
fun CharSequence?.isEmptyOrBlank(): Boolean {
    contract {
        returns(false) implies (this@isEmptyOrBlank != null)
    }
    return isNullOrEmpty() || isNullOrBlank()
}

/**
 * 判断字符串是否为空
 * @author dingpeihua
 * @date 2020/8/7 8:58
 * @version 1.0
 */
@OptIn(ExperimentalContracts::class)
fun CharSequence?.isNonEmpty(): Boolean {
    contract {
        returns(true) implies (this@isNonEmpty != null)
    }
    return !isNullOrEmpty() && isNotBlank()
}

/**
 * 判断多个字符串是否为空，如果有某一个为空则返回false，否则返回true
 * @param text
 * @author dingpeihua
 * @date 2021/1/11 11:02
 * @version 1.0
 */
fun isNonEmpty(vararg text: CharSequence?): Boolean {
    if (text.isEmpty()) {
        return false
    }
    for (c in text) {
        if (c.isNullOrEmpty()) {
            return false
        }
    }
    return true
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

/**
 * 用户电话号码的打码隐藏加星号加*
 *
 * @return 处理完成的身份证
 */
fun CharSequence?.phoneMask(): String {
    var res = ""
    if (this.isNonEmpty()) {
        val stringBuilder = StringBuilder(this)
        res = stringBuilder.replace(4, 9, "****").toString()
    }
    return res
}

const val MOBILE_PHONE =
    "^((\\+86)?(13\\d|14[5-9]|15[0-35-9]|16[5-6]|17[0-8]|18\\d|19[158-9])\\d{8})$"

val MOBILE_PHONE_PATTERN: Pattern = Pattern.compile(MOBILE_PHONE)
fun CharSequence?.isPhoneNumber(): Boolean {
    return this != null && MOBILE_PHONE_PATTERN.matcher(this).matches()
}

/**
 * 生成保护隐私的用户昵称
 * @param originNickname 原始用户昵称
 * @param defaultName 默认显示（如果originNickname参数为空，则默认返回改参数值）
 * @return a*****b 中间带了*号的昵称
 * @author dingpeihua
 * @date 2020/7/10 21:41
 * @version 1.0
 */
fun Any?.generatePrivacyNickname(originNickname: String, defaultName: String): String {
    if (TextUtils.isEmpty(originNickname)) {
        return defaultName
    }
    return if (originNickname.length == 1) {
        "$originNickname*****"
    } else originNickname[0] + "*****" +
            originNickname[originNickname.length - 1]
}

/**
 * 首字母大写
 *
 * @return 成功返回true，失败返回false
 */
fun CharSequence?.firstLetterUpperCase(local: Locale): String {
    if (this.isEmptyOrBlank()) {
        return ""
    }
    if (length <= 1) {
        return this.toString().uppercase(local)
    }
    val firstLetter = substring(0, 1).uppercase(local)
    return firstLetter + substring(1)
}

/**
 * 首字母大写
 *
 * @return 成功返回true，失败返回false
 */
fun CharSequence?.firstLetterUpperCase(): String {
    return firstLetterUpperCase(Locale.ROOT)
}

/**
 * 根据sku截取商品spu
 * @author dingpeihua
 * @date 2021/2/1 15:37
 * @version 1.0
 */
fun CharSequence?.splitSpu(): CharSequence? {
    if (isNonEmpty() && length > 7) {
        return substring(0, 7)
    }
    return this
}

/**
 * 字符串拼接，
 * @param text 拼接字符串
 * @param separator 分隔符
 * @author dingpeihua
 * @date 2021/8/31 14:09
 * @version 1.0
 */
fun CharSequence?.splice(text: String?, separator: String): CharSequence {
    if (this.isNonEmpty()) {
        if (text.isNonEmpty()) {
            return "$this$separator$text"
        }
        return this
    }
    return ""
}

/**
 * 字符串拼接，以逗号分隔
 * @param text
 * @author dingpeihua
 * @date 2021/8/31 14:09
 * @version 1.0
 */
fun CharSequence?.splice(text: String?): CharSequence {
    return splice(text, ",")
}

/**
 * 删除最后一个字符
 * @author dingpeihua
 * @date 2022/1/12 15:00
 * @version 1.0
 */
fun StringBuilder.deleteEndChar(): StringBuilder {
    return deleteEndChar(",")
}

/**
 * 删除最后一个指定字符
 * @author dingpeihua
 * @date 2022/1/12 15:00
 * @version 1.0
 */
fun StringBuilder.deleteEndChar(endChar: String): StringBuilder {
    val index = lastIndexOf(endChar)
    if (length > 0 && index == length - 1) {
        deleteCharAt(index)
    }
    return this
}

inline fun <C : CharSequence> C?.ifNullOrEmpty(defaultValue: () -> C): C =
    if (isNullOrEmpty()) defaultValue() else this

@SinceKotlin("1.3")
inline fun <C : CharSequence> C?.ifNullOrBlank(defaultValue: () -> C): C =
    if (isNullOrBlank()) defaultValue() else this