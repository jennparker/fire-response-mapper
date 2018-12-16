package com.booisajerk.fireresponsemapper.view.activities

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import com.booisajerk.fireresponsemapper.R
import rx.subscriptions.CompositeSubscription

abstract class BaseActivity : AppCompatActivity() {

    var subscriptions = CompositeSubscription()

    override fun onResume() {
        super.onResume()
        subscriptions = CompositeSubscription()
    }

    override fun onPause() {
        super.onPause()
        if (!subscriptions.isUnsubscribed) {
            subscriptions.unsubscribe()
        }
        subscriptions.clear()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.list, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_list -> {
                val listIntent = Intent(this, ListActivity::class.java)
                startActivity(listIntent)
                true
            }

            R.id.action_map -> {
                val mapIntent = Intent(this, MapsActivity::class.java)
                startActivity(mapIntent)
                true
            }

            R.id.action_about -> true
            //TODO add about activity
            else -> super.onOptionsItemSelected(item)
        }
    }
}