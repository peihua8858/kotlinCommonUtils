@file:JvmName("DoubleFormat")
package com.fz.common.format

import com.socks.library.KLog
import java.math.BigDecimal
import java.math.RoundingMode
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.*
import kotlin.math.pow

/**
 * 精度控制 十万分之一
 *
 * @param digit
 * @param mode
 * @return
 */
fun BigDecimal.format(digit: Int, mode: RoundingMode): String {
    return this.let {
        when (mode) {
            RoundingMode.FLOOR -> setScale(digit, BigDecimal.ROUND_FLOOR)
            else -> setScale(digit, BigDecimal.ROUND_UP)
        }
    }.toString()
}

/**
 * 对应个十百千万位向上向下取整
 * orientation:2向下取整,1向上取整,0不变-向上向下取整 （旧字段）
 * digit:取整位数,0个位1十位2百位3千位4万为5十万位6百万位（新字段）
 *
 * @param digit       取整位数
 * @param orientation 向上向下取整
 * @return 取整位数后价格
 */
fun Double.formatCodRound(orientation: Int, digit: Int): Double {
    return this.let {
        KLog.d("COD DISCOUNT取整位数前>>>>$this")
        var bd = BigDecimal(it.toString())
        //$23239， 选择百位向上取整则为 $23300，百位向下取整则为$23200
        //2 3398.2 上3400 下3300-----3398
        val powString = 10.0.pow(digit.toDouble()).toString()
        if (orientation == 2) {
            //向下取整
            bd = bd.divide(BigDecimal(powString), 0, BigDecimal.ROUND_FLOOR)
            bd = bd.multiply(BigDecimal(powString))
        } else if (orientation == 1) {
            //向上取整
            bd = bd.divide(BigDecimal(powString), 0, BigDecimal.ROUND_CEILING)
            bd = bd.multiply(BigDecimal(powString))
        }
        KLog.d("COD DISCOUNT取整位数后>>>>" + bd.toDouble())
        bd.toDouble()
    }
}