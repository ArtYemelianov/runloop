package com.launchmode.artus.runlooptest.viewmodel

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.databinding.ObservableField
import android.view.View
import com.launchmode.artus.runlooptest.model.RssEntry

class RssDetailViewModel(app: Application, data: RssEntry) : AndroidViewModel(app) {

    var rssData: ObservableField<RssEntry> = ObservableField()

    init {
        rssData.set(data)
    }


    var title: String = rssData.get()?.title ?: "Title"
    var description: String = rssData.get()!!.description
    var date: String = rssData.get()!!.date
}