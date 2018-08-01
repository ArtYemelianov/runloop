package com.launchmode.artus.runlooptest.model

import android.arch.lifecycle.MutableLiveData
import android.os.Looper
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledFuture
import java.util.concurrent.TimeUnit

class InfoModel {

    val name: String = "Artem Yemelianov"
    var date: MutableLiveData<String>
    var selectedItem: MutableLiveData<String>

    private var future: ScheduledFuture<*>? = null
    private  val scheduler = Executors.newSingleThreadScheduledExecutor()

    init {
        date = MutableLiveData()
        updateDate()

        selectedItem = MutableLiveData()
        scheduleNext()
    }

    private fun scheduleNext(){
        future = scheduler.scheduleAtFixedRate({
            android.os.Handler(Looper.getMainLooper()).post({
                updateDate()
            })
        }, 1000, 1000, TimeUnit.MILLISECONDS)
    }


    private fun updateDate(){
        val currentDate = SimpleDateFormat("dd/M/yyyy hh:mm:ss")
        date.value = currentDate.format(Date())
    }
}