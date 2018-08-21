package com.launchmode.artus.runlooptest.scheduler

import android.os.Looper
import com.launchmode.artus.runlooptest.utils.Parser
import com.launchmode.artus.runlooptest.model.RssEntry
import okhttp3.OkHttpClient
import okhttp3.Request
import java.text.SimpleDateFormat

/**
 * Presents repeated timer for rss request
 */
class RequestRepeatTimer(private val url: String,
                         var delegate: RequestRepeatTimer.Companion.Callback?,
                         cycleDelegate: TaskCycleCallback?) : RepeatTimer(cycleDelegate) {


    companion object {
        /**
         * Callback
         */
        interface Callback {
            /**
             * Request done
             * @param list Result
             */
            fun onCompleted(list: List<RssEntry>)
        }
    }

    private val mClient = OkHttpClient()

    override fun execute() {
        val request = Request.Builder()
                .url(url)
                .build()

        try {
            val response = mClient.newCall(request).execute()
            val body = response.body()?.string()
            if (body == null) {
                sendCallback(emptyList())
                return
            }
            val list = Parser(body!!).parse() ?: return

            var rssList = list.map { item ->
                RssEntry(item.title,
                        item.description,
                        SimpleDateFormat("dd/M/yyyy hh:mm:ss").format(item.pubDate))
            }.toList()
            sendCallback(rssList)
        } catch (e: Exception) {
            e.printStackTrace()
            sendCallback(emptyList())
        }
    }

    /**
     * Sends result into main thread
     */
    private fun sendCallback(list: List<RssEntry>) {
        if (Thread.currentThread().id == Looper.getMainLooper().thread.id) {
            delegate?.onCompleted(list)
        } else {
            mHandler.post({
                delegate?.onCompleted(list)
            })
        }
    }
}