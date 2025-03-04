package com.fz.common.permissions

import java.util.concurrent.atomic.AtomicInteger

/**
 * Used for generating request code and hold permission callbacks on a map.
 */
@Deprecated("Use androidx permissions < a herf='https://github.com/peihua8858/AndroidxPermissions.git'>github</a>")
internal object PermissionsMap {

    private val atomicInteger = AtomicInteger(100)

    private val map = mutableMapOf<Int, PermissionCallbacks>()

    fun put(callbacks: PermissionCallbacks): Int {
        return atomicInteger.getAndIncrement().also {
            map[it] = callbacks
        }
    }

    fun get(requestCode: Int): PermissionCallbacks? {
        return map[requestCode].also {
            map.remove(requestCode)
        }
    }

}