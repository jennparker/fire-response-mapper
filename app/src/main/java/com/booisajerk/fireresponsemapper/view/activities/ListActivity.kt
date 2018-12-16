package com.booisajerk.fireresponsemapper.view.activities

import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.widget.LinearLayoutManager
import android.view.MenuItem
import com.booisajerk.fireresponsemapper.R
import com.booisajerk.fireresponsemapper.presenter.IncidentPresenter
import com.booisajerk.fireresponsemapper.view.adapters.IncidentAdapter
import kotlinx.android.synthetic.main.activity_list.*
import kotlinx.android.synthetic.main.app_bar_list.*
import kotlinx.android.synthetic.main.content_list.*
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

class ListActivity : BaseActivity(), NavigationView.OnNavigationItemSelectedListener {

    val incidentAdapter = IncidentAdapter()
    val incidentPresenter = IncidentPresenter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list)
        setSupportActionBar(toolbar)

        recycler_view.setHasFixedSize(true)
        recycler_view.layoutManager = LinearLayoutManager(this)

        initAdapter()

        val toggle = ActionBarDrawerToggle(
            this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)
    }

    private fun requestIncidents(){
        val subscription = incidentPresenter.getIncidents()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe ({ retrievedIncidents ->
                (recycler_view.adapter as IncidentAdapter).setDataSource(retrievedIncidents)
            },
                {e ->
                    e.printStackTrace()
                })
        subscriptions.add(subscription)
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
            R.id.nav_camera -> {
                // Handle the camera action
            }
            R.id.nav_gallery -> {

            }
            R.id.nav_slideshow -> {

            }
            R.id.nav_manage -> {

            }
            R.id.nav_share -> {

            }
            R.id.nav_send -> {

            }
        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    fun initAdapter() {
        if (recycler_view.adapter == null) {
            recycler_view.adapter = incidentAdapter

        }
    }
}
