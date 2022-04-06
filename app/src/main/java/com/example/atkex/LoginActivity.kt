package com.example.atkex

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class LoginActivity : AppCompatActivity() {

    //Auth variables
    private var mAuth = FirebaseAuth.getInstance()
    private var currentUser = mAuth.currentUser
    lateinit var cEmail : String

    private lateinit var db : FirebaseFirestore

    lateinit var emailText : EditText
    lateinit var passwordText : EditText
    lateinit var usernameText : EditText
    lateinit var registerBtn : Button
    lateinit var loginBtn : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        emailText = findViewById(R.id.emailInput)
        passwordText = findViewById(R.id.passwordInput)
        usernameText = findViewById(R.id.nameInput)
        loginBtn = findViewById(R.id.btn_login)
        registerBtn = findViewById(R.id.btn_register)

//        if (mAuth.currentUser != null) {
//            val newIntent = Intent(this, MainActivity::class.java)
//            startActivity(newIntent)
//        }
    }

    override fun onStart() {
        super.onStart()
        update()
    }

    fun loginClick(view : View) {
        db = FirebaseFirestore.getInstance()
        if (loginBtn.text.toString() == "Map") {
            val newIntent = Intent(this, MainActivity::class.java)
            Log.d("TAG", cEmail)
            db.collection("users")
                .whereEqualTo("email", cEmail).get()
                .addOnSuccessListener { documents ->
                    for (document in documents) {
                        newIntent.putExtra("documentID", document.id)
                        startActivity(newIntent)
                    }
                }

        } else if (emailText.text.toString().isEmpty() || passwordText.text.toString().isEmpty()) {
            closeKeyBoard()
            displayMessage(loginBtn, getString(R.string.login_fail))
            update()
        } else {
            mAuth.signInWithEmailAndPassword(
                emailText.text.toString(),
                passwordText.text.toString()
            )
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {

                    update()
                    closeKeyBoard()
                    val newIntent = Intent(this, MainActivity::class.java)
                    startActivity(newIntent)
                } else {
                    closeKeyBoard()
                    displayMessage(loginBtn, getString(R.string.login_fail))
                }
            }
        }
    }

    fun logoutClick(view: View) {
        mAuth.signOut()
        update()
    }

    fun registerClick(view : View) {
        if (mAuth.currentUser != null) {
            displayMessage(view, getString(R.string.register_while_logged_in))
        } else if (emailText.text.toString().isEmpty() || passwordText.text.toString().isEmpty()) {
            closeKeyBoard()
            displayMessage(loginBtn, getString(R.string.register_fail))
            update()
        } else {
            mAuth.createUserWithEmailAndPassword(
                emailText.text.toString(),
                passwordText.text.toString()
            )
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        saveFireStore(usernameText.text.toString(), emailText.text.toString())
                        update()
                        closeKeyBoard()
                        val newIntent = Intent(this, MainActivity::class.java)
                        //snack bar here, say registered
                        startActivity(newIntent)
                    } else {
                        closeKeyBoard()
                        displayMessage(loginBtn, getString(R.string.register_fail))
                    }
                }
        }
    }

    fun saveFireStore(name: String, email: String) {
        val db = FirebaseFirestore.getInstance()
        val user: MutableMap<String, Any> = HashMap()
        user["name"] = name
        user["email"] = email
        user["points"] = 0

        db.collection("users")
            .add(user)

    }

    private fun displayMessage(view: View, msgTxt : String) {
        val sb = Snackbar.make(view, msgTxt, Snackbar.LENGTH_SHORT)
        sb.show()
    }

    fun update() {
        val currentUser = mAuth.currentUser

        val currentEmail = currentUser?.email
        cEmail = currentUser?.email.toString()
        val greetingSpace = findViewById<TextView>(R.id.txtView)

        if (currentEmail == null) {
            greetingSpace.text = getString(R.string.not_logged_in)
            loginBtn.text = "Login"
        } else {
            greetingSpace.text = getString(R.string.logged_in, currentEmail)
            loginBtn.text = "Map"
        }
    }

    private fun closeKeyBoard() {
        val view = this.currentFocus
        if (view != null) {
            val imn = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imn.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }
}