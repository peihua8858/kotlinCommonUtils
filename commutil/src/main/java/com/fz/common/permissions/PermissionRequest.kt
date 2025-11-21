package com.fz.common.permissions

/**
 * Represents permission request encapsulating [requestCode] and
 * [resultCallback] for a permission request.
 */
@Deprecated("Use androidx permissions < a herf='https://github.com/peihua8858/AndroidxPermissions.git'>github</a>")
class PermissionRequest(
        var requestCode: Int? = null,
        var resultCallback: (PermissionResult.() -> Unit)? = null
)