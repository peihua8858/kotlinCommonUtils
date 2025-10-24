package com.fz.common.utils

import android.database.Cursor

fun Cursor.getString(name: String): String {
    val index = getColumnIndex(name)
    return getString(index) ?: ""
}

fun Cursor.getLong(name: String): Long {
    val index = getColumnIndex(name)
    return getLong(index)
}

fun Cursor.getInt(name: String): Int {
    val index = getColumnIndex(name)
    return getInt(index)
}

fun Cursor.getFloat(name: String): Float {
    val index = getColumnIndex(name)
    return getFloat(index)
}

fun Cursor.getDouble(name: String): Double {
    val index = getColumnIndex(name)
    return getDouble(index)
}

fun Cursor.getShort(name: String): Short {
    val index = getColumnIndex(name)
    return getShort(index)
}

fun Cursor.getBlob(name: String): ByteArray? {
    val index = getColumnIndex(name)
    return getBlob(index)
}
