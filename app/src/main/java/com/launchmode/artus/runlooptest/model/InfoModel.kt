package com.launchmode.artus.runlooptest.model

import android.arch.lifecycle.MutableLiveData
import android.os.Handler
import android.os.Looper
import com.launchmode.artus.runlooptest.scheduler.RepeatTimer

/**
 * Info model
 */
class InfoModel {

    val name: String = "Artem Yemelianov"
    var date: MutableLiveData<Long> = MutableLiveData()
    var selectedItem: MutableLiveData<String> = MutableLiveData()
    private val handler: Handler = Handler(Looper.getMainLooper())

    private val timer: RepeatTimer by lazy {
        object : RepeatTimer() {
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