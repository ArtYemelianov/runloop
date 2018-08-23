package com.launchmode.artus.runlooptest.scheduler

import android.os.Handler
import android.os.Looper
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledFuture
import java.util.concurrent.TimeUnit

/**
 * Base Repeat timer
 */
abstract class RepeatTimer {

    private var future: ScheduledFuture<*>? = null
    private val scheduler = Executors.newSingleThreadScheduledExecutor()
    protected val mHandler = Handler(Looper.getMainLooper())

    /**
     * Starts scheduler. <br>
     * It stops if timer already launched and starts again
     * @param interval Interval between the tasks
     */
    fun start(interval: Long) {
        stop()

        scheduler.scheduleWithFixedDelay({
            execute()
        }, 0, interval, TimeUnit.MILLISECONDS)

    }

    /**
     * Executes specific task
     */
    abstract fun execute()


    /**
     * Stops scheduler
     */
    fun stop() {
        future?.cancel(false)
    }

    fun destroy() {
        stop()
    }

}