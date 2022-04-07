package com.example.atkex

import android.content.Context
import android.graphics.Bitmap
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.util.Log
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.*
import com.google.firebase.firestore.EventListener
import java.util.*
import kotlin.collections.ArrayList

/**
 * Class for a selected point of interest activity
 */
class PointOfInterestActivity  : AppCompatActivity() {

    //Initialise variables
    private lateinit var recyclerView : RecyclerView
    private lateinit var reviewsList : ArrayList<ReviewsModel>
    private lateinit var myAdapter : ReviewsAdapter
    private lateinit var db : FirebaseFirestore
    private lateinit var reviewText : EditText
    private var id = ""
    private lateinit var tts : TextToSpeech

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_point_of_interest)

        //Initialise toolbar
        val newToolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.main_toolbar)

        //Initialise intents
        val extras = intent.extras
        var name = ""
        var info = ""
        var lat = ""
        var long = ""
        var userDocumentID = ""

        if (extras != null) {
            name = extras.getString("name") as String
            info = extras.getString("info") as String
            lat = extras.getString("lat") as String
            long = extras.getString("long") as String
            id = extras.getString("id") as String
            userDocumentID = extras.getString("userDocumentID") as String
        }

        //Toolbar settings
        newToolbar.title = name
        setSupportActionBar(newToolbar)
        supportActionBar?.apply {
            setDisplayShowHomeEnabled(true)
            setDisplayHomeAsUpEnabled(true)
        }

        //Initialise pallet
        val imageView = findViewById<ImageView>(R.id.imageView)
        val textViewName = findViewById<TextView>(R.id.text_view_name)
        val textViewDistance = findViewById<TextView>(R.id.text_view_distance)
        val textViewInfo = findViewById<TextView>(R.id.text_view_info)
        val speakButton = findViewById<Button>(R.id.btnSpeak)
        val postReviewButton = findViewById<Button>(R.id.btnPostReview)
        reviewText = findViewById<EditText>(R.id.input_review)
        textViewName.text = name
        textViewDistance.text = "($lat, $long)"
        textViewInfo.text = "Info\n$info"

        //On speak button press
        speakButton.setOnClickListener {
            tts = TextToSpeech(applicationContext, TextToSpeech.OnInitListener {
                if (it==TextToSpeech.SUCCESS) {
                    tts.language = Locale.ENGLISH
                    tts.setSpeechRate(1.0f)
                    tts.speak(textViewInfo.text.toString(), TextToSpeech.QUEUE_ADD, null)
                }
            })
        }

        //On post review button press
        postReviewButton.setOnClickListener {
            postReview(userDocumentID, reviewText.text.toString())
        }

        /** DO NEED??????????????????????????????????*/
        var bitmap: Bitmap? = intent.getParcelableExtra("BitmapImage") as Bitmap?
        imageView.setImageBitmap(bitmap)

        //Initialise recycler view
        recyclerView = findViewById(R.id.recyclerViewReview)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)

        reviewsList = arrayListOf()
        myAdapter = ReviewsAdapter(this@PointOfInterestActivity, reviewsList)
        recyclerView.adapter = myAdapter

        EventChangeListener()
    }

    /**
     * Method called to post a review
     */
    private fun postReview(userDocumentID: String, comment: String) {
        db = FirebaseFirestore.getInstance()

        val docRef = db.collection("users").document(userDocumentID)
        docRef.get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    var username = document.getString("name").toString()
                    addComment(username, comment, userDocumentID)
                }
            }
    }

    /**
     * Method to add comment to database
     */
    private fun addComment(username: String, comment: String, userDocumentID: String) {
        val db = FirebaseFirestore.getInstance()
        val review: MutableMap<String, Any> = HashMap()
        review["name"] = username
        review["comment"] = comment

        db.collection("points_of_interests/$id/reviews")
            .add(review).addOnSuccessListener {
                closeKeyBoard()
                reviewText.text.clear()
            }
    }

    /**
     * Method to close on screen keyboard
     */
    private fun closeKeyBoard() {
        val view = this.currentFocus
        if (view != null) {
            val imn = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imn.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    /**
     * Method to check for database changes
     */
    private fun EventChangeListener() {
        db = FirebaseFirestore.getInstance()

        db.collection("points_of_interests/$id/reviews")
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

    /**
     * Method to go to previous activity
     */
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}