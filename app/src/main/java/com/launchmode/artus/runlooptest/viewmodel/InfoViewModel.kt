package com.launchmode.artus.runlooptest.viewmodel

import android.arch.lifecycle.*
import android.databinding.ObservableField
import com.launchmode.artus.runlooptest.model.InfoModel

class InfoViewModel : ViewModel() {

    var name: ObservableField<String> = ObservableField()
    var date: ObservableField<String> = ObservableField()
    var selectedItem: ObservableField<String> = ObservableField()

    private var model: InfoModel = InfoModel()

    init {
        name.set(model.name)
    }

    /**
     * Subscribes observers
     */
    fun subscribe(lifecycle: LifecycleOwner) {
        model.date.observe(lifecycle, Observer<String> {
            date.set(it)
        })

        model.selectedItem.observe(lifecycle, Observer<String> {
            selectedItem.set(it)
        })
    }

}