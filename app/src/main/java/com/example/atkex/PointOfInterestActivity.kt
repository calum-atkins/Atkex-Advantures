package com.example.atkex

import android.graphics.Bitmap
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity


class PointOfInterestActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_point_of_interest)

        val extras = intent.extras
        var name = ""
        var distance = ""

        if (extras != null) {
            name = extras.getString("name") as String
            distance = extras.getString("distance") as String
        }

        //actionbar
//        val actionbar = supportActionBar
//        //set actionbar title
//        actionbar!!.title = name
//        //set back button
//        actionbar.setDisplayHomeAsUpEnabled(true)
//        actionbar.setDisplayHomeAsUpEnabled(true)

        val imageView = findViewById<ImageView>(R.id.imageView)
        val textViewName = findViewById<TextView>(R.id.text_view_name)
        val textViewDistance = findViewById<TextView>(R.id.text_view_distance)

        textViewName.text = name
        textViewDistance.text = distance + " away"
        var bitmap: Bitmap? = intent.getParcelableExtra("BitmapImage") as Bitmap?
        imageView.setImageBitmap(bitmap)

    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}