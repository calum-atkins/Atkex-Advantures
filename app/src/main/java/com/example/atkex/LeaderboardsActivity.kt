package com.example.atkex

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class LeaderboardsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_leaderboards)

        /** Action bar and settings */
//        val actionbar = supportActionBar
//        actionbar!!.title = "Leaderboards"
//        actionbar.setDisplayHomeAsUpEnabled(true)
//        actionbar.setDisplayHomeAsUpEnabled(true)

        /** Initialise list and populate */
        val imageModelArrayList = populateList()
        val recyclerView = findViewById<View>(R.id.recycler_view_list_leaderboards) as RecyclerView

        val layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager
        val adapter = LeaderboardsAdapter(imageModelArrayList)
        recyclerView.adapter = adapter
    }

    /**
     * Function created to fill the list with data
     */
    private fun populateList(): ArrayList<LeaderboardsModel> {
        val list = ArrayList<LeaderboardsModel>()

        val namesList = arrayOf("James", "Steph")
        val pointsList = arrayOf(5, 9)

        for (i in 0..1) {
            val imageModel = LeaderboardsModel()
            imageModel.setNames(namesList[i])
            imageModel.setPoints(pointsList[i])
            list.add(imageModel)
        }
        list.sortBy { list -> list.name }
        return list
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}