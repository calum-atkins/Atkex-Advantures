package com.example.atkex

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.firestore.*


class MainActivity : AppCompatActivity(), OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private lateinit var mapFragment : SupportMapFragment
    private lateinit var googleMap : GoogleMap
    private lateinit var client : FusedLocationProviderClient
    private lateinit var lastLocation : Location
    private lateinit var userDocumentID : String

    private lateinit var db : FirebaseFirestore
    private lateinit var currentLocation : LatLng

    companion object{
        private const val LOCATION_REQUEST_CODE = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val extras = intent.extras
        userDocumentID = ""

        if (extras != null) {
            userDocumentID = extras.getString("documentID") as String
        }

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
                newIntent.putExtra("userDocumentID", userDocumentID)
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
                newIntent.putExtra("userDocumentID", userDocumentID)
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

         googleMap.setOnMarkerClickListener(OnMarkerClickListener { marker ->
             val markerName = marker.title
             val markerPosition = marker.position

             val distance = FloatArray(1)
             Location.distanceBetween(
                 markerPosition.latitude, markerPosition.longitude,
                 currentLocation.latitude, currentLocation.longitude, distance
             )

             val radiusInMeters = 50.0 //1 km = 1000 m (km * m)

             if( distance[0] > radiusInMeters ){
                 Toast.makeText(getBaseContext(),
                     markerPosition.toString(),
                     Toast.LENGTH_LONG).show();
             } else {

                 val pointOfInterest: MutableMap<String, Any> = HashMap()
                 pointOfInterest["name"] = markerName.toString()

                 db = FirebaseFirestore.getInstance()

                 val rootRef = FirebaseFirestore.getInstance()
                 val allUsersRef = rootRef.collection("users/$userDocumentID/visited")
                 val userNameQuery = allUsersRef.whereEqualTo("name", markerName)
                 userNameQuery.get().addOnCompleteListener { task ->
                     if (task.isSuccessful) {
                         var unvisited: Boolean = true
                         for (document in task.result) {
                             if (document.exists()) {
                                unvisited = false;
                                 Toast.makeText(getBaseContext(),
                             "You have already visited " + markerName ,
                                Toast.LENGTH_LONG).show()

                             }
                         }
                         if (unvisited) {
                             db.collection("users/$userDocumentID/visited")
                                 .add(pointOfInterest)
                                 .addOnSuccessListener {
                                     Toast.makeText(getBaseContext(),
                                         "New POI discovered " + markerName ,
                                         Toast.LENGTH_LONG).show()
                                 }
                             val docRef = db.collection("users").document(userDocumentID)
                                 .get()
                                 .addOnSuccessListener { document ->
                                     if (document != null) {
                                         var points = document.getLong("points")
                                         points = points?.plus(1)
                                         updatePoints(userDocumentID, points!!)
                                     }
                                 }
                         }
                     } else {
                         Log.d("TAG", "Error getting documents: ", task.exception)
                     }
                 }
             }
             false
         })

         setupMap()
    }

    private fun updatePoints(id: String, points: Long) {
        db = FirebaseFirestore.getInstance()
        db.collection("users").document(userDocumentID).update("points", points)
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
                currentLocation = currentLatLong
                //placeMarkerOnMap(currentLatLong, "You are here")
                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLong, 12f))
                addMarkers()
            }
        }
    }

    private fun addMarkers() {
        db = FirebaseFirestore.getInstance()
        db.collection("points_of_interests")
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