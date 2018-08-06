package com.launchmode.artus.runlooptest.model

import android.arch.lifecycle.MutableLiveData
import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import com.artus.rssreader.Article
import com.artus.rssreader.Parser
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledFuture
import java.util.concurrent.TimeUnit
import kotlin.collections.ArrayList

class RssModel {

    val bussinesNews: MutableLiveData<List<RssEntry>> = MutableLiveData()
    var otherNews: MutableLiveData<List<RssEntry>> = MutableLiveData()
    var selectedItem: MutableLiveData<String> = MutableLiveData()
    var isLoading: MutableLiveData<Boolean> = MutableLiveData()

    private var future: ScheduledFuture<*>? = null
    private val scheduler = Executors.newSingleThreadScheduledExecutor()

    init {
        bussinesNews.value = ArrayList()
        isLoading.value = true
        Handler(Looper.getMainLooper()).postAtTime({
            isLoading.value = false
        }, SystemClock.uptimeMillis() + 5000)

        val parser = Parser()
        parser.execute("http://feeds.reuters.com/reuters/businessNews")
        parser.onFinish(object : Parser.OnTaskCompleted {
            //what to do when the parsing is done
            override fun onTaskCompleted(list: ArrayList<Article>) {
                var rssList = list.map { item ->
                    RssEntry(item.title,
                            item.description,
                            SimpleDateFormat("dd/M/yyyy hh:mm:ss").format(item.pubDate))
                }.toList()
                bussinesNews.value = rssList
            }

            //what to do in case of error
            override fun onError() {

            }
        })
    }

    private fun scheduleNext() {
        future = scheduler.scheduleAtFixedRate({
            android.os.Handler(Looper.getMainLooper()).post({
                val list = bussinesNews.value!!
                val rss = RssEntry("Title",
                        "Description",
                        SimpleDateFormat("dd/M/yyyy hh:mm:ss").format(Date()))
                bussinesNews.value = list
            })
        }, 1000, 5000, TimeUnit.MILLISECONDS)
    }


}