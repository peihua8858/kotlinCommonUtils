package com.fz.common.utils

import android.os.Build


/**
 * [Build.VERSION.SDK_INT]>= Android 13
 */
inline val isTiramisu: Boolean
    get() = Build.VERSION.SDK_INT >= 33

/**
 * [Build.VERSION.SDK_INT]>= Android 9
 */
inline val isAtLeastO: Boolean
    get() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.O

inline val isAtLeastS: Boolean
    get() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.S
inline val isAtLeastSv2: Boolean
    get() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.S_V2
inline val isAtLeastT: Boolean
    get() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU

/**
 * [Build.VERSION.SDK_INT]>= Android 9
 */
inline val isAtLeastP: Boolean
    get() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.P
inline val isUpsideDownCake: Boolean
    get() = Build.VERSION.SDK_INT >= 34
inline val isAtLeastQ: Boolean
    get() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q

/**
 * [Build.VERSION.SDK_INT]>= Android 11
 */
inline val isAtLeastR: Boolean
    get() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.R
inline val isAtLeastM: Boolean
    get() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
inline val isAtLeastN: Boolean
    get() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.N