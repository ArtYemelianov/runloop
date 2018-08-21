package com.launchmode.artus.runlooptest.viewmodel

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.databinding.ObservableField
import com.launchmode.artus.runlooptest.model.RssEntry
import java.text.SimpleDateFormat
import java.util.*

class RssDetailViewModel(app: Application, data: RssEntry) : AndroidViewModel(app) {

    var rssData: ObservableField<RssEntry> = ObservableField()

    init {
        rssData.set(data)
    }


    var title: String = rssData.get()?.title ?: "Title"
    var description: String = rssData.get()!!.description
    val date: String = formatDate()

    fun formatDate(): String {
        val currentDate = SimpleDateFormat("formatDate/M/yyyy hh:mm:ss")
        return currentDate.format(Date(rssData.get()!!.date))
    }
}