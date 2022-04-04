package com.example.atkex

import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.media.Image
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import com.example.atkex.databinding.ActivityAddPoiactivityBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import java.net.URI
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap

class AddPOIActivity : AppCompatActivity() {

    lateinit var nameText : EditText
    lateinit var infoText : EditText
    lateinit var latitudeText : EditText
    lateinit var longitudeText : EditText
    lateinit var addBtn : Button

    lateinit var binding : ActivityAddPoiactivityBinding
    lateinit var imageURI : Uri

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



        nameText = findViewById(R.id.inputPOIName)
        infoText = findViewById(R.id.inputPOIInfo)
        longitudeText = findViewById(R.id.inputPOILong)
        latitudeText = findViewById(R.id.inputPOILat)
        addBtn = findViewById(R.id.btnAddPOI)

        binding.btnSelectImage.setOnClickListener {
            selectImage()
        }


        addBtn.setOnClickListener {
            val name = nameText.text.toString()
            val info = nameText.text.toString()
            val lat = nameText.text.toString()
            val long = longitudeText.text.toString()

            saveFireStore(name, info, lat, long)
        }
    }

    private fun uploadImage() {
        val progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Uploading...")
        progressDialog.setCancelable(false)
        progressDialog.show()

        val formatter = SimpleDateFormat("yyyy_MM_dd_HH_mm_ss", Locale.getDefault())
        val now = Date()
        val fileName = formatter.format(now)
        val storageReference = FirebaseStorage.getInstance().getReference("images/$fileName")

        storageReference.putFile(imageURI).
                addOnSuccessListener {
                    binding.imgView.setImageURI(null)
                    displayMessage(addBtn, "Image Uploaded")

                }.addOnFailureListener {
                    displayMessage(addBtn, "Failed to Upload")
        }
    }

    private fun selectImage() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT

        startActivityForResult(intent, 100)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 100 && resultCode == RESULT_OK) {
            imageURI = data?.data!!
            binding.imgView.setImageURI(imageURI)
        }
    }

    fun saveFireStore(name: String, info: String, lat: String, long: String) {
        val db = FirebaseFirestore.getInstance()
        val pointOfInterest: MutableMap<String, Any> = HashMap()
        pointOfInterest["name"] = name
        pointOfInterest["info"] = info
        pointOfInterest["lat"] = lat
        pointOfInterest["long"] = long


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

    private fun displayMessage(view: View, msgTxt : String) {
        val sb = Snackbar.make(view, msgTxt, Snackbar.LENGTH_SHORT)
        sb.show()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun closeKeyBoard() {
        val view = this.currentFocus
        if (view != null) {
            val imn = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imn.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }
}