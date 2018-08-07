package com.launchmode.artus.runlooptest.data

import com.artus.rssreader.Article
import com.artus.rssreader.XMLParser

/**
 * Parses rss response into pretty format
 */
internal class Parser(private val str: String) {

    private val _xmlParser: XMLParser = XMLParser()


    /**
     * Parses str into list of articles
     */
    fun parse(): List<Article>? {

        try {
            val list = _xmlParser.parseXML(str)
            return list
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }
    }
}