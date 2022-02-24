package com.example.atkex

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class PointsOfInterestActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_points_of_interest)

        val imageModelArrayList = populateList()

        val recyclerView = findViewById<View>(R.id.recycler_view_list_poi) as RecyclerView

        val layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager
        val adapter = PointsOPfInterestAdapter(imageModelArrayList)
        recyclerView.adapter = adapter
    }

    private fun populateList(): ArrayList<PointsOfInterestModel> {
        val list = ArrayList<PointsOfInterestModel>()
//        Nasty handcoded array of images
        val myImageList = arrayOf(R.drawable.gower_peninsula, R.drawable.mumbles_pier)

        val distancesList = arrayOf(5, 9)
//    ditto but of names (at least the strings are externalised)
        val myImageNameList = arrayOf(R.string.gower_peninsula, R.string.mumbles_pier)

//    Wrapping up an image and a name in the model class
        for (i in 0..1) {
            val imageModel = PointsOfInterestModel()
            imageModel.setNames(getString(myImageNameList[i]))
            imageModel.setDistances(distancesList[i])
            imageModel.setImages(myImageList[i])
            list.add(imageModel)
        }
//    Sorting alphabetically
        list.sortBy { list -> list.modelName }
        return list
    }
}