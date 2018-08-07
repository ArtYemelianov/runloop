package com.launchmode.artus.runlooptest.data

import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import android.support.annotation.MainThread
import com.launchmode.artus.runlooptest.utils.Utils
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledFuture
import java.util.concurrent.TimeUnit

/**
 * Presents class that allow emulates delay.
 *
 * Reason: If operation is brief (less 0.5 seconds) loading is shown very quickly and it looks
 * not smooth enough therefore we put delay in emulation to increase the downloading.
 */
class FakeTimerDelay {


    companion object {
        private const val DELAY = 2000L

        interface Callback {
            fun onChanged(boolean: Boolean)
        }
    }

    var delegate: Callback? = null

    private val scheduler = Executors.newSingleThreadScheduledExecutor()
    private val handler = Handler(Looper.getMainLooper())
    private var future: ScheduledFuture<*>? = null
    private var currentFlag: Boolean = false
    private var lastChange: Long? = null

    /**
     * Changes status
     *
     */
    fun change(enabled: Boolean) {
        Utils.fatalErrorIfNotMainThread()
        if (lastChange == null) {
            // not one change was before
            emitChange(enabled)
        } else if (enabled == currentFlag) {
            // do nothing.
        } else if (enabled != currentFlag && SystemClock.elapsedRealtime() - lastChange!! > DELAY) {
            // timeout is over
            future?.cancel(false)
            emitChange(enabled)
        } else {
            // changed flag and timeout is not over yet
            future?.cancel(true)
            future = scheduler.schedule({
                emitChange(enabled)
            }, DELAY, TimeUnit.MILLISECONDS)
        }
    }

    /**
     * Emits event in main thread
     */
    private fun emitChange(value: Boolean) {
        lastChange = SystemClock.elapsedRealtime()
        currentFlag = value
        if (Thread.currentThread().id == Looper.getMainLooper().thread.id) {
            delegate?.onChanged(value)
        } else {
            handler.post { delegate?.onChanged(value) }
        }
    }


}

