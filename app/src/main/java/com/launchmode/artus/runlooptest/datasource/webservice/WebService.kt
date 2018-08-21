package com.launchmode.artus.runlooptest.datasource.webservice

import com.launchmode.artus.runlooptest.model.RssEntry
import com.launchmode.artus.runlooptest.utils.Parser
import okhttp3.OkHttpClient
import okhttp3.Request

class WebService(private val url: String) {

    private val mClient = OkHttpClient()
    /**
     * Retrieves newest data from server
     */
    fun execute(): List<RssEntry>? {
        val request = Request.Builder()
                .url(url)
                .build()
        val response = mClient.newCall(request).execute()
        val body = response.body()?.string() ?: return null
        val list = Parser(body!!).parse() ?: return null
        return list.map { item ->
            RssEntry(item.title,
                    item.description,
                    item.pubDate.time)
        }.toList()
    }
}