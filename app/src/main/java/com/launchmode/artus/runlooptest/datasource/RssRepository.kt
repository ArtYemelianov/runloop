package com.launchmode.artus.runlooptest.datasource

import android.arch.lifecycle.LiveData
import com.artus.rssreader.Article
import com.launchmode.artus.runlooptest.datasource.runtime.RssRuntimeStorage
import com.launchmode.artus.runlooptest.datasource.webservice.Resource
import com.launchmode.artus.runlooptest.datasource.webservice.RssService
import com.launchmode.artus.runlooptest.model.RssEntry
import com.launchmode.artus.runlooptest.utils.AppExecutors

open class RssRepository constructor(protected val rssService: RssService,
                                     protected val restaurantsDao: RssRuntimeStorage,
                                     protected val appExecutors: AppExecutors) {

    fun getRss(url: String): LiveData<Resource<List<RssEntry>>> {
        return object : NetworkBoundResource<List<RssEntry>>(url, appExecutors) {
            override fun saveNetworkResult(data: List<RssEntry>) {

            }

            override fun loadFromDatabase(): LiveData<List<RssEntry>> {
                appExecutors.diskIO().execute {

                }
            }

            override fun convert(data: List<Article>): List<RssEntry> {
                return data.map { item ->
                    RssEntry(item.title,
                            item.description,
                            item.pubDate.time)
                }.toList()
            }

        }.asLiveData()
    }
}