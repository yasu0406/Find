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

/*
 *
 * ClassName:MainActivity
 * Date:2018/09/10
 * Create by: Yasuhiro Katayama
 *
 */
class MainActivity : AppCompatActivity(), View.OnClickListener {
    // variables of Firebase
    private var mAuth: FirebaseAuth? = null
    private var mAuthListener: FirebaseAuth.AuthStateListener? = null

    // variables of widget
    private var etEmail: EditText? = null
    private var etPassword: EditText? = null
    private var email: String? = null
    private var password: String? = null

    // variable of tag's EmailPassword
    private var TAG = "EmailPassword"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // call EditText
        etEmail = findViewById(R.id.etEmail) as EditText?
        etPassword = findViewById(R.id.etPass) as EditText?

        // Firebase instance
        mAuth = FirebaseAuth.getInstance()

        // Attach a new AuthListener to detect sign in and out
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

        // setOnClickListener for Login and regist
        findViewById<TextView>(R.id.loginButton).setOnClickListener(this)
        findViewById<TextView>(R.id.registerButton).setOnClickListener(this)
    }

    // Add Auth function
    override fun onStart() {
        super.onStart()
        mAuth!!.addAuthStateListener(mAuthListener!!)
    }

    // Click button function
    override fun onClick(v: View) {
        when (v.id) {
            R.id.loginButton -> signUserIn()

            R.id.registerButton -> siginUp()
        }
    }

    // Remove Auth function
    public override fun onStop() {
        super.onStop()
        // Remove the AuthListener
        if (mAuthListener != null) {
            mAuth!!.removeAuthStateListener(mAuthListener!!)
        }
    }

    // Check for email and password function
    private fun checkFormFields(): Boolean {
        email = etEmail!!.text.toString()
        password = etPassword!!.text.toString()

        if(email!!.isEmpty()) {
            etEmail!!.error = "Email Required"

            return false
        }
        if(password!!.isEmpty()) {
            etPassword!!.error = "Password Required"

            return false
        }

        return true
    }

    // SiginIn check function
    private fun signUserIn() {
        if (!checkFormFields())
            return

        email = etEmail!!.text.toString()
        password = etPassword!!.text.toString()

        // Sign the user in with email and password credentials
        mAuth!!.signInWithEmailAndPassword(email!!, password!!)
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

    // Move to siginUp
    private fun siginUp() {
        val intent = Intent(this, SignUpActivity::class.java)
        startActivity(intent)
    }
}
