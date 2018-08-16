package com.launchmode.artus.runlooptest.model

import android.arch.lifecycle.MutableLiveData
import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import com.launchmode.artus.runlooptest.scheduler.RepeatTimer
import com.launchmode.artus.runlooptest.scheduler.RequestRepeatTimer
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledFuture
import java.util.concurrent.TimeUnit

/**
 * Info model
 */
class InfoModel {

    val name: String = "Artem Yemelianov"
    var date: MutableLiveData<Long> = MutableLiveData()
    var selectedItem: MutableLiveData<String> = MutableLiveData()
    private val handler: Handler = Handler(Looper.getMainLooper())

    private val timer: RepeatTimer by lazy {
        object : RepeatTimer(null) {
            override fun execute() {
                handler.post { updateDate() }
            }
        }
    }

    init {
        timer.start(1000)
    }

    /**
     * Updates a value field
     */
    private fun updateDate() {
        date.value = System.currentTimeMillis()
    }

    /**
     * Destroy model
     */
    fun destroy() {
        timer.destroy()
    }
}