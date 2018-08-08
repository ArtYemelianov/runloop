package com.launchmode.artus.runlooptest.scheduler

import android.os.Handler
import android.os.Looper
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledFuture
import java.util.concurrent.TimeUnit

/**
 * Base Repeat timer
 */
abstract class RepeatTimer(val cycleDelegate: TaskCycleCallback?) {

    companion object {
        interface TaskCycleCallback {
            /**
             * Handles cycle of task changed
             * @param status If true - task started. Otherwise - stopped
             */
            fun onChange(status: Boolean)
        }
    }

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
            sendCallback(true)
            execute()
            sendCallback(false)
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
//        scheduler.shutdownNow()
    }


    /**
     * Sends result into main thread
     */
    private fun sendCallback(cycle: Boolean) {
        if (Thread.currentThread().id == Looper.getMainLooper().thread.id) {
            cycleDelegate?.onChange(cycle)
        } else {
            mHandler.post({
                cycleDelegate?.onChange(cycle)
            })
        }
    }

}