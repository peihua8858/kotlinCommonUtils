package com.fz.common.permissions

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment

/**
 * @param permissions vararg of all the permissions for request.
 * @param requestBlock block constructing [PermissionRequest] object for permission request.
 */
inline fun AppCompatActivity.requestPermissions(
        vararg permissions: String,
        requestBlock: PermissionRequest.() -> Unit,
) {
    PermissionManager.requestPermissions(this, *permissions) { this.requestBlock() }
}

/**
 * @param permissions vararg of all the permissions for request.
 * @param requestBlock block constructing [PermissionRequest] object for permission request.
 */
inline fun AppCompatActivity.requestPermissionsDsl(
        vararg permissions: String,
        requestBlock: PermissionCallbacksDSL.() -> Unit,
) {
    DslPermissionManager.requestPermissions(this, *permissions) { requestBlock() }
}


/**
 * @param permissions vararg of all the permissions for request.
 * @param requestBlock block constructing [PermissionRequest] object for permission request.
 */
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
inline fun Fragment.requestPermissionsDsl(
        vararg permissions: String,
        requestBlock: PermissionCallbacksDSL.() -> Unit,
) {
    DslPermissionManager.requestPermissions(this, *permissions) { requestBlock() }
}
