package com.example.atkex

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView

class PointOfInterestActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_point_of_interest)

        val extras = intent.extras
        var name = ""
        var distance = ""

        if (extras == null) {
            name = "No extras found"
        } else {
            name = extras.getString("name") as String
            distance = extras.getString("distance") as String
        }

        val imageView = findViewById<ImageView>(R.id.imageView)
        val textViewName = findViewById<TextView>(R.id.text_view_name)
        val textViewDistance = findViewById<TextView>(R.id.text_view_distance)

        textViewName.text = name
        textViewDistance.text = distance

    }
}