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
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.*


class MainActivity : AppCompatActivity(), OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private lateinit var mapFragment : SupportMapFragment
    private lateinit var googleMap : GoogleMap
    private lateinit var client : FusedLocationProviderClient
    private lateinit var lastLocation : Location
    private lateinit var userName : String

    private lateinit var db : FirebaseFirestore


    companion object{
        private const val LOCATION_REQUEST_CODE = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val newToolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.main_toolbar)
        setSupportActionBar(newToolbar)

        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        //Initialise location
        client = LocationServices.getFusedLocationProviderClient(this)

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_layout, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val myView = findViewById<View>(R.id.main_toolbar)

        when (item.itemId) {
            R.id.POI_list_but -> {
                val newIntent = Intent(this, PointsOfInterestActivity::class.java)
                startActivity(newIntent)
                return true
            }
            R.id.leaderboards_but -> {
                val newIntent = Intent(this, LeaderboardsActivity::class.java)
                startActivity(newIntent)
                return true
            }
            R.id.settings_but -> {
                val newIntent = Intent(this, SettingsActivity::class.java)
                startActivity(newIntent)
                return true
            }
            R.id.logout_but -> {
                val newIntent = Intent(this, LoginActivity::class.java)
                startActivity(newIntent)
                return true
            }
        }


        return super.onOptionsItemSelected(item)
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
                //placeMarkerOnMap(currentLatLong, "You are here")
                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLong, 12f))
                addMarkers()
            }
        }
    }

    private fun addMarkers() {
        db = FirebaseFirestore.getInstance()
        db.collection("points_of_interests")//.orderBy("distance", Query.Direction.ASCENDING)
            .addSnapshotListener(object : EventListener<QuerySnapshot> {
                override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {
                    if (error != null) {
                        Log.e("Firebase error", error.message.toString())
                        return
                    }
                    for (dc : DocumentChange in value?.documentChanges!!){
                        if (dc.type == DocumentChange.Type.ADDED) {
                            val latLong = LatLng(dc.document.toObject(PointsOfInterestModel::class.java).lat.toString().toDouble(),
                                dc.document.toObject(PointsOfInterestModel::class.java).long.toString().toDouble())
                            placeMarkerOnMap(latLong, dc.document.toObject(PointsOfInterestModel::class.java).name.toString())

                        }
                    }
                }
            })
    }

    private fun placeMarkerOnMap(currentLatLong: LatLng, name: String) {
        val markerOptions = MarkerOptions().position(currentLatLong)
        markerOptions.title("$name")
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