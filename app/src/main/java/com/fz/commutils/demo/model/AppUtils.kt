package com.fz.commutils.demo.model

import android.Manifest
import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import androidx.annotation.RequiresPermission
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.fz.dialog.LoadingDialogFragment

fun FragmentActivity.dismissProgressDialog() {
    this.processDialog.dismissAllowingStateLoss()
}

fun FragmentActivity.showProcessDialog() {
    this.processDialog.show(this.supportFragmentManager, "Loading")
}

fun Fragment.dismissProgressDialog() {
    this.processDialog.show(this.requireFragmentManager(), "Loading")
}

fun Fragment.showProcessDialog() {
    this.processDialog.dismissAllowingStateLoss()
}

val FragmentActivity.processDialog: LoadingDialogFragment
    get() {
        return LoadingDialogFragment()
    }
val Fragment.processDialog: LoadingDialogFragment
    get() = LoadingDialogFragment()

object AppUtils {
    @JvmStatic
    fun startCameraActivity(activity: Activity?, requestCode: Int): Uri? {
        if (activity == null) {
            return null
        }
        val mUri = activity.contentResolver.insert(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            ContentValues()
        )
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.putExtra(MediaStore.EXTRA_OUTPUT, mUri)
        activity.startActivityForResult(intent, requestCode)
        return mUri
    }
}
