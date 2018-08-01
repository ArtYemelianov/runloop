package com.launchmode.artus.runlooptest.model

class RssEntry {

    val title: String
    val description: String
    val date: String

    constructor(title: String, description: String, date: String) {
        this.title = title
        this.description = description
        this.date = date
    }

}