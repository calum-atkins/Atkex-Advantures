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

        val newToolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.main_toolbar)

        val extras = intent.extras
        var name = ""
        var info = ""
        var lat = ""
        var long = ""

        if (extras != null) {
            name = extras.getString("name") as String
            info = extras.getString("info") as String
            lat = extras.getString("lat") as String
            long = extras.getString("long") as String
        }

        newToolbar.title = name
        setSupportActionBar(newToolbar)
        supportActionBar?.apply {
            setDisplayShowHomeEnabled(true)
            setDisplayHomeAsUpEnabled(true)
        }


        val imageView = findViewById<ImageView>(R.id.imageView)
        val textViewName = findViewById<TextView>(R.id.text_view_name)
        val textViewDistance = findViewById<TextView>(R.id.text_view_distance)
        val textViewInfo = findViewById<TextView>(R.id.text_view_info)

        textViewName.text = name
        textViewDistance.text = "($lat, $long)"
        textViewInfo.text = "Info\n$info"

        var bitmap: Bitmap? = intent.getParcelableExtra("BitmapImage") as Bitmap?
        imageView.setImageBitmap(bitmap)





    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}