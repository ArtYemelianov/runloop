package com.launchmode.artus.runlooptest.datasource.runtime

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import com.launchmode.artus.runlooptest.model.RssEntry

class RssRuntimeStorage : IRssStorage {

    private val storage: HashMap<String, MutableLiveData<List<RssEntry>>> = HashMap()

    override fun insertRss(url: String, list: List<RssEntry>) {
        var result: MutableLiveData<List<RssEntry>>? = storage[url]
        if (result == null) {
            result = MutableLiveData()
            storage[url] = result
        }
        result.value = list
    }

    override fun queryRssByUrl(url: String): LiveData<List<RssEntry>>? {
        return storage[url]
    }
}