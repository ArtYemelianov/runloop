package com.launchmode.artus.runlooptest

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

class RssFragment: Fragment() {


    override fun onCreateView(aInflater: LayoutInflater, aContainer: ViewGroup?, aSavedInstanceState: Bundle?): View? {
        val view: View = aInflater.inflate(R.layout.rss_layout, aContainer, false)
        return view
    }
}