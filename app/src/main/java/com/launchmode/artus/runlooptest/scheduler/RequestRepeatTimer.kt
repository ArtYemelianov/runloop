package com.launchmode.artus.runlooptest.scheduler

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MediatorLiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Observer
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
                         var repository: RssRepository) : RepeatTimer() {


    private val _result: MutableLiveData<Resource<List<RssEntry>>> = MutableLiveData()
    val result: LiveData<Resource<List<RssEntry>>>
        get() = _result


    override fun execute() {
        mHandler.post {
            val data = repository.getRss(url)

            val observer = Observer<Resource<List<RssEntry>>> {
                _result.value = it
            }
            data.observeForever(observer)
        }
    }
}