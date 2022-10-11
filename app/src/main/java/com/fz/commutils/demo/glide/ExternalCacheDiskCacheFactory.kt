package com.fz.commutils.demo.glide

import android.content.Context
import com.bumptech.glide.load.engine.cache.DiskCache
import com.bumptech.glide.load.engine.cache.DiskLruCacheFactory

/**
 * glide图片保存至SD卡
 *
 * @author longxl
 * @version 1.0
 * @email 343827585@qq.com
 * @date 2016/7/15
 * @since 1.0
 */
class ExternalCacheDiskCacheFactory @JvmOverloads constructor(context: Context, diskCacheSize: Int = DiskCache.Factory.DEFAULT_DISK_CACHE_SIZE)
    : DiskLruCacheFactory(DiskCacheDirectoryGetter(context), diskCacheSize.toLong()) {
}