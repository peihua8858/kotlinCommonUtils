@file:JvmName("AnimatorUtil")
@file:JvmMultifileClass

package com.fz.common.utils

import android.app.Activity
import android.app.ActivityOptions
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.view.animation.LinearInterpolator
import android.view.animation.RotateAnimation
import androidx.annotation.StringRes
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityOptionsCompat
import androidx.core.view.ViewCompat
import androidx.core.view.ViewPropertyAnimatorListener
import androidx.fragment.app.Fragment
import androidx.interpolator.view.animation.LinearOutSlowInInterpolator
import com.fz.common.text.isNonEmpty

/**
 * 显示view
 *
 * @param viewPropertyAnimatorListener
 */
fun View?.scaleShow(viewPropertyAnimatorListener: ViewPropertyAnimatorListener?) {
    if (this == null) {
        return
    }
    this.visibility = View.VISIBLE
    ViewCompat.animate(this).scaleX(1.0f).scaleY(1.0f).alpha(1.0f)
            .setDuration(800).setListener(viewPropertyAnimatorListener).setInterpolator(
                    LinearOutSlowInInterpolator()
            )
            .start()
}

/**
 * 隐藏view
 *
 * @param viewPropertyAnimatorListener
 */
fun View?.scaleHide(viewPropertyAnimatorListener: ViewPropertyAnimatorListener?) {
    if (this == null) {
        return
    }
    ViewCompat.animate(this).scaleX(0.0f).scaleY(0.0f).alpha(0.0f)
            .setDuration(800).setInterpolator(LinearOutSlowInInterpolator())
            .setListener(viewPropertyAnimatorListener)
            .start()
}

/**
 * 共享元素
 *
 * @param intent
 * @param shareView
 * @param transitionNameRes
 */
fun Activity?.startActivityByShareElement2(intent: Intent, shareView: View?, @StringRes transitionNameRes: Int) {
    if (this != null) {
        if (transitionNameRes != 0) {
            startActivityByShareElement2(intent, shareView, getString(transitionNameRes))
        } else {
            startActivity(intent)
        }
    }
}

/**
 * 共享元素
 *
 * @param intent
 * @param shareView
 * @param transitionName
 */
fun Activity?.startActivityByShareElement2(intent: Intent, shareView: View?, transitionName: String) {
    if (this != null) {
        if (shareView != null && transitionName.isNonEmpty()) {
            shareView.transitionName = transitionName
            val options = ActivityOptionsCompat.makeSceneTransitionAnimation(this,
                    shareView, transitionName).toBundle()
            if (options != null && !isFinishing) {
                startActivity(intent, options)
                return
            }
        }
        startActivity(intent)
    }
}

fun Context?.startActivityByShareElement(bundle: Bundle?, shareView: View?, shareResource: Int, target: Class<*>?) {
    if (this is Activity) {
        startActivityByShareElement(bundle, shareView, shareResource, target)
    } else {
        val intent = Intent(this, target)
        if (bundle != null) {
            intent.putExtras(bundle)
        }
        this?.startActivity(intent)
    }
}

fun Activity.startActivityByShareElement(bundle: Bundle?, shareView: View?, shareResource: Int, target: Class<*>?) {
    val intent = Intent(this, target)
    if (bundle != null) {
        intent.putExtras(bundle)
    }
    if (shareView != null) {
        val options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                this, shareView,
                getString(shareResource)
        ).toBundle()
        startActivity(intent, options)
    } else {
        startActivity(intent)
    }
}

/**
 * 通过共享元素启动过渡
 */
fun Activity.startActivityByShareElement(shareView: View?, shareResource: Int, target: Class<*>?) {
    startActivityByShareElement(null, shareView, shareResource, target)
}

/**
 * 共享元素动画
 *
 * @param context
 * @param intent
 * @param shareView
 * @param shareResource
 */
fun Context?.startActivityByShareElement(intent: Intent, shareView: View?, @StringRes shareResource: Int) {
    this?.let {
        if (this is Activity) {
            startActivityByShareElement2(intent, shareView, shareResource)
        } else {
            it.startActivity(intent)
        }
    }
}

/**
 * 共享元素动画
 *
 * @param intent
 * @param shareView
 */
fun Context?.startActivityByShareElement(intent: Intent, shareView: View? = null) {
    if (this != null) {
        if (shareView != null) {
            val compat = ActivityOptionsCompat.makeScaleUpAnimation(shareView,
                    shareView.width / 2, shareView.height / 2, 0, 0)
            ActivityCompat.startActivity(this, intent, compat.toBundle())
        } else {
            startActivity(intent)
        }
    }
}

/**
 * 共享元素动画
 *
 * @param intent
 * @param shareView
 */
fun Fragment?.startActivityByShareElement(intent: Intent, shareView: View? = null) {
    if (this != null) {
        val context = context
        if (shareView != null && context != null) {
            val compat = ActivityOptionsCompat.makeScaleUpAnimation(shareView,
                    shareView.width / 2, shareView.height / 2, 0, 0)
            ActivityCompat.startActivity(context, intent, compat.toBundle())
        } else {
            startActivity(intent)
        }
    }
}

fun Activity.startActivityForResultByShareElement(
        shareView: View?,
        @StringRes shareResource: Int,
        target: Class<*>?,
        requestCode: Int
) {
    val intent = Intent(this, target)
    if (shareResource != 0) {
        val options = ActivityOptions.makeSceneTransitionAnimation(
                this, shareView,
                getString(shareResource)
        ).toBundle()
        startActivityForResult(intent, requestCode, options)
    } else {
        startActivityForResult(intent, requestCode)
    }
}

fun Activity.startActivityForResultByShareElement(
        intent: Intent,
        shareView: View?,
        @StringRes shareResource: Int,
        requestCode: Int
) {
    if (shareResource != 0) {
        val options = ActivityOptions.makeSceneTransitionAnimation(
                this, shareView,
                getString(shareResource)
        ).toBundle()
        startActivityForResult(intent, requestCode, options)
    } else {
        startActivityForResult(intent, requestCode)
    }
}

fun Fragment.startActivityForResultByShareElement(intent: Intent, shareView: View?,
                                                  @StringRes shareResource: Int, requestCode: Int
) {
    val activity = activity
    if (shareResource != 0 && activity != null) {
        val options = ActivityOptions.makeSceneTransitionAnimation(
                activity, shareView,
                getString(shareResource)
        ).toBundle()
        startActivityForResult(intent, requestCode, options)
    } else {
        startActivityForResult(intent, requestCode)
    }
}

/**
 * 通过共享元素关闭界面
 */
fun Activity.finishByElement() {
    try {
        finishAfterTransition()
    } catch (e: Exception) {
        finish()
    }
}

/**
 * 根据当前的状态来旋转箭头。
 *
 * @param
 * @author dingpeihua
 * @date 2019/10/31 15:29
 * @version 1.0
 */
fun View.rotateArrow(flag: Boolean) {
    // flag为true则向上
    val fromDegrees = if (flag) 180f else 0f
    val toDegrees = if (flag) 360f else 180f
    //旋转动画效果   参数值 旋转的开始角度  旋转的结束角度  pivotX x轴伸缩值
    val animation = RotateAnimation(
            fromDegrees, toDegrees, Animation.RELATIVE_TO_SELF,
            0.5f, Animation.RELATIVE_TO_SELF, 0.5f
    )
    val lin = LinearInterpolator()
    animation.interpolator = lin
    //设置重复次数
    animation.repeatCount = 0
    //该方法用于设置动画的持续时间，以毫秒为单位
    animation.duration = 300
    //动画终止时停留在最后一帧
    animation.fillAfter = true
    //执行前的等待时间
    animation.startOffset = 10
    //启动动画
    startAnimation(animation)
}