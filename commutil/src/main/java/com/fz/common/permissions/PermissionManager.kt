package com.fz.common.permissions

import android.content.Context
import androidx.annotation.MainThread
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity

/**
 * A headless fragment which wraps the boilerplate code for checking and requesting permission.
 * A simple [Fragment] subclass.
 */
class PermissionManager : BasePermissionManager() {

    private var callbackMap = mutableMapOf<Int, PermissionResult.() -> Unit>()

    override fun onPermissionResult(requestCode: Int, permissionResult: PermissionResult) {
        callbackMap[permissionResult.requestCode]?.let {
            permissionResult.it()
        }
        callbackMap.remove(permissionResult.requestCode)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callbackMap.clear()
    }

    companion object {

        private const val TAG = "PermissionManager"

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
                requestBlock: PermissionRequest.() -> Unit,
        ) {
            val permissionRequest = PermissionRequest().apply(requestBlock)
            requireNotNull(permissionRequest.requestCode) {
                "No request code specified."
            }
            requireNotNull(permissionRequest.resultCallback) {
                "No result callback found."
            }
            _requestPermissions(
                    activity,
                    permissionRequest.requestCode!!,
                    permissionRequest.resultCallback!!,
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
                requestBlock: PermissionRequest.() -> Unit,
        ) {
            val permissionRequest = PermissionRequest().apply(requestBlock)
            requireNotNull(permissionRequest.requestCode) {
                "No request code specified."
            }
            requireNotNull(permissionRequest.resultCallback) {
                "No result callback found."
            }
            _requestPermissions(
                    fragment,
                    permissionRequest.requestCode!!,
                    permissionRequest.resultCallback!!,
                    *permissions
            )
        }

        fun _requestPermissions(
                activityOrFragment: Any,
                requestId: Int,
                callback: PermissionResult.() -> Unit,
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
            if (fragmentManager.findFragmentByTag(TAG) != null) {
                (fragmentManager.findFragmentByTag(TAG) as PermissionManager)
                        .also { it.callbackMap[requestId] = callback }
                        .requestPermissions(requestId, *permissions)
            } else {
                val permissionManager = PermissionManager()
                fragmentManager.beginTransaction().add(permissionManager, TAG).commitNow()
                permissionManager.callbackMap[requestId] = callback
                permissionManager.requestPermissions(requestId, *permissions)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        callbackMap.clear()
    }
}