package com.launchmode.artus.runlooptest.viewmodel

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.Observer
import android.databinding.ObservableBoolean
import android.databinding.ObservableField
import android.databinding.ObservableInt
import com.launchmode.artus.runlooptest.view.RssAdapter
import com.launchmode.artus.runlooptest.model.RssEntry
import com.launchmode.artus.runlooptest.model.RssModel

class RssViewModel(application: Application): AndroidViewModel(application) {

    var currentTab : ObservableInt = ObservableInt()
    var bussinesNewsAdapter: ObservableField<RssAdapter> = ObservableField()
    var bussinesNewsData: ObservableField<List<RssEntry>> = ObservableField()
    var otherNewsAdapter: ObservableField<RssAdapter> = ObservableField()
    var otherNewsData: ObservableField<List<RssEntry>> = ObservableField()
    var isLoading: ObservableBoolean = ObservableBoolean()

    val model =  RssModel()
    init {
        currentTab.set(0)

        bussinesNewsAdapter.set(RssAdapter())
        bussinesNewsData.set(arrayListOf(RssEntry("One", "Description", "Empty")))

        otherNewsAdapter.set(RssAdapter())
        otherNewsData.set(arrayListOf(RssEntry("Twp", "Description", "Empty")))

    }

    /**
     * Subscribes observes
     */
    fun subscribe(lifecycleOwner: LifecycleOwner) {

        model.bussinesNews.observe(lifecycleOwner, Observer<List<RssEntry>> {
            bussinesNewsAdapter.get()?.updateData(it)
//            bussinesNews.get()?.clear()
//            bussinesNews.get()?.addAll(it!!)
//            bussinesNews.get()?.notifyDataSetChanged()
        })

        model.isLoading.observe(lifecycleOwner, Observer<Boolean> {
            isLoading.set(it!!)
        })
    }

    fun onTabChanged(id: String){
        print("Tab changed, value: " +currentTab.get() )
    }
}