package com.launchmode.artus.runlooptest.datasource.runtime

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.support.annotation.MainThread
import com.launchmode.artus.runlooptest.model.RssEntry
import com.launchmode.artus.runlooptest.utils.AppExecutors

/**
 * Presents runtime storage
 */
@Deprecated("Replace storage for greater thread safety")
class RssRuntimeStorage(private val appExecutors: AppExecutors) : IRssStorage {

    private val storage: HashMap<String, MutableLiveData<List<RssEntry>>> = HashMap()
    private val monitor: Any = Any()

    override fun insertRss(url: String, list: List<RssEntry>) {
        synchronized(monitor) {
            putIfAbsent(url)
            var result: MutableLiveData<List<RssEntry>> = storage[url]!!
            result.postValue(list)
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

    /**
     * Puts a new url if the last absent.
     * Method should be done in synchronized block
     */
    private fun putIfAbsent(url: String) {
        if (storage[url] == null) {
            val data = MutableLiveData<List<RssEntry>>()
            data.postValue(emptyList())
            storage[url] = data
        }
    }
}