package com.launchmode.artus.runlooptest

import android.app.Application
import android.databinding.DataBindingUtil
import com.launchmode.artus.runlooptest.databinding.AppDataBindingComponent
import com.launchmode.artus.runlooptest.datasource.RssRepository
import com.launchmode.artus.runlooptest.datasource.runtime.IRssStorage
import com.launchmode.artus.runlooptest.datasource.runtime.RssRuntimeStorage
import com.launchmode.artus.runlooptest.datasource.webservice.WebService
import com.launchmode.artus.runlooptest.utils.AppExecutors


/**
 * Created by Artus
 */
class App : Application() {

    private var _rssRepository: RssRepository? = null
    private val _webService: WebService by lazy { WebService() }
    private val _storage: IRssStorage by lazy { RssRuntimeStorage(appExecutors) }
    private val _appExecutors: AppExecutors by lazy { AppExecutors() }

    override fun onCreate() {
        super.onCreate()
        DataBindingUtil.setDefaultComponent(AppDataBindingComponent())
    }

    // keep RssService object as single Instance to prevent creation it in different places
    val rssRepository: RssRepository
        get() {
            if (_rssRepository == null) {
                _rssRepository = RssRepository(_webService, _storage, _appExecutors)
            }
            return _rssRepository!!
        }

    val webService: WebService
        get() = _webService

    val appExecutors: AppExecutors
        get() = _appExecutors

    val storage: IRssStorage
        get() = _storage
}
