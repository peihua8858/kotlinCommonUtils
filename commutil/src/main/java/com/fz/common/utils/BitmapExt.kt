package com.fz.common.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.graphics.Matrix
import android.graphics.Paint
import android.renderscript.Allocation
import android.renderscript.Element
import android.renderscript.RenderScript
import android.renderscript.ScriptIntrinsicBlur
import androidx.core.graphics.createBitmap
import androidx.exifinterface.media.ExifInterface
import java.io.File
import java.io.FileInputStream
import kotlin.math.max

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


fun Bitmap.adjustBitmapOrientation(filePath: String): Bitmap? {
    var exifInterface: ExifInterface? = null
    try {
        exifInterface = ExifInterface(filePath)
    } catch (e: Throwable) {
        e.printStackTrace()
    }
    var rotation = 0
    if (exifInterface != null) {
        val orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, 0)
        when (orientation) {
            ExifInterface.ORIENTATION_ROTATE_90 -> rotation = 90
            ExifInterface.ORIENTATION_ROTATE_180 -> rotation = 180
            ExifInterface.ORIENTATION_ROTATE_270 -> rotation = 270
            else -> {}
        }
    }
    dLog { "adjustBitmapOrientation, adjust degree " + rotation + "to 0." }
    if (rotation == 0) {
        return this
    }
    val matrix = Matrix()
    matrix.postRotate(rotation.toFloat())
    return Bitmap.createBitmap(
        this,
        0,
        0,
        getWidth(),
        getHeight(),
        matrix,
        true
    )
}


fun String.decodePathOptionsFile(screenWidth: Int, screenHeight: Int): Bitmap? {
    try {
        val mScreenWidth = screenWidth
        val mScreenHeight = screenHeight
        val file = File(this)
        val o = BitmapFactory.Options()
        o.inJustDecodeBounds = true
        BitmapFactory.decodeStream(FileInputStream(file), null, o)
        val width_tmp = o.outWidth
        val height_tmp = o.outHeight
        var scale = 1
        if (width_tmp <= mScreenWidth && height_tmp <= mScreenHeight) {
            scale = 1
        } else {
            val widthFit: Double = width_tmp * 1.0 / mScreenWidth
            val heightFit: Double = height_tmp * 1.0 / mScreenHeight
            val fit = max(widthFit, heightFit)
            scale = (fit + 0.5).toInt()
        }
        var bitmap: Bitmap? = null
        if (scale == 1) {
            bitmap = BitmapFactory.decodeStream(FileInputStream(file))
        } else {
            val o2 = BitmapFactory.Options()
            o2.inSampleSize = scale
            bitmap = BitmapFactory.decodeStream(FileInputStream(file), null, o2)
        }
        if (bitmap != null) {
            eLog { "scale = " + scale + " bitmap.size = " + (bitmap.getRowBytes() * bitmap.getHeight()) }
        }
        return bitmap
    } catch (e: Throwable) {
        eLog { "fileNotFoundException, e: $e" }
    }
    return null
}

fun Bitmap.toBlackAndWhite(): Bitmap {
    val bmpMonochrome = createBitmap(width, height)
    val canvas = Canvas(bmpMonochrome)
    val ma = ColorMatrix()
    ma.setSaturation(0f)
    val paint = Paint()
    paint.setColorFilter(ColorMatrixColorFilter(ma))
    canvas.drawBitmap(this, 0f, 0f, paint)
    return bmpMonochrome
}
