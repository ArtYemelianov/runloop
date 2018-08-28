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
import com.launchmode.artus.runlooptest.utils.ResourceObserver
import okhttp3.OkHttpClient
import okhttp3.Request
import java.text.SimpleDateFormat

/**
 * Presents repeated timer for rss request
 */
class RequestRepeatTimer(private val url: String,
                         var repository: RssRepository) : RepeatTimer() {


    private val _result: MediatorLiveData<Resource<List<RssEntry>>> = MediatorLiveData()
    val result: LiveData<Resource<List<RssEntry>>>
        get() = _result


    override fun execute() {
        mHandler.post {
            val data = repository.getRss(url)

            _result.addSource(data) {
                _result.value = it

                if (it!!.status != Resource.Status.LOADING) {
                    _result.removeSource(data)
                    nextSchedule()
                }

            }
        }
    }
}