package com.launchmode.artus.runlooptest.datasource

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import com.launchmode.artus.runlooptest.datasource.runtime.IRssStorage
import com.launchmode.artus.runlooptest.datasource.webservice.Resource
import com.launchmode.artus.runlooptest.datasource.webservice.WebService
import com.launchmode.artus.runlooptest.model.RssEntry
import com.launchmode.artus.runlooptest.utils.AppExecutors

/**
 * Class presents retrieving of Rss data
 */
open class RssRepository constructor(protected val webService: WebService,
                                     protected val storage: IRssStorage,
                                     protected val appExecutors: AppExecutors) {

    fun getRss(url: String): LiveData<Resource<List<RssEntry>>> {
        return object : NetworkBoundResource<List<RssEntry>>(url, appExecutors) {

            override fun makefetch(): List<RssEntry>? {
                return webService.execute()
            }

            override fun saveNetworkResult(data: List<RssEntry>) {
                storage.insertRss(url, data)
            }

            override fun loadFromDatabase(): LiveData<List<RssEntry>> {
                //TODO load in DiskIO executor
                return storage.queryRssByUrl(url) ?: MutableLiveData()
            }

        }.asLiveData()
    }
}