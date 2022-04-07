package com.example.atkex

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.google.firebase.firestore.FirebaseFirestore

/**
 * Activity class for settings screen
 */
class SettingsActivity : AppCompatActivity() {

    //Initialise variables
    private lateinit var userDocumentID: String
    private var isAdmin: Boolean = false
    private lateinit var db : FirebaseFirestore

    /**
     * Method run on activity start
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        //Get intent values
        val extras = intent.extras
        userDocumentID = ""

        if (extras != null) {
            userDocumentID = extras.getString("userDocumentID") as String
        }

        //Find if user is admin
        db = FirebaseFirestore.getInstance()
        val docRef = db.collection("users").document(userDocumentID)
            .get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    var admin = document.getBoolean("admin")
                    isAdmin = admin!!
                }
            }

        //Initialise toolbar
        val newToolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.main_toolbar)
        setSupportActionBar(newToolbar)

        supportActionBar?.apply {
            setDisplayShowHomeEnabled(true)
            setDisplayHomeAsUpEnabled(true)
        }
    }

    /**
     * Method to provide a method to go back to the previous activity
     */
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    /**
     * Method to be run on edit button click
     */
    fun onClickEditPOI(view: View) {
        if (!isAdmin) {
            Toast.makeText(getBaseContext(),
                "You do not have access to modify POI's " ,
                Toast.LENGTH_LONG).show()
        } else {
            val newIntent = Intent(this, ManagePointsOfInterestActivity::class.java)
            startActivity(newIntent)
        }
    }

    /**
     * Method to be run on add poi button click
     */
    fun onClickAddPOI(view: View) {
        if (!isAdmin) {
            Toast.makeText(getBaseContext(),
                "You do not have access to add POI's " ,
                Toast.LENGTH_LONG).show()
        } else {
            val newIntent = Intent(this, AddPOIActivity::class.java)
            startActivity(newIntent)
        }
    }
}