package com.example.atkex

import android.content.Context
import android.graphics.Bitmap
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.*
import com.google.firebase.firestore.EventListener
import java.util.*
import kotlin.collections.ArrayList


class PointOfInterestActivity  : AppCompatActivity() {

    private lateinit var recyclerView : RecyclerView
    private lateinit var reviewsList : ArrayList<ReviewsModel>
    private lateinit var myAdapter : ReviewsAdapter
    private lateinit var db : FirebaseFirestore

    lateinit var tts : TextToSpeech

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
        val speakButton = findViewById<Button>(R.id.btnSpeak)



        textViewName.text = name
        textViewDistance.text = "($lat, $long)"
        textViewInfo.text = "Info\n$info"

        speakButton.setOnClickListener {
            tts = TextToSpeech(applicationContext, TextToSpeech.OnInitListener {
                if (it==TextToSpeech.SUCCESS) {
                    tts.language = Locale.ENGLISH
                    tts.setSpeechRate(1.0f)
                    tts.speak(textViewInfo.text.toString(), TextToSpeech.QUEUE_ADD, null)
                }
            })
        }

        var bitmap: Bitmap? = intent.getParcelableExtra("BitmapImage") as Bitmap?
        imageView.setImageBitmap(bitmap)

        recyclerView = findViewById(R.id.recyclerViewReview)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)

        reviewsList = arrayListOf()
        myAdapter = ReviewsAdapter(this@PointOfInterestActivity, reviewsList)
        recyclerView.adapter = myAdapter

        EventChangeListener()

    }

    private fun EventChangeListener() {
        db = FirebaseFirestore.getInstance()

        db.collection("points_of_interests/02ESZ6AHBj0rx1U0HEyo/reviews")//.orderBy("distance", Query.Direction.ASCENDING)
            .addSnapshotListener(object : EventListener<QuerySnapshot> {
                override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {
                    if (error != null) {
                        Log.e("Firebase error", error.message.toString())
                        return
                    }
                    for (dc : DocumentChange in value?.documentChanges!!){
                        if (dc.type == DocumentChange.Type.ADDED) {
                            reviewsList.add(dc.document.toObject(ReviewsModel::class.java))
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