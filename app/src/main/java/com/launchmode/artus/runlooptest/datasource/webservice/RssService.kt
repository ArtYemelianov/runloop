package com.launchmode.artus.runlooptest.datasource.webservice

import com.launchmode.artus.runlooptest.model.RssEntry
import com.launchmode.artus.runlooptest.scheduler.RepeatTimer
import com.launchmode.artus.runlooptest.scheduler.RequestRepeatTimer
import com.launchmode.artus.runlooptest.utils.FakeTimerDelay
import com.launchmode.artus.runlooptest.utils.Utils
import java.util.concurrent.atomic.AtomicInteger


class RssService private constructor() {

    companion object {
        const val URL_BUSINESS_NEWS = "http://feeds.reuters.com/reuters/businessNews"
        const val URL_ENVIRONMENT = "http://feeds.reuters.com/reuters/environment"
        const val URL_ENTERTAINMENT = "http://feeds.reuters.com/reuters/entertainment"

        fun create(): RssService {
            return RssService()
        }

        interface RssServiceCallback {
            fun onTaskCycleChanged(isLoading: Boolean)
            fun onBusinessDataChanged(list: List<RssEntry>)
            fun onOtherChanged(list: List<RssEntry>)
        }

    }

    var delegate: RssServiceCallback? = null

    var isDestroyed: Boolean = false
        private set
    /**
     * A new instance of business data
     */
    val businessData: List<RssEntry>
        get() {
            return _businessList.toList()
        }

    /**
     * A combined data for entertaiment and envirmoment
     */
    val otherData: List<RssEntry>
        get() {
            val list = ArrayList<RssEntry>()
            list.addAll(_environnementList.toList())
            list.addAll(_entertainmentList.toList())
            return list
        }


    private val _businessList: ArrayList<RssEntry> = ArrayList()
    private val _environnementList: ArrayList<RssEntry> = ArrayList()
    private val _entertainmentList: ArrayList<RssEntry> = ArrayList()

    private val businessTimer: RequestRepeatTimer  by lazy { createTimer(URL_BUSINESS_NEWS) }
    private val entertainmentTimer: RequestRepeatTimer by lazy { createTimer(URL_ENTERTAINMENT) }
    private val environnementTimer: RequestRepeatTimer by lazy { createTimer(URL_ENVIRONMENT) }

    private val counter: AtomicInteger = AtomicInteger(0)
    private val fakeTimer = FakeTimerDelay()

    init {

        fakeTimer.delegate = object : FakeTimerDelay.Companion.Callback {
            override fun onChanged(value: Boolean) {
                delegate?.onTaskCycleChanged(value)
            }

        }
        businessTimer.delegate = object : RequestRepeatTimer.Companion.Callback {
            override fun onCompleted(list: List<RssEntry>) {
                Utils.fatalErrorIfNotMainThread()
                _businessList.clear()
                _businessList.addAll(list)
                delegate?.onBusinessDataChanged(businessData)
            }
        }

        entertainmentTimer.delegate = object : RequestRepeatTimer.Companion.Callback {
            override fun onCompleted(list: List<RssEntry>) {
                Utils.fatalErrorIfNotMainThread()
                _entertainmentList.clear()
                _entertainmentList.addAll(list)
                delegate?.onOtherChanged(otherData)
            }
        }

        environnementTimer.delegate = object : RequestRepeatTimer.Companion.Callback {
            override fun onCompleted(list: List<RssEntry>) {
                Utils.fatalErrorIfNotMainThread()
                _environnementList.clear()
                _environnementList.addAll(list)
                delegate?.onOtherChanged(otherData)
            }
        }
    }

    private fun createTimer(url: String): RequestRepeatTimer {
        val timer = RequestRepeatTimer(url, null, object : RepeatTimer.Companion.TaskCycleCallback {
            override fun onChange(status: Boolean) {
                checkAndEmitStatusLoading(status)
            }
        })
        return timer
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
        isDestroyed = true


    }

}


