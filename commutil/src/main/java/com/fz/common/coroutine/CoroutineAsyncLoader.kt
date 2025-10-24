package com.fz.common.coroutine

import android.content.Context
import android.os.Looper
import android.os.SystemClock
import android.util.Log
import androidx.core.os.OperationCanceledException
import androidx.loader.content.Loader
import com.fz.common.utils.dLog
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.concurrent.atomic.AtomicBoolean

/**
 * 基于kotlin 协程实现的异步加载器
 * @param <T>
 * @author dingpeihua
 * @date 2025/8/14 15:57
 **/
abstract class CoroutineAsyncLoader<T>(context: Context) : Loader<T>(context) {
    companion object {
        const val DEBUG: Boolean = false
        const val TAG: String = "CoroutineAsyncLoader"
    }

    private var mResult: T? = null

    @Volatile
    private var mTask: LoadTask<T>? = null

    @Volatile
    private var mCancellingTask: LoadTask<T>? = null
    private var mLastLoadCompleteTime: Long = -10000

    class LoadTask<T>(private val loader: CoroutineAsyncLoader<T>) :
        ModernAsyncTask<Void, Void, T>(), Runnable {
        override suspend fun doInBackground(vararg params: Void): T? {
            if (DEBUG) Log.v(TAG, "$this >>> doInBackground")
            try {
                val data: T? = loader.onLoadInBackground()
                if (DEBUG) Log.v(TAG, "$this  <<< doInBackground")
                return data
            } catch (ex: Throwable) {
                if (!isCancelled()) {
                    throw ex
                }
                if (DEBUG) Log.v(TAG, "$this  <<< doInBackground (was canceled)", ex)
                return null
            }
        }

        override fun onPostExecute(result: T?) {
            if (DEBUG) Log.v(TAG, "$this onPostExecute")
            loader.dispatchOnLoadComplete(this, result)
        }

        override fun onCancelled(result: T?) {
            if (DEBUG) Log.v(TAG, "$this onCancelled")
            loader.dispatchOnCancelled(this, result)
        }

        override fun run() {
            loader.executePendingTask()
        }
    }

    fun dispatchOnLoadComplete(task: LoadTask<T>, data: T?) {
        if (mTask != task) {
            if (DEBUG) Log.v(
                TAG,
                "Load complete of old task, trying to cancel"
            )
            dispatchOnCancelled(task, data)
        } else {
            if (isAbandoned) {
                // This cursor has been abandoned; just cancel the new data.
                onCanceled(data)
            } else {
                commitContentChanged()
                mLastLoadCompleteTime = SystemClock.uptimeMillis()
                mTask = null
                if (DEBUG) Log.v(TAG, "Delivering result")
                deliverResult(data)
            }
        }
    }

    fun dispatchOnCancelled(task: LoadTask<T>, data: T?) {
        onCanceled(data)
        if (mCancellingTask == task) {
            if (DEBUG) Log.v(TAG, "Cancelled task is now canceled!")
            rollbackContentChanged()
            mLastLoadCompleteTime = SystemClock.uptimeMillis()
            mCancellingTask = null
            if (DEBUG) Log.v(TAG, "Delivering cancellation")
            deliverCancellation()
            executePendingTask()
        }
    }

    /**
     * 如果当前 [loadInBackground] 调用被取消，则返回 true。
     *
     * @return 如果当前 [loadInBackground] 调用被取消，则为 True。
     *
     * @see loadInBackground
     */
    fun isLoadInBackgroundCanceled(): Boolean {
        return mCancellingTask != null
    }

    /**
     * 调用[loadInBackground].
     *
     * 此方法保留用于加载器框架。子类应覆盖[loadInBackground]而不是此方法。
     *
     * @return 负载操作的结果。
     *
     * @throws OperationCanceledException 如果执行期间取消负载
     *
     * @see loadInBackground
     */
    protected fun onLoadInBackground(): T? {
        return loadInBackground()
    }

    /**
     * 在主线程上调用以中止正在进行的加载。
     *
     * 重写此方法以中止在工作线程上在后台运行的 [loadInBackground]的当前调用。
     *
     * 如果 [loadInBackground] 尚未开始运行或者已经完成，则此方法不应执行任何操作。
     *
     * @see loadInBackground
     */
    open fun cancelLoadInBackground() {
    }

    final override fun forceLoad() {
        super.forceLoad()
    }

    override fun onForceLoad() {
        super.onForceLoad()
        cancelLoad()
        mTask = LoadTask(this)
        if (DEBUG) Log.v(TAG, "Preparing load: mTask=$mTask")
        executePendingTask()
    }

    override fun onCancelLoad(): Boolean {
        if (DEBUG) Log.v(TAG, "onCancelLoad: mTask=$mTask")
        mTask?.let { task ->
            if (mCancellingTask != null) {
                // There was a pending task already waiting for a previous
                // one being canceled; just drop it.
                if (DEBUG) Log.v(TAG, "cancelLoad: still waiting for cancelled task; dropping next")
                mTask = null
                return false
            } else {
                task.cancel()
                val cancelled = task.isCancelled()
                if (DEBUG) Log.v(TAG, "cancelLoad: cancelled=$cancelled")
                if (cancelled) {
                    mCancellingTask = task
                    cancelLoadInBackground()
                }
                mTask = null
                return cancelled
            }
        }
        return false
    }

    fun executePendingTask() {
        mTask?.let { task ->
            if (mCancellingTask == null) {
                if (DEBUG) Log.v(TAG, "Executing: $task")
                task.execute()
            }

        }
    }

    final override fun onStartLoading() {
        if (mResult != null) {
            deliverResult(mResult)
        }
        if (takeContentChanged() || mResult == null) {
            forceLoad()
        }
    }

    override fun onStopLoading() {
        cancelLoad()
        mTask?.cancel()
    }

    override fun deliverResult(data: T?) {
        if (isReset) {
            if (data != null) {
                onDiscardResult(data)
            }
            return
        }

        val oldResult = mResult
        mResult = data

        if (isStarted) {
            super.deliverResult(data)
        }

        if (oldResult != null && oldResult !== mResult) {
            onDiscardResult(oldResult)
        }
    }

    override fun onReset() {
        super.onReset()

        onStopLoading()
        mResult?.let { onDiscardResult(it) }
        mResult = null
    }

    open fun onCanceled(data: T?) {
        if (data != null) {
            onDiscardResult(data)
        }
    }

    /**
     * 在丢弃加载结果时调用，以便子类可以处理清理或回收任务。
     * 如果后续调用 [loadInBackground] 时再次返回相同的结果（通过指针相等性判断），或者结果为 null，则不会调用此方法。
     *
     * 请注意，这可能与 [loadInBackground] 同时调用，并且在某些情况下可能对给定对象调用多次。
     *
     * @param result [loadInBackground] 返回的将被丢弃的值。
     */
    protected abstract fun onDiscardResult(result: T)
    abstract fun loadInBackground(): T?
}

abstract class ModernAsyncTask<Params, Progress, Result> : CoroutineScope by MainScope() {
    private val mWorker: WorkerRunnable<Params, Result>
    val mCancelled: AtomicBoolean = AtomicBoolean()
    val mTaskInvoked: AtomicBoolean = AtomicBoolean()

    @Volatile
    private var mStatus = Status.PENDING

    enum class Status {
        /**
         * 表示尚未执行任务
         */
        PENDING,

        /**
         * 表示任务正在运行。
         */
        RUNNING,

        /**
         * 表示[ModernAsyncTask.onPostExecute]已经完成。
         */
        FINISHED,
    }

    init {
        mWorker = object : WorkerRunnable<Params, Result>() {
            @Throws(Exception::class)
            override suspend fun call(): Result? {
                mTaskInvoked.set(true)
                onPreExecute()
                var result: Result? = null
                try {
                   dLog { "doInBackground>>>>mParams:${mParams}" }
                    result = withContext(Dispatchers.IO) {
                        dLog { "doInBackground0000>>>>params:${mParams}" }
                        doInBackground(params = mParams)
                    }
                } catch (tr: Throwable) {
                    mCancelled.set(true)
                    throw tr
                } finally {
                    finish(result)
                }
                return result
            }
        }
    }

    fun execute(vararg params: Params) {
        if (mStatus != Status.PENDING) {
            when (mStatus) {
                Status.RUNNING -> throw IllegalStateException(
                    "Cannot execute task:"
                            + " the task is already running."
                )

                Status.FINISHED -> throw IllegalStateException(
                    ("Cannot execute task:"
                            + " the task has already been executed "
                            + "(a task can be executed only once)")
                )

                else -> throw IllegalStateException("We should never reach this state")
            }
        }

        mStatus = Status.RUNNING
        mWorker.mParams = params
        launch { mWorker.call() }
    }

    fun finish(result: Result?) {
        if (isCancelled()) {
            onCancelled(result)
        } else {
            onPostExecute(result)
        }
        mStatus = Status.FINISHED
    }

    /**
     * 此方法可以从 [doInBackground] 调用，以便在后台计算仍在运行时在UI线程上发布更新。
     * 每次调用此方法都会触发UI线程上 [onProgressUpdate] 的执行。
     *
     * 如果任务已被取消，则 [onProgressUpdate] 将不会被调用。
     *
     * @param values 更新UI的进度值。
     * @see onProgressUpdate
     * @see doInBackground
     */
    protected fun publishProgress(vararg values: Progress) {
        if (!isCancelled()) {
            if (Looper.myLooper() == Looper.getMainLooper()) {
                onProgressUpdate(*values)
            } else {
                launch {
                    onProgressUpdate(*values)
                }
            }
        }
    }

    /**
     * 重写此方法可在后台线程上执行计算。指定的参数是此任务调用者传递给 [execute] 的参数。
     *
     * 此方法可以调用 [publishProgress] 在 UI 线程上发布更新。
     *
     * @param params 任务的参数。
     *
     * @return [Result] 由此任务的子类定义。
     * @see onPreExecute
     * @see onPostExecute
     * @see publishProgress
     */
    protected open suspend fun doInBackground(vararg params: Params): Result? {
       dLog { "doInBackground>>>>params:${params.toList()}" }
        return onLoadInBackground(params = params)
    }


    protected open fun onLoadInBackground(vararg params: Params): Result? {
        return null
    }

    /**
     * 在[doInBackground]之前在UI线程上运行。
     *
     * @see onPostExecute
     * @see doInBackground
     */
    protected open fun onPreExecute() {
    }

    /**
     *
     * 在 [doInBackground] 之后在 UI 线程上运行。 指定的结果是 [doInBackground] 返回的值。
     * 如果任务被取消，则不会调用此方法。
     *
     * @param result [doInBackground] 计算的操作结果
     * @see onPreExecute
     * @see doInBackground
     * @see onCancelled
     */
    protected open fun onPostExecute(result: Result?) {
    }

    /**
     * 调用[publishProgress]后，在UI线程上运行。
     * 指定的值是传递给[publishProgress]的值。
     *
     * @param values 表示进度的值。
     * @see publishProgress
     * @see doInBackground
     */
    protected open fun onProgressUpdate(vararg values: Progress) {
    }

    fun cancel() {
        this.cancel("task is canceled")
        mCancelled.set(true)
    }

    /**
     * 如果此任务在正常完成之前被取消，则返回 true。
     * 如果您在任务上调用 [cancel]，则应从 [doInBackground] 开始定期检查此方法的返回值，以尽快结束任务。
     *
     * @return 如果任务在完成之前已取消则返回true
     * @see cancel
     */
    fun isCancelled(): Boolean {
        return mCancelled.get()
    }

    /**
     * 在调用 [cancel] 方法后，在 UI 线程中运行，
     * 并且 [doInBackground] 已完成。
     *
     * 默认实现简单地调用 [onCancelled] 并忽略结果。
     * 如果您编写自己的实现，请勿调用
     * `super.onCancelled(result)`。
     *
     * @param result 在 [doInBackground] 中计算的结果，可能为 null
     * @see cancel(boolean)
     * @see isCancelled()
     */
    @Suppress("UNUSED_PARAMETER")
    protected open fun onCancelled(result: Result?) {
        onCancelled()
    }

    /**
     * 应用程序应当重写 [onCancelled] 方法。
     * 此方法由 [onCancelled] 的默认实现调用。
     *
     * 在调用 [cancel] 后在 UI 线程运行，并且
     * [doInBackground] 已完成。
     *
     * @see onCancelled(result) 带参数的取消方法
     * @see cancel(boolean) 取消操作
     * @see isCancelled() 检查是否已取消
     */
    protected open fun onCancelled() {

    }

    private abstract class WorkerRunnable<Params, Result>(vararg params: Params) {
        @Throws(Exception::class)
        abstract suspend fun call(): Result?
        var mParams: Array<out Params> = params
    }
}