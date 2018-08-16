package com.launchmode.artus.runlooptest.viewmodel

import android.annotation.SuppressLint
import android.app.Application
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import android.support.annotation.VisibleForTesting
import com.launchmode.artus.runlooptest.model.RssEntry

class RssDetailViewModelFactory(val app: Application, val data: RssEntry) :
        ViewModelProvider.NewInstanceFactory() {


    override fun <T : ViewModel> create(modelClass: Class<T>): T =
            with(modelClass) {
                when {
                    isAssignableFrom(RssDetailViewModel::class.java) ->
                        RssDetailViewModel(app, data)
                    else ->
                        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
                }
            } as T

    companion object {


        @SuppressLint("StaticFieldLeak")
        private var INSTANCE: RssDetailViewModelFactory? = null

        fun getInstance(application: Application, data: RssEntry) =
                INSTANCE ?: synchronized(RssDetailViewModelFactory::class.java) {
                    INSTANCE
                            ?: RssDetailViewModelFactory(application, data).also { INSTANCE = it }
                }


        @VisibleForTesting
        fun destroyInstance() {
            INSTANCE = null
        }
    }
}