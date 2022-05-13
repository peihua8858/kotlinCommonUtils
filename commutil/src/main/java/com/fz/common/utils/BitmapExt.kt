package com.fz.common.utils

import android.content.Context
import android.graphics.Bitmap
import android.renderscript.Allocation
import android.renderscript.Element
import android.renderscript.RenderScript
import android.renderscript.ScriptIntrinsicBlur
/**
 * Bitmap高斯模糊
 * @param   context 当前上下文
 * @param   bitmap 要模糊的图片
 * @param   radius 模糊半径
 * @param   sale 图片缩放比例
 * @author dingpeihua
 * @date 2022/5/13 16:54
 * @version 1.0
 */
fun Bitmap.blur(context: Context, radius:Float, sale:Float): Bitmap {
    //创建一个缩小后的bitmap
    val inputBitmap = Bitmap.createScaledBitmap(this, (width/sale).toInt(),
        (height/sale).toInt(), false)
    //创建将在ondraw中使用到的经过模糊处理后的bitmap
    val outputBitmap = Bitmap.createBitmap(inputBitmap)

    //创建RenderScript，ScriptIntrinsicBlur固定写法
    val rs: RenderScript = RenderScript.create(context)
    val blurScript = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs))

    //根据inputBitmap，outputBitmap分别分配内存
    val tmpIn = Allocation.createFromBitmap(rs, inputBitmap)
    val tmpOut = Allocation.createFromBitmap(rs, outputBitmap)

    //设置模糊半径取值0-25之间，不同半径得到的模糊效果不同
    blurScript.setRadius(radius)
    blurScript.setInput(tmpIn)
    blurScript.forEach(tmpOut)

    //得到最终的模糊bitmap
    tmpOut.copyTo(outputBitmap)
    return outputBitmap
}