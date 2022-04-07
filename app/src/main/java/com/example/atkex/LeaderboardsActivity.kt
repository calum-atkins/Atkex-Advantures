package com.example.atkex

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.*

/**
 * Class used for the leaderboards activity screen
 */
class LeaderboardsActivity : AppCompatActivity() {

    //Initialise variables
    private lateinit var recyclerView : RecyclerView
    private lateinit var usersArrayList : ArrayList<LeaderboardsModel>
    private lateinit var myAdapter : LeaderboardsAdapter
    private lateinit var db : FirebaseFirestore

    /**
     * Method called on activity start
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_leaderboards)

        //Initialise recycler view
        recyclerView = findViewById(R.id.recycler_view_list_leaderboards)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)

        //Initialise adapter
        usersArrayList = arrayListOf()
        myAdapter = LeaderboardsAdapter(this@LeaderboardsActivity, usersArrayList)
        recyclerView.adapter = myAdapter

        EventChangeListener()

        //Initialise toolbar
        val newToolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.main_toolbar)
        setSupportActionBar(newToolbar)

        supportActionBar?.apply {
            setDisplayShowHomeEnabled(true)
            setDisplayHomeAsUpEnabled(true)
        }
    }

    /**
     * Method to populate the array list from the database
     */
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

    /**
     * Method to go to previous activity
     */
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}