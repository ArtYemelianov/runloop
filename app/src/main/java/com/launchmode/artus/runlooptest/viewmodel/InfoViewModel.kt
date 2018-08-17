package com.launchmode.artus.runlooptest.viewmodel

import android.arch.lifecycle.*
import android.arch.lifecycle.Observer
import android.databinding.ObservableField
import com.launchmode.artus.runlooptest.model.InfoModel
import java.text.SimpleDateFormat
import java.util.*

class InfoViewModel : ViewModel() {

    var name: ObservableField<String> = ObservableField()
    var date: ObservableField<String> = ObservableField()
    var selectedItem: ObservableField<String> = ObservableField()

    private val model: InfoModel = InfoModel()

    init {
        name.set(model.name)
    }

    /**
     * Subscribes observers
     */
    fun subscribe(lifecycle: LifecycleOwner) {
        model.date.observe(lifecycle, Observer<Long> {
            // VM can partially be responsibility for interaction logic Model and View,
            // but not always
            val currentDate = SimpleDateFormat("dd/M/yyyy hh:mm:ss")
            date.set(currentDate.format(Date(it!!)))
        })

        model.selectedItem.observe(lifecycle, Observer<String> {
            selectedItem.set(it)
        })
    }

    override fun onCleared() {
        super.onCleared()
        model.destroy()
    }

}