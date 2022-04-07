package com.example.atkex

import android.graphics.Bitmap
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.database.DatabaseReference
import com.google.firebase.firestore.*
import kotlin.collections.ArrayList

/**
 * Activity screen manage a point of interest
 */
class ManagePointOfInterestActivity  : AppCompatActivity() {

    //Initialise variables
    private lateinit var recyclerView : RecyclerView
    private lateinit var reviewsList : ArrayList<ReviewsModel>
    private lateinit var myAdapter : ReviewsAdapter
    private lateinit var db : FirebaseFirestore
    private lateinit var databaseReference: DatabaseReference
    private lateinit var updateButton : Button
    private lateinit var deleteButton : Button
    private var id = ""

    /**
     * Method called on activity start
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.manage_activity_point_of_interest)

        //Initialise toolba
        val newToolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.main_toolbar)

        val extras = intent.extras
        var name = ""
        var info = ""
        var lat = ""
        var long = ""

        //Define intents
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

        //Define palette
        val imageView = findViewById<ImageView>(R.id.imageView)
        val inputTextName = findViewById<EditText>(R.id.inputTextName)
        val inputTextLat = findViewById<EditText>(R.id.inputTextLat)
        val inputTextLong = findViewById<EditText>(R.id.inputTextLong)
        val inputTextInfo = findViewById<EditText>(R.id.inputTextInfo)

        updateButton = findViewById<Button>(R.id.btnSpeak)
        deleteButton = findViewById<Button>(R.id.btnPostReview)

        inputTextName.setText(name)
        inputTextLat.setText(lat)
        inputTextLong.setText(long)
        inputTextInfo.setText(info)

        /**------------------------------------------------------------------------------do need?*/
        var bitmap: Bitmap? = intent.getParcelableExtra("BitmapImage") as Bitmap?
        imageView.setImageBitmap(bitmap)

        updateButton.setOnClickListener {
            updateData(id, inputTextName.text.toString(), inputTextLat.text.toString(),
                inputTextLong.text.toString(), inputTextInfo.text.toString())
        }

        deleteButton.setOnClickListener {
            deleteData(id)
        }

    }

    /**
     * Method to delete data from database
     */
    private fun deleteData(id: String) {
        db = FirebaseFirestore.getInstance()
        db.collection("points_of_interests")
            .document(id).delete().addOnSuccessListener {
//                onSupportNavigateUp()
//                displayMessage(recyclerView, "Successfully deleted.")
            }.addOnFailureListener {
                displayMessage(deleteButton, "Failed to delete.")
            }
    }

    /**
     * Method to update data in database
     */
    private fun updateData(id: String, name: String, lat: String, long: String, info: String) {
        db = FirebaseFirestore.getInstance()
        db.collection("points_of_interests").document(id).update("name", name)
        db.collection("points_of_interests").document(id).update("lat", lat)
        db.collection("points_of_interests").document(id).update("long", long)
        db.collection("points_of_interests").document(id).update("info", info)

        displayMessage(updateButton, "Updated successfully")
    }

    /**
     * Method to display snack bar
     */
    private fun displayMessage(view: View, msgTxt: String) {
        val sb = Snackbar.make(view, msgTxt, Snackbar.LENGTH_SHORT)
        sb.show()
    }

    /**
     * Method fo back button press
     */
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}