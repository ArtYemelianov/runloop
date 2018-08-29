package com.launchmode.artus.runlooptest.datasource.runtime

import android.arch.lifecycle.LiveData
import com.launchmode.artus.runlooptest.model.RssEntry

interface IRssStorage {

    /**
     * Queries specific list by url
     */
    fun queryRssByUrl(url: String): LiveData<List<RssEntry>>

    /**
     * Insert Rss.
     * It completely replaces the old data
     */
    fun insertRss(url: String, list: List<RssEntry>)
}