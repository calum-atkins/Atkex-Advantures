package com.example.atkex

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.google.firebase.firestore.FirebaseFirestore

class SettingsActivity : AppCompatActivity() {

    private lateinit var userDocumentID: String
    private var isAdmin: Boolean = false

    private lateinit var db : FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val extras = intent.extras
        userDocumentID = ""

        if (extras != null) {
            userDocumentID = extras.getString("userDocumentID") as String
        }

        db = FirebaseFirestore.getInstance()

        val docRef = db.collection("users").document(userDocumentID)
            .get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    var admin = document.getBoolean("admin")
                    isAdmin = admin!!
                }
            }

        val newToolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.main_toolbar)
        setSupportActionBar(newToolbar)

        supportActionBar?.apply {
            setDisplayShowHomeEnabled(true)
            setDisplayHomeAsUpEnabled(true)
        }

    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

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