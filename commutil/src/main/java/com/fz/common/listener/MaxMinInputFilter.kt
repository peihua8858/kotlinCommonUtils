package com.fz.common.listener

import android.text.InputFilter
import android.text.Spanned
import java.util.regex.Pattern

/**
 * 限制TextView 输入从最小值[minNum]到最大值[maxNum]，并允许保留指定位数[numOfDecimals]的小数
 * @author dingpeihua
 * @date 2022/8/11 9:45
 * @version 1.0
 */
class MaxMinInputFilter(private val minNum: Double, private val maxNum: Double, private val numOfDecimals: Int = 2) :
    InputFilter {
    private val pattern: Pattern = Pattern.compile(
        "^" + (if (minNum < 0) "-?" else "") + "[0-9]*\\.?[0-9]" + if (numOfDecimals > 0) "{0,$numOfDecimals}$" else "*"
    )

    override fun filter(
        source: CharSequence,
        start: Int,
        end: Int,
        dest: Spanned,
        dstart: Int,
        dend: Int
    ): CharSequence {
        if ("." == source) {
            if (dstart == 0 || dest[dstart - 1] !in '0'..'9' || dest[0] == '0') {
                return ""
            }
        }
        if ("0" == source && dest.toString().contains(".") && dstart == 0) {
            return ""
        }
        val builder = StringBuilder(dest)
        builder.delete(dstart, dend)
        builder.insert(dstart, source)
        if (!pattern.matcher(builder.toString()).matches()) {
            return ""
        }
        if (builder.isNotEmpty()) {
            val num = builder.toString().toDouble()
            if (num > maxNum) {
                return ""
            }
        }
        return source
    }
}