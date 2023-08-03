package com.fz.common.utils

import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.os.Parcelable
import android.view.View
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContract
import androidx.annotation.StringRes
import androidx.core.app.ActivityOptionsCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import java.util.ArrayList

fun <T> Intent?.getParcelableExtraCompat(name: String?, clazz: Class<T>): T? {
    if (this == null) return null
    if (isTiramisu) {
        return getParcelableExtra(name, clazz)
    } else {
        return getParcelableExtra(name)
    }
}

fun <T : Parcelable> Intent?.getParcelableArrayListExtraCompat(name: String?, clazz: Class<T>): ArrayList<T> {
    if (this == null) return arrayListOf()
    if (isTiramisu) {
        return getParcelableArrayListExtra(name, clazz) ?: arrayListOf()
    } else {
        return getParcelableArrayListExtra(name) ?: arrayListOf()
    }
}

fun <T> Bundle?.getParcelableCompat(name: String?, clazz: Class<T>): T? {
    if (this == null) return null
    if (isTiramisu) {
        return getParcelable(name, clazz)
    } else {
        return getParcelable(name)
    }
}

fun <T : Parcelable> Bundle?.getParcelableArrayListCompat(name: String?, clazz: Class<T>): ArrayList<T> {
    if (this == null) return arrayListOf()
    if (isTiramisu) {
        return getParcelableArrayList(name, clazz) ?: arrayListOf()
    } else {
        return getParcelableArrayList(name) ?: arrayListOf()
    }
}


fun Context.registerReceiverExported(receiver: BroadcastReceiver, intent: IntentFilter) {
    if (isTiramisu) {
        registerReceiver(receiver, intent, Context.RECEIVER_EXPORTED)
    } else registerReceiver(receiver, intent)
}

fun Context.registerReceiverNotExported(receiver: BroadcastReceiver, intent: IntentFilter) {
    if (isTiramisu) {
        registerReceiver(receiver, intent, Context.RECEIVER_NOT_EXPORTED)
    } else registerReceiver(receiver, intent)
}


fun <T : FragmentActivity> FragmentActivity.registerForActivityResult(
    clazz: Class<T>,
    callback: (ActivityResult) -> Unit
): ActivityResultLauncher<Void?> {
    return ActivityResultLauncher(registerForActivityResult(object :
        ActivityResultContract<Void?, ActivityResult>() {

        override fun createIntent(context: Context, input: Void?): Intent = Intent(context, clazz)

        override fun parseResult(resultCode: Int, intent: Intent?): ActivityResult =
            ActivityResult(resultCode, intent)
    }, callback))
}

fun <T : FragmentActivity, I> FragmentActivity.registerForActivityResult(
    createIntent: (context: Context, input: I) -> Intent,
    callback: (ActivityResult) -> Unit
): ActivityResultLauncher<I> {
    return ActivityResultLauncher(registerForActivityResult(object :
        ActivityResultContract<I, ActivityResult>() {

        override fun createIntent(context: Context, input: I): Intent = createIntent(context, input)

        override fun parseResult(resultCode: Int, intent: Intent?): ActivityResult =
            ActivityResult(resultCode, intent)
    }, callback))
}

val intentResultContract = object : ActivityResultContract<Intent, ActivityResult>() {

    override fun createIntent(context: Context, input: Intent): Intent = input

    override fun parseResult(
        resultCode: Int,
        intent: Intent?
    ): ActivityResult = ActivityResult(resultCode, intent)
}

fun FragmentActivity.registerForActivityResult(callback: (ActivityResult) -> Unit): ActivityResultLauncher<Intent> {
    return ActivityResultLauncher(registerForActivityResult(intentResultContract, callback))
}

fun <T : FragmentActivity> Fragment.registerForActivityResult(
    clazz: Class<T>,
    callback: (ActivityResult) -> Unit
): ActivityResultLauncher<Void?> {
    return ActivityResultLauncher(registerForActivityResult(object :
        ActivityResultContract<Void?, ActivityResult>() {

        override fun createIntent(context: Context, input: Void?): Intent = Intent(context, clazz)

        override fun parseResult(resultCode: Int, intent: Intent?): ActivityResult =
            ActivityResult(resultCode, intent)
    }, callback))
}

fun <T : FragmentActivity, I> Fragment.registerForActivityResult(
    createIntent: (context: Context, input: I) -> Intent,
    callback: (ActivityResult) -> Unit
): ActivityResultLauncher<I> {
    return ActivityResultLauncher(registerForActivityResult(object :
        ActivityResultContract<I, ActivityResult>() {

        override fun createIntent(context: Context, input: I): Intent = createIntent(context, input)

        override fun parseResult(resultCode: Int, intent: Intent?): ActivityResult =
            ActivityResult(resultCode, intent)
    }, callback))
}

fun Fragment.registerForActivityResult(callback: (ActivityResult) -> Unit): ActivityResultLauncher<Intent> {
    return ActivityResultLauncher(registerForActivityResult(intentResultContract, callback))
}


class ActivityResultLauncher<I>(private val resultLauncher: androidx.activity.result.ActivityResultLauncher<I>) :
    androidx.activity.result.ActivityResultLauncher<I>() {
    override fun launch(input: I, options: ActivityOptionsCompat?) {
        return resultLauncher.launch(input, options)
    }

    override fun unregister() {
        return resultLauncher.unregister()
    }

    override fun getContract(): ActivityResultContract<I, *> {
        return resultLauncher.contract
    }

    fun launch() {
        launch(null)
    }

    /**
     * 共享元素动画
     * @author dingpeihua
     * @date 2023/7/6 16:41
     * @version 1.0
     */
    fun launchByShareElement(
        input: I, activity: Activity? = null, shareView: View? = null, @StringRes shareResource: Int = 0
    ) {
        if (shareResource != 0 && shareView != null && activity != null) {
            val options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                activity, shareView,
                activity.getString(shareResource)
            )
            launch(input, options)
        } else {
            launch(input)
        }
    }
}