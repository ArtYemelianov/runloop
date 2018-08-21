package com.launchmode.artus.runlooptest.datasource.runtime

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import com.launchmode.artus.runlooptest.model.RssEntry

class RssRuntimeStorage : IRssStorage {

    private val storage: HashMap<String, MutableLiveData<List<RssEntry>>> = HashMap()

    override fun insertRss(url: String, list: List<RssEntry>) {
        putIfAbsent(url)
        var result: MutableLiveData<List<RssEntry>> = storage[url]!!
        result.value = list
    }

    override fun queryRssByUrl(url: String): LiveData<List<RssEntry>> {
        putIfAbsent(url)
        return storage[url]!!
    }

    private fun putIfAbsent(url: String) {
        if (storage[url] == null) {
            storage[url] = MutableLiveData()
        }
    }
}