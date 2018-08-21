package com.launchmode.artus.runlooptest.scheduler

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MediatorLiveData
import android.os.Looper
import com.launchmode.artus.runlooptest.datasource.RssRepository
import com.launchmode.artus.runlooptest.datasource.webservice.Resource
import com.launchmode.artus.runlooptest.utils.Parser
import com.launchmode.artus.runlooptest.model.RssEntry
import okhttp3.OkHttpClient
import okhttp3.Request
import java.text.SimpleDateFormat

/**
 * Presents repeated timer for rss request
 */
class RequestRepeatTimer(private val url: String,
                         var repository: RssRepository,
                         cycleDelegate: TaskCycleCallback?) : RepeatTimer(cycleDelegate) {


    private val _result: MediatorLiveData<Resource<List<RssEntry>>> = MediatorLiveData()
    val result: LiveData<Resource<List<RssEntry>>>
        get() = _result


    override fun execute() {
        val data = repository.getRss(url)
        mHandler.post {
            _result.addSource(data) {
                _result.removeSource(data)
                _result.value = it
            }
        }
    }
}