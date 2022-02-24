package com.example.atkex

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class LeaderboardsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_leaderboards)

        //actionbar
        val actionbar = supportActionBar
        //set actionbar title
        actionbar!!.title = "Leaderboards"
        //set back button
        actionbar.setDisplayHomeAsUpEnabled(true)
        actionbar.setDisplayHomeAsUpEnabled(true)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}