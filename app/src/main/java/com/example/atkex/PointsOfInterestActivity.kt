package com.example.atkex

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.*
import org.w3c.dom.Document

class PointsOfInterestActivity : AppCompatActivity() {

    private lateinit var recyclerView : RecyclerView
    private lateinit var poiArrayList : ArrayList<PointsOfInterestModel>
    private lateinit var myAdapter : PointsOPfInterestAdapter
    private lateinit var db : FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_points_of_interest)


//        val imageModelArrayList = populateList()
//
//        val recyclerView = findViewById<View>(R.id.recycler_view_list_poi) as RecyclerView
//
//        val layoutManager = LinearLayoutManager(this)
//        recyclerView.layoutManager = layoutManager
//        val adapter = PointsOPfInterestAdapter(imageModelArrayList)
//        recyclerView.adapter = adapter

        recyclerView = findViewById(R.id.recycler_view_list_poi)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)

        poiArrayList = arrayListOf()
        myAdapter = PointsOPfInterestAdapter(this@PointsOfInterestActivity, poiArrayList)
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
                            poiArrayList.add(dc.document.toObject(PointsOfInterestModel::class.java))

                        }
                    }

                    myAdapter.notifyDataSetChanged()
                }
            })
    }


//    private fun populateList(): ArrayList<PointsOfInterestModel> {
//        val list = ArrayList<PointsOfInterestModel>()
//        val myImageList = arrayOf(R.drawable.gower_peninsula, R.drawable.mumbles_pier)
//
//        val distancesList = arrayOf(5, 9)
//        val myImageNameList = arrayOf(R.string.gower_peninsula, R.string.mumbles_pier)
//
//        for (i in 0..1) {
//            val imageModel = PointsOfInterestModel()
//            imageModel.setNames(getString(myImageNameList[i]))
//            imageModel.setDistances(distancesList[i])
//            imageModel.setImages(myImageList[i])
//            list.add(imageModel)
//        }
//        list.sortBy { list -> list.name }
//        return list
//    }

}