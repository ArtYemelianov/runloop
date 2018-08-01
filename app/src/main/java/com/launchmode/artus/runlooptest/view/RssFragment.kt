package com.launchmode.artus.runlooptest.view

import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.launchmode.artus.runlooptest.R
import com.launchmode.artus.runlooptest.databinding.RssLayoutBinding
import com.launchmode.artus.runlooptest.viewmodel.RssViewModel
import kotlinx.android.synthetic.main.rss_layout.*


class RssFragment : Fragment() {

    lateinit var rssViewModel: RssViewModel

    override fun onCreateView(aInflater: LayoutInflater, aContainer: ViewGroup?, aSavedInstanceState: Bundle?): View? {
        var binding = DataBindingUtil.inflate<RssLayoutBinding>( aInflater, R.layout.rss_layout, aContainer, false)
        rssViewModel= ViewModelProviders.of(this).get(RssViewModel::class.java!!)
        binding.rssViewModel = rssViewModel
        return binding.root
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        initViews()
        rssViewModel.subscribe(this)
    }


    /**
     * Initializes views
     */
    private fun initViews() {
        tabhost.setup()
        tabhost.addTab(tabhost.newTabSpec("first")
                .setIndicator("first", null)
                .setContent(list_1.id))
        tabhost.addTab(tabhost.newTabSpec("second")
                .setIndicator("second", null)
                .setContent(list_2.id))

        //inits recycle views
        initRecycleView(view!!.findViewById(R.id.list_1))
        initRecycleView(view!!.findViewById(R.id.list_2))
    }

    /**
     * Initializes recycle view
     */
    private fun initRecycleView(recycleView: RecyclerView){
        recycleView.setHasFixedSize(true)
        val llm = LinearLayoutManager(context)
        recycleView.layoutManager = llm
        val divider = DividerItemDecoration(context, llm.orientation)
        recycleView.addItemDecoration(divider)
    }

}

