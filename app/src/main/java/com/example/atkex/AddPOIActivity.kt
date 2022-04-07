package com.example.atkex

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import com.example.atkex.databinding.ActivityAddPoiactivityBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap
import com.bumptech.glide.Glide

/**
 * This class is an activity to supply the addition of a new points of interest
 */
class AddPOIActivity : AppCompatActivity() {

    //Initialise variables
    lateinit var nameText : EditText
    lateinit var infoText : EditText
    lateinit var latitudeText : EditText
    lateinit var longitudeText : EditText
    lateinit var addBtn : Button
    lateinit var selectImageBtn : Button
    lateinit var binding : ActivityAddPoiactivityBinding
    lateinit var imageURI : Uri
    lateinit var imageView: ImageView

    /**
     * Method on activity start
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddPoiactivityBinding.inflate(layoutInflater)
        setContentView(R.layout.activity_add_poiactivity)

        val newToolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.main_toolbar)
        setSupportActionBar(newToolbar)

        supportActionBar?.apply {
            setDisplayShowHomeEnabled(true)
            setDisplayHomeAsUpEnabled(true)
        }

        //Assign palette to variables
        nameText = findViewById(R.id.inputPOIName)
        infoText = findViewById(R.id.inputPOIInfo)
        longitudeText = findViewById(R.id.inputPOILong)
        latitudeText = findViewById(R.id.inputPOILat)
        addBtn = findViewById(R.id.btnAddPOI)
        selectImageBtn = findViewById(R.id.btnSelectImage)
        imageView = findViewById(R.id.imgView)

        //On click listener for select image button
        selectImageBtn.setOnClickListener {
            selectImage()
        }

        //On click listener for add button
        addBtn.setOnClickListener {
            val name = nameText.text.toString()
            val info = infoText.text.toString()
            val lat = latitudeText.text.toString()
            val long = longitudeText.text.toString()

            uploadImage(name, info, lat, long)
        }
    }

    /**
     * Method used to upload an image the the point of interest
     */
    private fun uploadImage(name: String, info: String, lat: String, long: String) {
        //Format the image file
        val formatter = SimpleDateFormat("yyyy_MM_dd_HH_mm_ss", Locale.getDefault())
        val now = Date()
        val fileName = formatter.format(now)
        val storageReference = FirebaseStorage.getInstance().getReference("images/$fileName")

        //Store the image
        storageReference.putFile(imageURI).
                addOnSuccessListener {
                    binding.imgView.setImageURI(null)
                    displayMessage(addBtn, "Image Uploaded")
                    saveFireStore(name, info, lat, long, fileName)
                }.addOnFailureListener {
                    displayMessage(addBtn, "Failed to Upload")
        }
    }

    /**
     * Method used when the select image is pressed
     */
    private fun selectImage() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT

        startActivityForResult(intent, 100)
    }

    /**
     * Display select image
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 100 && resultCode == RESULT_OK) {
            imageURI = data?.data!!
            binding.imgView.setImageURI(imageURI)
        }
    }

    /**
     * Save the data to the FireStore
     */
    fun saveFireStore(name: String, info: String, lat: String, long: String, imgName: String) {
        val db = FirebaseFirestore.getInstance()
        val pointOfInterest: MutableMap<String, Any> = HashMap()
        pointOfInterest["name"] = name
        pointOfInterest["info"] = info
        pointOfInterest["lat"] = lat
        pointOfInterest["long"] = long
        pointOfInterest["img"] = imgName

        //Add POI to the database
        db.collection("points_of_interests")
            .add(pointOfInterest)
            .addOnSuccessListener {
                closeKeyBoard()
                displayMessage(addBtn, "Added Successfully")
            }
            .addOnFailureListener {
                closeKeyBoard()
                displayMessage(addBtn, "Data Not Added")
            }
    }

    /**
     * Method to go to previous activity
     */
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    /**
     * Method to display snack bar
     */
    private fun displayMessage(view: View, msgTxt : String) {
        val sb = Snackbar.make(view, msgTxt, Snackbar.LENGTH_SHORT)
        sb.show()
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
}