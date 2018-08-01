package com.launchmode.artus.runlooptest.view

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.launchmode.artus.runlooptest.R
import com.launchmode.artus.runlooptest.databinding.InfoLayoutBinding
import com.launchmode.artus.runlooptest.viewmodel.InfoViewModel
import kotlin.jvm.java

class InfoFragment: Fragment() {

    lateinit var infoViewModel: InfoViewModel

    override fun onCreateView(aInflater: LayoutInflater, aContainer: ViewGroup?, aSavedInstanceState: Bundle?): View? {
        var binding = DataBindingUtil.inflate<InfoLayoutBinding>( aInflater, R.layout.info_layout, aContainer, false)
        infoViewModel = ViewModelProviders.of(this).get(InfoViewModel::class.java!!)
        binding.infoViewModel = infoViewModel
        return binding.root
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        infoViewModel.getDate().observe(this, Observer<String> {
            infoViewModel.date.set(it)
        })

        infoViewModel.getSelectedItem().observe(this, Observer<String> {
            infoViewModel.selectedItem.set(it)
        })
    }

}