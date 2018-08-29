package com.launchmode.artus.runlooptest.viewmodel

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.Observer
import android.databinding.ObservableBoolean
import android.databinding.ObservableField
import android.databinding.ObservableInt
import com.launchmode.artus.runlooptest.App
import com.launchmode.artus.runlooptest.datasource.webservice.RssService
import com.launchmode.artus.runlooptest.model.RssEntry
import com.launchmode.artus.runlooptest.view.RssAdapter

class RssViewModel(application: Application) : AndroidViewModel(application) {

    var currentTab: ObservableInt = ObservableInt()
    var businessNewsAdapter: ObservableField<RssAdapter> = ObservableField()
    var otherNewsAdapter: ObservableField<RssAdapter> = ObservableField()
    var isLoading: ObservableBoolean = ObservableBoolean()

    private val app: App = getApplication()
    private val service = RssService(app.storage, app.rssRepository, app.appExecutors)

    init {
        currentTab.set(0)
        businessNewsAdapter.set(RssAdapter())
        otherNewsAdapter.set(RssAdapter())
        service.start(5000)
    }

    /**
     * Subscribes observes
     */
    fun subscribe(lifecycleOwner: LifecycleOwner) {

        service.business.observe(lifecycleOwner, Observer<List<RssEntry>> {
            businessNewsAdapter.get()?.updateData(it)
        })

        service.other.observe(lifecycleOwner, Observer<List<RssEntry>> {
            otherNewsAdapter.get()?.updateData(it)
        })

        service.isLoading.observe(lifecycleOwner, Observer<Boolean> {
            isLoading.set(it!!)
        })

    }

    fun onTabChanged(id: String) {
        print("Tab changed, value: " + currentTab.get())
    }

    override fun onCleared() {
        service.stop()
    }
}