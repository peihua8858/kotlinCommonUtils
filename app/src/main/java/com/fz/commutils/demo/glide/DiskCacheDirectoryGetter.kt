package com.fz.commutils.demo.glide

import android.content.Context
import com.bumptech.glide.load.engine.cache.DiskLruCacheFactory
import com.fz.common.utils.getDiskCacheDir
import java.io.File

/**
 * 获取glide图片缓存目录
 * @author dingpeihua
 * @date 2021/10/15 15:31
 * @version 1.0
 */
class DiskCacheDirectoryGetter(val context: Context) : DiskLruCacheFactory.CacheDirectoryGetter {
    override fun getCacheDirectory(): File {
        return context.getImageCacheFile()
    }

    companion object {
        /**
         * 获取图片缓存目录
         * @author dingpeihua
         * @date 2021/10/15 15:41
         * @version 1.0
         */
        @JvmStatic
        fun Context.getImageCacheFile(): File {
            val file = getDiskCacheDir()
            return File(File(file, "diskCache"), "imageCache")
        }
    }
}