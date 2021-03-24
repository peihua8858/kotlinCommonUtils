package com.fz.common.permissions

import android.app.Activity
import androidx.core.app.ActivityCompat
import java.lang.ref.WeakReference

sealed class PermissionResult(val requestCode: Int) {
    class PermissionGranted(requestCode: Int) : PermissionResult(requestCode)
    class PermissionDenied(
            requestCode: Int,
            val deniedPermissions: List<String>
    ) : PermissionResult(requestCode)

    class ShowRational(requestCode: Int,val rationalPermissions: List<String>) : PermissionResult(requestCode)
    class PermissionDeniedPermanently(
            requestCode: Int,
            val permanentlyDeniedPermissions: List<String>
    ) : PermissionResult(requestCode)
}

/**
 * Represents permission request to retry after rationale message is shown.
 */
class PermissionRequestDsl internal constructor(
        activity: Activity,
        val permissions: List<String>,
        val requestCode: Int
) {

    private val weakActivity: WeakReference<Activity> = WeakReference(activity)

    /**
     * Invoke this after rationale message is shown.
     */
    fun retry() {
        val activity = weakActivity.get()
        activity?.let { ActivityCompat.requestPermissions(it, permissions.toTypedArray(), requestCode) }
    }
}