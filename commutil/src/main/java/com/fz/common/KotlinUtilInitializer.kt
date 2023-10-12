package com.fz.common

import android.annotation.SuppressLint
import android.content.Context
import androidx.startup.Initializer

/**
 * 工具初始化器
 * @author dingpeihua
 * @date 2023/10/12 15:22
 * @version 1.0
 */
class KotlinUtilInitializer : Initializer<Unit> {

    override fun create(context: Context) {
        ContextInitializer.mContext = context
    }

    override fun dependencies() = emptyList<Class<Initializer<*>>>()
}

@SuppressLint("StaticFieldLeak")
object ContextInitializer {
    internal lateinit var mContext: Context
    fun setContext(context: Context) {
        mContext = context
    }
}