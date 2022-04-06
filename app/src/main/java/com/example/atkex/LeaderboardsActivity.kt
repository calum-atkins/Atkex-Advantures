package com.example.atkex

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.*

class LeaderboardsActivity : AppCompatActivity() {

    private lateinit var recyclerView : RecyclerView
    private lateinit var usersArrayList : ArrayList<LeaderboardsModel>
    private lateinit var myAdapter : LeaderboardsAdapter
    private lateinit var db : FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_leaderboards)




        /** Initialise list and populate */
//        val imageModelArrayList = populateList()
//        val recyclerView = findViewById<View>(R.id.recycler_view_list_leaderboards) as RecyclerView
//
//        val layoutManager = LinearLayoutManager(this)
//        recyclerView.layoutManager = layoutManager
//        val adapter = LeaderboardsAdapter(imageModelArrayList)
//        recyclerView.adapter = adapter

        recyclerView = findViewById(R.id.recycler_view_list_leaderboards)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)

        usersArrayList = arrayListOf()
        myAdapter = LeaderboardsAdapter(this@LeaderboardsActivity, usersArrayList)
        recyclerView.adapter = myAdapter

        EventChangeListener()

        val newToolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.main_toolbar)
        setSupportActionBar(newToolbar)

        supportActionBar?.apply {
            setDisplayShowHomeEnabled(true)
            setDisplayHomeAsUpEnabled(true)
        }
    }

    private fun EventChangeListener() {
        db = FirebaseFirestore.getInstance()
        db.collection("users")//.orderBy("points", Query.Direction.DESCENDING)
            .addSnapshotListener(object : EventListener<QuerySnapshot> {
                override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {
                    if (error != null) {
                        Log.e("Firebase error", error.message.toString())
                        return
                    }
                    for (dc : DocumentChange in value?.documentChanges!!){
                        if (dc.type == DocumentChange.Type.ADDED) {
                            usersArrayList.add(dc.document.toObject(LeaderboardsModel::class.java))

                        }
                    }

                    myAdapter.notifyDataSetChanged()
                }
            })
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}