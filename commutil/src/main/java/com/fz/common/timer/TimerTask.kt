package com.fz.common.timer

abstract class TimerTask : Runnable {
    companion object {
        /**
         * This task has not yet been scheduled.
         */
        const val VIRGIN = 0

        /**
         * This task is scheduled for execution.  If it is a non-repeating task,
         * it has not yet been executed.
         */
        const val SCHEDULED = 1

        /**
         * This non-repeating task has already executed (or is currently
         * executing) and has not been cancelled.
         */
        const val EXECUTED = 2

        /**
         * This task has been cancelled (with a call to TimerTask.cancel).
         */
        const val CANCELLED = 3
    }

    /**
     * This object is used to control access to the TimerTask internals.
     */
    private val lock = Any()

    /**
     * Next execution time for this task in the format returned by
     * System.currentTimeMillis, assuming this task is scheduled for execution.
     * For repeating tasks, this field is updated prior to each task execution.
     */
    var nextExecutionTime: Long = 0

    /**
     * Period in milliseconds for repeating tasks.  A positive value indicates
     * fixed-rate execution.  A negative value indicates fixed-delay execution.
     * A value of 0 indicates a non-repeating task.
     */
    var period: Long = 0

    /**
     * The state of this task, chosen from the constants below.
     */
    var state = VIRGIN
    open fun cancel(): Boolean {
        synchronized(lock) {
            val result = state == SCHEDULED
            state = CANCELLED
            return result
        }
    }

    open fun scheduledExecutionTime(): Long {
        synchronized(lock) { return if (period < 0) nextExecutionTime + period else nextExecutionTime - period }
    }
}