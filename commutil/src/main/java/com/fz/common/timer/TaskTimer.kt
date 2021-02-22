package com.fz.common.timer

import android.os.Handler
import android.os.HandlerThread
import android.os.Message
import android.util.SparseArray
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent

/**
 * 计时器
 * @author dingpeihua
 * @date 2020/7/7 11:42
 * @version 1.0
 */
class TaskTimer(var period: Long) : LifecycleObserver {
    private var mExecute = true
    private var handleThread: HandlerThread = HandlerThread("TaskTimer")
    private var mHandler: Handler? = null
    private var mStarted: Boolean = false
    private val mRunnables: SparseArray<Runnable> = SparseArray<Runnable>()
    private var index = 0;

    constructor() : this(0)

    fun onCancel() {
        mRunnables.clear()
        removeAllMessages()
        mExecute = false
    }

    fun isCancelled(): Boolean {
        return mExecute
    }

    fun onPause() {
        removeAllMessages()
        mExecute = false
    }

    fun isStarted(): Boolean {
        return mStarted
    }

    fun onResume() {
        if (mExecute || mRunnables.size() == 0) {
            return
        }
        mExecute = true
        val tempRunnables = mRunnables.clone()
        mRunnables.clear()
        val size: Int = tempRunnables.size()
        for (index: Int in 0..size) {
            val runnable = tempRunnables.get(index)
            if (runnable is TimerTask) {
                schedule(runnable, runnable.period)
            } else if (runnable != null) {
                schedule(runnable)
            }
        }
    }

    fun onStart() {
        mExecute = true
        if (isStarted()) {
            return
        }
        mStarted = true
        handleThread = HandlerThread("TaskTimer")
        handleThread.start()
        mHandler = object : Handler(handleThread.looper) {
            override fun dispatchMessage(msg: Message) {
                if (!mExecute) {
                    return
                }
                val callback = msg.callback
                if (callback != null) {
                    callback.run()
                    if (callback is TimerTask) {
                        if (callback.state == TimerTask.CANCELLED) {
                            return
                        } else if (callback.period == 0L) {
                            callback.state = TimerTask.EXECUTED
                            return
                        }
                        postDelayed(callback, callback.period)
                    } else if (period > 0) {
                        postDelayed(callback, period)
                    }
                }
            }
        }
    }

    /**
     * 停止计时
     * @author dingpeihua
     * @date 2020/7/9 15:59
     * @version 1.0
     */
    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onStop() {
        mRunnables.clear()
        mHandler?.removeCallbacksAndMessages(null)
        handleThread.quit()
        try {
            handleThread.interrupt()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        mExecute = false
        mStarted = false
    }

    fun schedule(task: Runnable) {
        schedule(task, 0)
    }

    fun schedule(task: Runnable, delay: Long) {
        schedule(task, delay, period)
    }

    fun schedule(task: Runnable, delay: Long, period: Long) {
        this.period = period
        if (task is TimerTask) {
            task.state = TimerTask.SCHEDULED
            task.period = period
        }
        if (!isStarted()) {
            onStart()
        }
        mHandler?.postDelayed(task, delay)
        mRunnables.put(index, task)
    }

    fun remove(runnable: Runnable) {
        mHandler?.removeCallbacks(runnable)
    }

    fun removeAllMessages() {
        mHandler?.removeCallbacksAndMessages(null)
    }
}