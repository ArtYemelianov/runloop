package com.launchmode.artus.runlooptest.model

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.os.Looper
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.ScheduledFuture
import java.util.concurrent.TimeUnit
import java.util.logging.Handler

class InfoModel {

    val name: String = "Artem Yemelianov"

    var date: MutableLiveData<String>

    private var future: ScheduledFuture<*>? = null
    private  val scheduler: ScheduledExecutorService

    init {
        date = MutableLiveData()
        updateDate()
        scheduler = Executors.newSingleThreadScheduledExecutor()
        scheduleNext()
    }

    private fun scheduleNext(){
        future = scheduler.scheduleAtFixedRate({
            print("Update timer")
            android.os.Handler(Looper.getMainLooper()).post({
                updateDate()
            })
        }, 1000, 1000, TimeUnit.MILLISECONDS)
    }


    private fun getDate() : LiveData<String> {
        return date
    }

    private fun updateDate(){
        val currentDate = SimpleDateFormat("dd/M/yyyy hh:mm:ss")
        date.value = currentDate.format(Date())
    }
}