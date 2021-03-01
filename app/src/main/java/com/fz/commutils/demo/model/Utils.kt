package com.fz.commutils.demo.model

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