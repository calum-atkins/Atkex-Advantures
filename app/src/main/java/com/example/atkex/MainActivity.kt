package com.example.atkex

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


    }

    fun onClickLeaderboards(view: View) {
        val newIntent = Intent(this, LeaderboardsActivity::class.java)
        startActivity(newIntent)
    }

    fun onClickSettings(view: View) {
        val newIntent = Intent(this, SettingsActivity::class.java)
        startActivity(newIntent)
    }

    fun onClickPointsOfInterest(view: View) {
        val newIntent = Intent(this, SettingsActivity::class.java)
        startActivity(newIntent)
    }
}