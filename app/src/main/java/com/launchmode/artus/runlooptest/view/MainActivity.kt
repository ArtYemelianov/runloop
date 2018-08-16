package com.launchmode.artus.runlooptest.view

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import com.launchmode.artus.runlooptest.R
import com.launchmode.artus.runlooptest.viewmodel.MainViewModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)
        viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java!!)
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        val fr = when (viewModel.indexFragment) {
            0 -> InfoFragment()
            1 -> RssFragment()
            else -> {
                throw IllegalArgumentException("Index ${viewModel.indexFragment} " +
                        "of fragment not exists")
            }
        }
        supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_main, fr, "fragment")
                .commit()
        val item = if (viewModel.indexFragment == 0) R.id.nav_info else R.id.nav_rss
        nav_view.setCheckedItem(item)
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.nav_info -> {
                supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_main, InfoFragment(), "fragment")
                        .commit()
                viewModel.indexFragment = 0
            }
            R.id.nav_rss -> {
                supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_main, RssFragment(), "fragment")
                        .commit()
                viewModel.indexFragment = 1
            }
        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }
}
