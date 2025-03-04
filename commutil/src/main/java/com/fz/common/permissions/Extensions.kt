package com.fz.common.permissions

import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract

/**
 * @param permissions vararg of all the permissions for request.
 * @param requestBlock block constructing [PermissionRequest] object for permission request.
 */
@Deprecated("Use androidx permissions < a herf='https://github.com/peihua8858/AndroidxPermissions.git'>github</a>")
inline fun FragmentActivity.requestPermissions(
    vararg permissions: String,
    requestBlock: PermissionRequest.() -> Unit,
) {
    PermissionManager.requestPermissions(this, *permissions) { this.requestBlock() }
}

/**
 * @param permissions vararg of all the permissions for request.
 * @param requestBlock block constructing [PermissionRequest] object for permission request.
 */
@Deprecated("Use androidx permissions < a herf='https://github.com/peihua8858/AndroidxPermissions.git'>github</a>")
inline fun FragmentActivity.requestPermissionsDsl(
    vararg permissions: String,
    requestBlock: PermissionCallbacksDSL.() -> Unit,
) {
    DslPermissionManager.requestPermissions(this, *permissions) { requestBlock() }
}


/**
 * @param permissions vararg of all the permissions for request.
 * @param requestBlock block constructing [PermissionRequest] object for permission request.
 */
@Deprecated("Use androidx permissions < a herf='https://github.com/peihua8858/AndroidxPermissions.git'>github</a>")
inline fun Fragment.requestPermissions(
    vararg permissions: String,
    requestBlock: PermissionRequest.() -> Unit,
) {
    PermissionManager.requestPermissions(this, *permissions) { this.requestBlock() }
}

/**
 * @param permissions vararg of all the permissions for request.
 * @param requestBlock block constructing [PermissionRequest] object for permission request.
 */
@Deprecated("Use androidx permissions < a herf='https://github.com/peihua8858/AndroidxPermissions.git'>github</a>")
inline fun Fragment.requestPermissionsDsl(
    vararg permissions: String,
    requestBlock: PermissionCallbacksDSL.() -> Unit,
) {
    DslPermissionManager.requestPermissions(this, *permissions) { requestBlock() }
}

@OptIn(ExperimentalContracts::class)
fun Context.checkSelfPermissions(vararg permissions: String): Boolean {
    contract {
        returns()
    }
    val result = permissions.filter { !this.checkPermission(it) }
    return result.isEmpty()
}

@OptIn(ExperimentalContracts::class)
fun Context.checkPermission(permission: String): Boolean {
    contract {
        returns()
    }
    return ActivityCompat.checkSelfPermission(
        this, permission
    ) == PackageManager.PERMISSION_GRANTED
}
