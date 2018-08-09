package com.launchmode.artus.runlooptest.view

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import com.launchmode.artus.runlooptest.R
import com.launchmode.artus.runlooptest.databinding.InfoRssLayoutBinding
import com.launchmode.artus.runlooptest.model.RssEntry
import com.launchmode.artus.runlooptest.viewmodel.RssDetailViewModel


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
        detailViewModel = RssDetailViewModel(application, rssEntry)
        binding.viewModel = detailViewModel
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (android.R.id.home == item.itemId) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}