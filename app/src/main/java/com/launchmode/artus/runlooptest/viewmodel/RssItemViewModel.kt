package com.launchmode.artus.runlooptest.viewmodel

import android.arch.lifecycle.ViewModel
import android.databinding.ObservableField
import com.launchmode.artus.runlooptest.model.RssEntry

class RssItemViewModel(data: RssEntry): ViewModel() {

    var rssData : ObservableField<RssEntry> = ObservableField()

    init {
        rssData.set(data)
    }


    var title : String = rssData.get()?.title ?: "Title"
    var description : String = rssData.get()?.description?: "Description"
    var date: String = rssData.get()?.date ?: "Date"
}