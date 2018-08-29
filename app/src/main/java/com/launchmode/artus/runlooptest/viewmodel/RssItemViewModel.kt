package com.launchmode.artus.runlooptest.viewmodel

import android.arch.lifecycle.ViewModel
import android.databinding.ObservableField
import android.view.View
import com.launchmode.artus.runlooptest.model.RssEntry
import java.text.SimpleDateFormat
import java.util.*

class RssItemViewModel(data: RssEntry) : ViewModel() {

    var rssData: ObservableField<RssEntry> = ObservableField()
    var clicked: ObservableField<Void> = ObservableField()

    init {
        rssData.set(data)
    }

    var title: String = rssData.get()?.title ?: "Title"
    var description: String = rssData.get()?.description ?: "Description"
    val date: String
        get() {
            val currentDate = SimpleDateFormat("dd/M/yyyy hh:mm:ss")
            return currentDate.format(Date(rssData.get()!!.date))
        }

    fun onItemClick(view: View) {
        print("Clicked")
        clicked.notifyChange()
    }

}