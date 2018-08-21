package com.launchmode.artus.runlooptest.datasource.webservice

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
            return _xmlParser.parseXML(str)
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }
    }
}