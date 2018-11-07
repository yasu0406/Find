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
import com.google.firebase.database.*
import com.yasuhiro.ca.find.R
import com.yasuhiro.ca.find.entity.Const

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
    private var mDatabaseReference: DatabaseReference? = null

    // variables of widget
    private var etEmail: EditText? = null
    private var etPassword: EditText? = null
    private var email: String? = null
    private var password: String? = null
    private var cUserImageUrl: String? = null
    private var map: MutableMap<String, Any>? = null
    private var cUserName: String? = null

    // variable of tag's EmailPassword
    private var TAG = "EmailPassword"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // call FirebaseDatabase
        mDatabaseReference = FirebaseDatabase.getInstance().getReference(Const.USER_DBPATH)
        // call EditText
        etEmail = findViewById(R.id.etEmail)
        etPassword = findViewById(R.id.etPass)

        // Firebase instance
        mAuth = FirebaseAuth.getInstance()

        // Attach a new AuthListener to detect sign in and out
        mAuthListener = FirebaseAuth.AuthStateListener { firebaseAuth ->
            val user = firebaseAuth.currentUser
            if (user != null) {
                // User is signed in
                Log.d(TAG, "Signed in: " + user.uid)
                getUser(user.uid)

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
                        intent.putExtra("cUserImageUrl", cUserImageUrl)
                        intent.putExtra("cUserName", cUserName)
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

    private fun getUser(uid: String) {
        var mChildEventListener =
                object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        map = dataSnapshot.getValue() as MutableMap<String, Any>
                        cUserImageUrl = map!!.get("imageUrl") as String
                        cUserName = map!!.get("userName") as String

                    }

                    override fun onCancelled(p0: DatabaseError) {
                        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                    }
                }

        mDatabaseReference!!.child(uid).addListenerForSingleValueEvent(mChildEventListener)

    }

}
