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
import java.text.SimpleDateFormat
import java.util.*

class InfoFragment : Fragment() {

    lateinit var infoViewModel: InfoViewModel
    lateinit var binding: InfoLayoutBinding

    override fun onCreateView(aInflater: LayoutInflater,
                              aContainer: ViewGroup?,
                              aSavedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(aInflater,
                R.layout.info_layout,
                aContainer,
                false)
        infoViewModel = ViewModelProviders.of(this).get(InfoViewModel::class.java!!)
        binding.infoViewModel = infoViewModel
        return binding.root
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        infoViewModel.model.date.observe(this, Observer {
            val currentDate = SimpleDateFormat("dd/M/yyyy hh:mm:ss")
            binding.infoDate.text = currentDate.format(Date(it!!))
        })
        infoViewModel.model.selectedItem.observe(this, Observer<String> {
            binding.infoSelectedItem.text = it
        })
    }

}