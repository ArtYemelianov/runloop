package com.launchmode.artus.runlooptest.viewmodel

import android.arch.lifecycle.*
import android.databinding.ObservableField
import com.launchmode.artus.runlooptest.model.InfoModel

class InfoViewModel : ViewModel() {

    var name: ObservableField<String> = ObservableField()

    val model: InfoModel = InfoModel()

    init {
        name.set(model.name)
    }

    override fun onCleared() {
        super.onCleared()
        model.destroy()
    }
}