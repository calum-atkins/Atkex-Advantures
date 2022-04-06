package com.example.atkex

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

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
        val newIntent = Intent(this, ManagePointsOfInterestActivity::class.java)
        startActivity(newIntent)
    }

    fun onClickAddPOI(view: View) {
        val newIntent = Intent(this, AddPOIActivity::class.java)
        startActivity(newIntent)
    }
}