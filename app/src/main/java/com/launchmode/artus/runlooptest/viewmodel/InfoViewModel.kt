package com.launchmode.artus.runlooptest.viewmodel

import android.arch.lifecycle.*
import android.databinding.ObservableField
import com.launchmode.artus.runlooptest.model.InfoModel

class InfoViewModel : ViewModel(){

    var name: ObservableField<String> = ObservableField()
    var date: ObservableField<String> = ObservableField()
    var selectedItem: ObservableField<String> = ObservableField()

    private var model: InfoModel = InfoModel()

    init {
        name.set(model.name)
    }


    fun getDate(): LiveData<String> {
        return model.date
    }

    fun getSelectedItem(): LiveData<String> {
        return model.selectedItem
    }
}