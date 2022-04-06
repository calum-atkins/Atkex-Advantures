package com.example.atkex

import android.graphics.Bitmap
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.firestore.*
import com.google.firebase.firestore.EventListener
import java.util.*
import kotlin.collections.ArrayList


class ManagePointOfInterestActivity  : AppCompatActivity() {

    private lateinit var recyclerView : RecyclerView
    private lateinit var reviewsList : ArrayList<ReviewsModel>
    private lateinit var myAdapter : ReviewsAdapter
    private lateinit var db : FirebaseFirestore

    private var id = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.manage_activity_point_of_interest)

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
            id = extras.getString("id") as String
        }

        newToolbar.title = name
        setSupportActionBar(newToolbar)
        supportActionBar?.apply {
            setDisplayShowHomeEnabled(true)
            setDisplayHomeAsUpEnabled(true)
        }


        val imageView = findViewById<ImageView>(R.id.imageView)
//        val textViewName = findViewById<TextView>(R.id.text_view_name)
//        val textViewDistance = findViewById<TextView>(R.id.text_view_distance)
//        val textViewInfo = findViewById<TextView>(R.id.text_view_info)
//        val speakButton = findViewById<Button>(R.id.btnUpdate)

        val inputTextName = findViewById<EditText>(R.id.inputTextName)
        val inputTextLat = findViewById<EditText>(R.id.inputTextLat)
        val inputTextLong = findViewById<EditText>(R.id.inputTextLong)
        val inputTextInfo = findViewById<EditText>(R.id.inputTextInfo)



        inputTextName.setText(name)
        inputTextLat.setText(lat)
        inputTextLong.setText(long)
        inputTextInfo.setText(info)

        var bitmap: Bitmap? = intent.getParcelableExtra("BitmapImage") as Bitmap?
        imageView.setImageBitmap(bitmap)


        EventChangeListener()

    }

    private fun EventChangeListener() {
        db = FirebaseFirestore.getInstance()
//
//        db.collection("points_of_interests/02ESZ6AHBj0rx1U0HEyo/reviews")//.orderBy("distance", Query.Direction.ASCENDING)
//            .addSnapshotListener(object : EventListener<QuerySnapshot> {
//                override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {
//                    if (error != null) {
//                        Log.e("Firebase error", error.message.toString())
//                        return
//                    }
//                    for (dc : DocumentChange in value?.documentChanges!!){
//                        if (dc.type == DocumentChange.Type.ADDED) {
//                            reviewsList.add(dc.document.toObject(ReviewsModel::class.java))
//                        }
//                    }
//
//                    myAdapter.notifyDataSetChanged()
//                }
//            })
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}