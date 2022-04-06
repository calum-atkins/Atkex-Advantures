package com.example.atkex

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.*

class PointsOfInterestActivity : AppCompatActivity() {

    private lateinit var recyclerView : RecyclerView
    private lateinit var poiArrayList : ArrayList<PointsOfInterestModel>
    private lateinit var collectionIDList : ArrayList<String>
    private lateinit var myAdapter : PointsOfInterestAdapter
    private lateinit var db : FirebaseFirestore

    private lateinit var userDocumentID : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_points_of_interest)

        val extras = intent.extras
        var userDocumentID = ""

        if (extras != null) {
            userDocumentID = extras.getString("userDocumentID") as String
        }

        recyclerView = findViewById(R.id.recycler_view_list_poi)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)

        poiArrayList = arrayListOf()
        collectionIDList = arrayListOf()
        myAdapter = PointsOfInterestAdapter(this@PointsOfInterestActivity, poiArrayList, collectionIDList, userDocumentID)
        recyclerView.adapter = myAdapter

        EventChangeListener()

        val newToolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.main_toolbar)
        setSupportActionBar(newToolbar)

        supportActionBar?.apply {
            setDisplayShowHomeEnabled(true)
            setDisplayHomeAsUpEnabled(true)
        }

    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun EventChangeListener() {
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
                            collectionIDList.add(dc.document.id)
                            poiArrayList.add(dc.document.toObject(PointsOfInterestModel::class.java))

                        }
                    }

                    myAdapter.notifyDataSetChanged()
                }
            })
    }

}