package com.launchmode.artus.runlooptest.datasource.runtime

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import com.launchmode.artus.runlooptest.model.RssEntry
import com.launchmode.artus.runlooptest.utils.AppExecutors

class RssRuntimeStorage(private val appExecutors: AppExecutors) : IRssStorage {

    private val storage: HashMap<String, MutableLiveData<List<RssEntry>>> = HashMap()
    private val monitor: Any = Any()

    override fun insertRss(url: String, list: List<RssEntry>) {
        appExecutors.mainThread().execute {
            synchronized(monitor) {
                putIfAbsent(url)
                var result: MutableLiveData<List<RssEntry>> = storage[url]!!
                result.value = list
            }
        }
    }

    override fun queryRssByUrl(url: String): LiveData<List<RssEntry>> {
        var result: LiveData<List<RssEntry>>? = null
        synchronized(monitor) {
            putIfAbsent(url)
            result = storage[url]!!
        }

        return result!!
    }

    private fun putIfAbsent(url: String) {
        if (storage[url] == null) {
            storage[url] = MutableLiveData()
            insertRss(url, emptyList())
        }
    }
}