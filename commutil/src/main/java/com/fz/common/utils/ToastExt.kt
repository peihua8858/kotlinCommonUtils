package com.fz.common.utils

import android.content.Context
import android.view.View
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment

fun Context.showToast(content: String) {
    Toast.makeText(this, content, Toast.LENGTH_SHORT).show()
}
fun Context.showToast(@StringRes ids: Int) {
    Toast.makeText(this, ids, Toast.LENGTH_SHORT).show()
}
fun Fragment.showToast(content: String) {
    Toast.makeText(requireContext(), content, Toast.LENGTH_SHORT).show()
}


fun Fragment.showToast(@StringRes ids: Int) {
    Toast.makeText(requireContext(), ids, Toast.LENGTH_SHORT).show()
}

fun View.showToast(content: String) {
    Toast.makeText(context, content, Toast.LENGTH_SHORT).show()
}
fun View.showToast(@StringRes ids: Int) {
    Toast.makeText(context, ids, Toast.LENGTH_SHORT).show()
}