package com.fz.common.permissions

/**
 * Represents permission request encapsulating [requestCode] and
 * [resultCallback] for a permission request.
 */
class PermissionRequest(
        var requestCode: Int? = null,
        var resultCallback: (PermissionResult.() -> Unit)? = null
)