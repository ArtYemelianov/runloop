package com.launchmode.artus.runlooptest

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

class InfoFragment: Fragment() {


    override fun onCreateView(aInflater: LayoutInflater, aContainer: ViewGroup?, aSavedInstanceState: Bundle?): View? {
        return aInflater.inflate(R.layout.info_layout, aContainer, false)
    }
}