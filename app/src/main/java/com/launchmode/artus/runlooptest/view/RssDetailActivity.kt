package com.launchmode.artus.runlooptest.view

import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import com.launchmode.artus.runlooptest.R
import com.launchmode.artus.runlooptest.databinding.InfoRssLayoutBinding
import com.launchmode.artus.runlooptest.model.RssEntry
import com.launchmode.artus.runlooptest.viewmodel.RssDetailViewModel
import com.launchmode.artus.runlooptest.viewmodel.RssDetailViewModelFactory


class RssDetailActivity : AppCompatActivity() {

    private lateinit var detailViewModel: RssDetailViewModel
    private lateinit var rssEntry: RssEntry

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setHomeButtonEnabled(true)

        rssEntry = intent.getParcelableExtra("entry")
        title = rssEntry.title

        val binding = DataBindingUtil.setContentView<InfoRssLayoutBinding>(this, R.layout.info_rss_layout)
        detailViewModel = ViewModelProviders.of(this,
                RssDetailViewModelFactory.getInstance(application, rssEntry)
        ).get(RssDetailViewModel::class.java)
        binding.viewModel = detailViewModel
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (android.R.id.home == item.itemId) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onDestroy() {
        super.onDestroy()
        RssDetailViewModelFactory.destroyInstance()
    }
}