package com.launchmode.artus.runlooptest.viewmodel

import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModel
import android.databinding.ObservableField
import com.launchmode.artus.runlooptest.model.InfoModel
import kotlinx.android.synthetic.main.info_layout.*

class InfoViewModel : ViewModel(), LifecycleOwner{

    var name: ObservableField<String>
    var date: ObservableField<String>

    private var model: InfoModel = InfoModel()

    init {
        name = ObservableField()
        name.set(model.name)

        date = ObservableField()
        getDate().observe(this, object : Observer<String> {
            override fun onChanged(t: String?) {
                date.set(t)
            }

        })

    }


    fun getDate(): LiveData<String> {
        return model.date
    }
}