package com.launchmode.artus.runlooptest.datasource.webservice

import android.arch.lifecycle.MediatorLiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Observer
import android.util.Log
import com.launchmode.artus.runlooptest.datasource.RssRepository
import com.launchmode.artus.runlooptest.datasource.runtime.IRssStorage
import com.launchmode.artus.runlooptest.model.RssEntry
import com.launchmode.artus.runlooptest.scheduler.RequestRepeatTimer
import com.launchmode.artus.runlooptest.utils.AppExecutors
import com.launchmode.artus.runlooptest.utils.FakeTimerDelay
import com.launchmode.artus.runlooptest.utils.ResourceObserver
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
    private val mediatorObserver = Observer<List<RssEntry>> {
        other.value = otherData
    }
    private val businessObserver = Observer<List<RssEntry>> {
        business.value = it
    }

    private val resourceObserver = ResourceObserver<List<RssEntry>>("RestaurantsMapActivity",
            hideLoading = ::hideLoading,
            showLoading = ::showLoading,
            onSuccess = null,
            onError = ::showErrorMessage)

    init {
        storage.queryRssByUrl(URL_BUSINESS_NEWS).observeForever(businessObserver)

        val callback = Observer<List<RssEntry>> {
            _other.value = it
        }

        _other.addSource(storage.queryRssByUrl(URL_ENTERTAINMENT), callback)
        _other.addSource(storage.queryRssByUrl(URL_ENVIRONMENT), callback)
        _other.observeForever(mediatorObserver)
    }


    /**
     * A combined data for entertaiment and envirmoment
     */
    private val otherData: List<RssEntry>
        get() {
            val list = ArrayList<RssEntry>()
            list.addAll(storage.queryRssByUrl(URL_ENVIRONMENT).value ?: emptyList())
            list.addAll(storage.queryRssByUrl(URL_ENTERTAINMENT).value ?: emptyList())
            return list
        }


    private val businessTimer: RequestRepeatTimer  by lazy {
        val timer = createTimer(URL_BUSINESS_NEWS)
        timer.result.observeForever(resourceObserver)
        timer
    }
    private val entertainmentTimer: RequestRepeatTimer by lazy {
        val timer = createTimer(URL_ENVIRONMENT)
        timer.result.observeForever(resourceObserver)
        timer
    }
    private val environnementTimer: RequestRepeatTimer by lazy {
        val timer = createTimer(URL_ENTERTAINMENT)
        timer.result.observeForever(resourceObserver)
        timer
    }

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
        return RequestRepeatTimer(url, repository)
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

        _other.removeSource(storage.queryRssByUrl(URL_ENTERTAINMENT))
        _other.removeSource(storage.queryRssByUrl(URL_ENVIRONMENT))
        _other.removeObserver(mediatorObserver)
        storage.queryRssByUrl(URL_ENVIRONMENT).removeObserver(businessObserver)

        businessTimer.result.removeObserver(resourceObserver)
        entertainmentTimer.result.removeObserver(resourceObserver)
        environnementTimer.result.removeObserver(resourceObserver)


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


