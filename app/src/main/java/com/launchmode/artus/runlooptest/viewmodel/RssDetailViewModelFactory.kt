package com.launchmode.artus.runlooptest.viewmodel

import android.app.Application
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.launchmode.artus.runlooptest.model.RssEntry

class RssDetailViewModelFactory(val app: Application, val data: RssEntry) :
        ViewModelProvider.NewInstanceFactory() {


    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return RssDetailViewModel(app, data) as T

    }
}