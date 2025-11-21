package com.fz.common.permissions

import android.content.pm.PackageManager
import androidx.annotation.MainThread
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity

/**
 * A headless fragment which wraps the boilerplate code for checking and requesting permission.
 * A simple [Fragment] subclass.
 */
@Deprecated("Use androidx permissions < a herf='https://github.com/peihua8858/AndroidxPermissions.git'>github</a>")
class DslPermissionManager : BasePermissionManager() {

    override fun onPermissionResult(requestCode: Int, result: PermissionResult) {
        val callback = PermissionsMap.get(requestCode)
        when (result) {
            is PermissionResult.PermissionGranted -> {
                callback?.onGranted()
            }
            is PermissionResult.PermissionDenied -> {
                callback?.onDenied(result.deniedPermissions)
            }
            is PermissionResult.ShowRational -> {
                callback?.onShowRationale(PermissionRequestDsl(requireActivity(), result.rationalPermissions, requestCode))
            }
            is PermissionResult.PermissionDeniedPermanently -> {
                callback?.onNeverAskAgain(result.permanentlyDeniedPermissions)
            }
        }
    }

    override fun requestPermissions(requestId: Int, vararg permissions: String) {
        rationalRequest[requestId]?.let {
            requestPermissions(permissions, requestId)
            rationalRequest.remove(requestId)
            return
        }
        val notGranted = permissions.filter {
            ContextCompat.checkSelfPermission(
                    requireActivity(),
                    it
            ) != PackageManager.PERMISSION_GRANTED
        }.toTypedArray()
        when {
            notGranted.isEmpty() ->
                onPermissionResult(requestId, PermissionResult.PermissionGranted(requestId))
            else -> {
                requestPermissions(notGranted, requestId)
            }
        }
    }

    companion object {

        private const val TAG = "DslPermissionManager"

        /**
         * A static inline method to request permission for activity.
         *
         * @param activity an instance of [AppCompatActivity]
         * @param permissions vararg of all permissions for request
         * @param requestBlock [PermissionRequest] block for permission request
         *
         */
        @JvmStatic
        @MainThread
        inline fun requestPermissions(
                activity: FragmentActivity,
                vararg permissions: String,
                requestBlock: PermissionCallbacksDSL.() -> Unit,
        ) {
            val permissionCallbacks: PermissionCallbacks = PermissionCallbacksDSL().apply { requestBlock() }
            _requestPermissions(
                    activity,
                    permissionCallbacks,
                    *permissions
            )
        }

        /**
         * A static inline method to request permission for fragment.
         *
         * @param fragment an instance of [Fragment]
         * @param permissions vararg of all permissions for request
         * @param requestBlock [PermissionRequest] block for permission request
         *
         */
        @JvmStatic
        @MainThread
        inline fun requestPermissions(
                fragment: Fragment,
                vararg permissions: String,
                requestBlock: PermissionCallbacksDSL.() -> Unit,
        ) {
            val permissionCallbacks: PermissionCallbacks = PermissionCallbacksDSL().apply { requestBlock() }
            _requestPermissions(
                    fragment,
                    permissionCallbacks,
                    *permissions
            )
        }

        fun _requestPermissions(
                activityOrFragment: Any,
                callback: PermissionCallbacks,
                vararg permissions: String,
        ) {
            val fragmentManager = when (activityOrFragment) {
                is FragmentActivity -> {
                    activityOrFragment.supportFragmentManager
                }
                is Fragment -> {
                    activityOrFragment.childFragmentManager
                }
                else -> {
                    throw IllegalArgumentException("Not found activity or fragment.")
                }
            }
            val requestId = PermissionsMap.put(callback)
            if (fragmentManager.findFragmentByTag(TAG) != null) {
                (fragmentManager.findFragmentByTag(TAG) as DslPermissionManager)
                        .requestPermissions(requestId, *permissions)
            } else {
                val permissionManager = DslPermissionManager()
                fragmentManager.beginTransaction().add(permissionManager, TAG).commitNow()
                permissionManager.requestPermissions(requestId, *permissions)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}