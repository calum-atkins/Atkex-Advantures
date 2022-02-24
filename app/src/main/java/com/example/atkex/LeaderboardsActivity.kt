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

        //actionbar
        val actionbar = supportActionBar
        //set actionbar title
        actionbar!!.title = "Leaderboards"
        //set back button
        actionbar.setDisplayHomeAsUpEnabled(true)
        actionbar.setDisplayHomeAsUpEnabled(true)

        val imageModelArrayList = populateList()

        val recyclerView = findViewById<View>(R.id.recycler_view_list_leaderboards) as RecyclerView

        val layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager
        val adapter = LeaderboardsAdapter(imageModelArrayList)
        recyclerView.adapter = adapter
    }

    private fun populateList(): ArrayList<LeaderboardsModel> {
        val list = ArrayList<LeaderboardsModel>()
//        Nasty handcoded array of images
        val namesList = arrayOf("James", "Steph")
        val pointsList = arrayOf(5, 9)
//    ditto but of names (at least the strings are externalised)

//    Wrapping up an image and a name in the model class
        for (i in 0..1) {
            val imageModel = LeaderboardsModel()
            imageModel.setNames(namesList[i])
            imageModel.setPoints(pointsList[i])
            list.add(imageModel)
        }
//    Sorting alphabetically
        list.sortBy { list -> list.name }
        return list
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}