package com.example.atkex

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment

class MainActivity : AppCompatActivity() {

    lateinit var mapFragment : SupportMapFragment
    lateinit var googleMap : GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(OnMapReadyCallback {
            googleMap = it
        })
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