package com.example.atkex

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.*
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import android.Manifest
import android.content.pm.PackageManager
import android.hardware.camera2.CameraAccessException
import android.location.Location
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions


class MainActivity : AppCompatActivity(), OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private lateinit var mapFragment : SupportMapFragment
    private lateinit var googleMap : GoogleMap
    private lateinit var client : FusedLocationProviderClient
    private lateinit var lastLocation : Location

    companion object{
        private const val LOCATION_REQUEST_CODE = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Custom image for action bar
        supportActionBar?.apply {
            setDisplayShowHomeEnabled(true)
            setDisplayUseLogoEnabled(true)
            setLogo(R.drawable.atkex_logo_dark)
        }

        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        //Initialise location
        client = LocationServices.getFusedLocationProviderClient(this)

    }

     override fun onMapReady(_googleMap: GoogleMap) {
         googleMap = _googleMap

         googleMap.uiSettings.isZoomControlsEnabled = true
         googleMap.setOnMarkerClickListener(this)
         setupMap()
    }

    private fun setupMap() {
        if (ActivityCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_REQUEST_CODE)
            return
        }
        googleMap.isMyLocationEnabled = true
        client.lastLocation.addOnSuccessListener(this) { location ->
            if (location != null) {
                lastLocation = location
                val currentLatLong = LatLng(location.latitude, location.longitude)
                placeMarkerOnMap(currentLatLong)
                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLong, 12f))
            }
        }
    }

    private fun placeMarkerOnMap(currentLatLong: LatLng) {
        val markerOptions = MarkerOptions().position(currentLatLong)
        markerOptions.title("$currentLatLong")
        googleMap.addMarker(markerOptions)
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
        val newIntent = Intent(this, PointsOfInterestActivity::class.java)
        startActivity(newIntent)
    }

    override fun onMarkerClick(p0: Marker) = false
}