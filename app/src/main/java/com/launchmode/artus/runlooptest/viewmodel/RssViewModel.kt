package com.launchmode.artus.runlooptest.viewmodel

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.Observer
import android.databinding.ObservableBoolean
import android.databinding.ObservableField
import android.databinding.ObservableInt
import com.launchmode.artus.runlooptest.model.RssEntry
import com.launchmode.artus.runlooptest.model.RssModel
import com.launchmode.artus.runlooptest.view.RssAdapter

class RssViewModel(application: Application) : AndroidViewModel(application) {

    var currentTab: ObservableInt = ObservableInt()
    var businesNewsAdapter: ObservableField<RssAdapter> = ObservableField()
    var otherNewsAdapter: ObservableField<RssAdapter> = ObservableField()
    var isLoading: ObservableBoolean = ObservableBoolean()

    val model = RssModel(getApplication())

    init {
        currentTab.set(0)
        businesNewsAdapter.set(RssAdapter())
        otherNewsAdapter.set(RssAdapter())

    }

    /**
     * Subscribes observes
     */
    fun subscribe(lifecycleOwner: LifecycleOwner) {

        model.bussinesNews.observe(lifecycleOwner, Observer<List<RssEntry>> {
            businesNewsAdapter.get()?.updateData(it)
        })

        model.otherNews.observe(lifecycleOwner, Observer<List<RssEntry>> {
            otherNewsAdapter.get()?.updateData(it)
        })

        model.isLoading.observe(lifecycleOwner, Observer<Boolean> {
            isLoading.set(it!!)
        })
    }

    fun onTabChanged(id: String) {
        print("Tab changed, value: " + currentTab.get())
    }

    override fun onCleared() {
        model.clear()
    }
}