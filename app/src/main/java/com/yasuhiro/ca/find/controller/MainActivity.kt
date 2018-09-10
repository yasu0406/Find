package com.yasuhiro.ca.find.controller

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.yasuhiro.ca.find.R


class MainActivity : AppCompatActivity(), View.OnClickListener {
    private var mAuth: FirebaseAuth? = null
    private var mAuthListener: FirebaseAuth.AuthStateListener? = null

    private var etEmail: EditText? = null
    private var etPassword: EditText? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        etEmail = findViewById(R.id.etEmail) as EditText?
        etPassword = findViewById(R.id.etPass) as EditText?

        // Firebase instance
        mAuth = FirebaseAuth.getInstance()

        // TODO: Attach a new AuthListener to detect sign in and out
        mAuthListener = FirebaseAuth.AuthStateListener { firebaseAuth ->
            val user = firebaseAuth.currentUser
            if (user != null) {
                // User is signed in
                Log.d(TAG, "Signed in: " + user.uid)
            } else {
                // User is signed out
                Log.d(TAG, "Currently signed out")
            }
        }

        findViewById<TextView>(R.id.loginButton).setOnClickListener(this)
    }

    // add Auth function
    override fun onStart() {
        super.onStart()
        // TODO: add the AuthListener
        mAuth!!.addAuthStateListener(mAuthListener!!)
    }

    // click button function
    override fun onClick(v: View) {
        when (v.id) {
            R.id.loginButton -> signUserIn()
        }
    }

    // remove Auth function
    public override fun onStop() {
        super.onStop()
        // TODO: Remove the AuthListener
        if (mAuthListener != null) {
            mAuth!!.removeAuthStateListener(mAuthListener!!)
        }
    }

    // check for email and password function
    private fun checkFormFields(): Boolean {
        val email: String
        val password: String

        email = etEmail!!.text.toString()
        password = etPassword!!.text.toString()

        if(email.isEmpty()) {
            etEmail!!.error = "Email Required"

            return false
        }
        if(password.isEmpty()) {
            etPassword!!.error = "Password Required"

            return false
        }

        return true
    }

    // siginIn check function
    private fun signUserIn() {
        if (!checkFormFields())
            return

        val email = etEmail!!.text.toString()
        val password = etPassword!!.text.toString()

        // TODO: sign the user in with email and password credentials
        mAuth!!.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this
                ) { task ->
                    if (task.isSuccessful) {
                        Log.d(TAG, "createUserWithEmail:success")
                        val intent = Intent(this, PlaceListActivity::class.java)
                        startActivity(intent)
                    } else {
                        Toast.makeText(this, "Sign in failed", Toast.LENGTH_SHORT)
                                .show()
                    }
                }
    }


    companion object {
        private val TAG = "EmailPassword"
    }

}
