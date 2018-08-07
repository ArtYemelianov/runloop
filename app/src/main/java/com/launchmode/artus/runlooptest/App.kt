package com.launchmode.artus.runlooptest

import android.app.Application
import android.databinding.DataBindingUtil
import com.launchmode.artus.runlooptest.data.RssService
import com.launchmode.artus.runlooptest.databinding.AppDataBindingComponent


/**
 * Created by Gregory Rasmussen on 7/26/17.
 */
class App : Application() {

    private var _rssService: RssService? = null

    override fun onCreate() {
        super.onCreate()
        DataBindingUtil.setDefaultComponent(AppDataBindingComponent())
    }


    val rssService: RssService
        get() {
        if ( _rssService == null){
            _rssService = RssService.create()
        }
        return _rssService!!
    }

}
