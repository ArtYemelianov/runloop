package com.launchmode.artus.runlooptest

import android.app.Application
import android.databinding.DataBindingUtil
import com.launchmode.artus.runlooptest.datasource.webservice.RssService
import com.launchmode.artus.runlooptest.databinding.AppDataBindingComponent


/**
 * Created by Artus
 */
class App : Application() {

    private var _rssService: RssService? = null

    override fun onCreate() {
        super.onCreate()
        DataBindingUtil.setDefaultComponent(AppDataBindingComponent())
    }

    // keep RssService object as single Instance to prevent creation it in different places
    val rssService: RssService
        get() {
            if (_rssService == null || _rssService!!.isDestroyed) {
                _rssService = RssService.create()
            }
            return _rssService!!
        }

}
