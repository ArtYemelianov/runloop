package com.launchmode.artus.runlooptest

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import android.support.annotation.MainThread
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ProgressBar
import kotlinx.android.synthetic.main.rss_layout.*


class RssFragment : Fragment() {


    override fun onCreateView(aInflater: LayoutInflater, aContainer: ViewGroup?, aSavedInstanceState: Bundle?): View? {
        return aInflater.inflate(R.layout.rss_layout, aContainer, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tabhost.setOnTabChangedListener({ tabId ->
            onTabChanged(tabId)
        })

        tabhost.setup()
        tabhost.addTab(tabhost.newTabSpec("first")
                .setIndicator("first", null)
                .setContent(list_1.id))
        tabhost.addTab(tabhost.newTabSpec("second")
                .setIndicator("second", null)
                .setContent(list_2.id))
        Handler(Looper.getMainLooper()).postAtTime({
            progress_bar.visibility = View.GONE
        }, SystemClock.uptimeMillis()+5000)


    }

    private fun onTabChanged(index: String) {
        if (index == "first") {
            list_1.adapter = ArrayAdapter(context, android.R.layout.simple_list_item_1, arrayOf("One", "Two"))
        } else {
            list_2.adapter = ArrayAdapter(context, android.R.layout.simple_list_item_1, arrayOf("One", "Two", "Three"))
        }
    }

}

