package com.launchmode.artus.runlooptest.model

import android.arch.lifecycle.MutableLiveData
import com.launchmode.artus.runlooptest.App
import com.launchmode.artus.runlooptest.datasource.webservice.RssService

class RssModel(app: App) {

    val businessNews: MutableLiveData<List<RssEntry>> = MutableLiveData()
    var otherNews: MutableLiveData<List<RssEntry>> = MutableLiveData()
    var selectedItem: MutableLiveData<String> = MutableLiveData()
    var isLoading: MutableLiveData<Boolean> = MutableLiveData()

    private val service: RssService = app.rssService

    init {
        businessNews.value = ArrayList()
        otherNews.value = ArrayList()
        isLoading.value = false

        service.delegate = object : RssService.Companion.RssServiceCallback {
            override fun onBusinessDataChanged(list: List<RssEntry>) {
                businessNews.value = list
            }

            override fun onOtherChanged(list: List<RssEntry>) {
                otherNews.value = list
            }

            override fun onTaskCycleChanged(load: Boolean) {
                isLoading.value = load
            }

        }
        service.start(5000)
    }


    fun clear() {
        service.stop()
    }

}