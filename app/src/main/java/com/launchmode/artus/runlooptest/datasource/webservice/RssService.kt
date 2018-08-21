package com.launchmode.artus.runlooptest.datasource.webservice

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MediatorLiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Observer
import android.view.View
import android.widget.Toast
import com.launchmode.artus.runlooptest.datasource.RssRepository
import com.launchmode.artus.runlooptest.datasource.runtime.IRssStorage
import com.launchmode.artus.runlooptest.model.RssEntry
import com.launchmode.artus.runlooptest.scheduler.RepeatTimer
import com.launchmode.artus.runlooptest.scheduler.RequestRepeatTimer
import com.launchmode.artus.runlooptest.utils.AppExecutors
import com.launchmode.artus.runlooptest.utils.FakeTimerDelay
import com.launchmode.artus.runlooptest.utils.ResourceObserver
import com.launchmode.artus.runlooptest.utils.Utils
import java.util.concurrent.atomic.AtomicInteger


class RssService(private val storage: IRssStorage,
                 private val repository: RssRepository,
                 private val appExecutors: AppExecutors) {

    companion object {
        const val URL_BUSINESS_NEWS = "http://feeds.reuters.com/reuters/businessNews"
        const val URL_ENVIRONMENT = "http://feeds.reuters.com/reuters/environment"
        const val URL_ENTERTAINMENT = "http://feeds.reuters.com/reuters/entertainment"
    }

    val isLoading: MutableLiveData<Boolean> = MutableLiveData()
    val business: MutableLiveData<List<RssEntry>> = MutableLiveData()
    val other: MutableLiveData<List<RssEntry>> = MutableLiveData()
    val errorMessage: MutableLiveData<String> = MutableLiveData()

    private val _other: MediatorLiveData<List<RssEntry>> = MediatorLiveData()

    init {
        storage.queryRssByUrl(URL_BUSINESS_NEWS).observeForever {
            appExecutors.mainThread().execute {
                business.value = it
            }
        }

        val callback: Observer<List<RssEntry>> = Observer {
            other.value = otherData
        }
        _other.addSource(storage.queryRssByUrl(URL_ENVIRONMENT), callback)
        _other.addSource(storage.queryRssByUrl(URL_ENTERTAINMENT), callback)


    }


    /**
     * A combined data for entertaiment and envirmoment
     */
    private val otherData: List<RssEntry>
        get() {
            val list = ArrayList<RssEntry>()
            list.addAll(storage.queryRssByUrl(URL_ENVIRONMENT).value!!)
            list.addAll(storage.queryRssByUrl(URL_ENTERTAINMENT).value!!)
            return list
        }


    private val businessTimer: RequestRepeatTimer  by lazy {
        val timer = createTimer(URL_BUSINESS_NEWS)
        val onSuccess: (data: List<RssEntry>) -> Unit = {
            print("onSuccess for business $it")
            //do nothing
        }
        val observer = ResourceObserver("RestaurantsMapActivity",
                hideLoading = ::hideLoading,
                showLoading = ::showLoading,
                onSuccess = onSuccess,
                onError = ::showErrorMessage)
        timer.result.observeForever(observer)
        timer
    }
    private val entertainmentTimer: RequestRepeatTimer by lazy { createTimer(URL_ENTERTAINMENT) }
    private val environnementTimer: RequestRepeatTimer by lazy { createTimer(URL_ENVIRONMENT) }

    private val counter: AtomicInteger = AtomicInteger(0)
    private val fakeTimer = FakeTimerDelay()

    init {

        fakeTimer.delegate = object : FakeTimerDelay.Companion.Callback {
            override fun onChanged(value: Boolean) {
                isLoading.value = value
            }
        }
    }

    private fun createTimer(url: String): RequestRepeatTimer {
        return RequestRepeatTimer(url, repository, null)
    }

    private fun checkAndEmitStatusLoading(status: Boolean) {
        var value: Int?
        if (status) {
            value = counter.incrementAndGet()
        } else {
            value = counter.decrementAndGet()
        }
        when (value) {
            1 -> fakeTimer.change(true)
            0 -> fakeTimer.change(false)
        }
    }


    /**
     * Starts scheduler
     * @param interval Interval between requests
     */
    fun start(interval: Long) {
        businessTimer.start(interval)
        entertainmentTimer.start(interval)
        environnementTimer.start(interval)

    }

    /**
     * Stops scheduler
     */
    fun stop() {
        fakeTimer.delegate = null
        businessTimer.destroy()
        entertainmentTimer.destroy()
        environnementTimer.destroy()
    }

    private fun showErrorMessage(error: String) {
        errorMessage.value = "Error: $error"
    }

    private fun showLoading() {
        checkAndEmitStatusLoading(true)
    }

    private fun hideLoading() {
        checkAndEmitStatusLoading(false)
    }

}


