package com.fz.common.permissions

import android.content.pm.PackageManager
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment

abstract class BasePermissionManager : Fragment() {

    protected val rationalRequest = mutableMapOf<Int, Boolean>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
    }

    override fun onRequestPermissionsResult(
            requestCode: Int,
            permissions: Array<out String>,
            grantResults: IntArray,
    ) {
        if (grantResults.isNotEmpty() &&
                grantResults.all { it == PackageManager.PERMISSION_GRANTED }
        ) {
            onPermissionResult(requestCode, PermissionResult.PermissionGranted(requestCode))
        } else if (permissions.any { shouldShowRequestPermissionRationale(it) }) {
            onPermissionResult(requestCode,
                    PermissionResult.PermissionDenied(requestCode,
                            permissions.filterIndexed { index, _ ->
                                grantResults[index] == PackageManager.PERMISSION_DENIED
                            }
                    )
            )
        } else {
            onPermissionResult(requestCode,
                    PermissionResult.PermissionDeniedPermanently(requestCode,
                            permissions.filterIndexed { index, _ ->
                                grantResults[index] == PackageManager.PERMISSION_DENIED
                            }
                    ))
        }
    }

    protected open fun requestPermissions(requestId: Int, vararg permissions: String) {
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
            notGranted.any { shouldShowRequestPermissionRationale(it) } -> {
                rationalRequest[requestId] = true
                onPermissionResult(requestId, PermissionResult.ShowRational(requestId, permissions.toList()))
            }
            else -> {
                requestPermissions(notGranted, requestId)
            }
        }
    }

    protected abstract fun onPermissionResult(requestCode: Int, permissionResult: PermissionResult)
}