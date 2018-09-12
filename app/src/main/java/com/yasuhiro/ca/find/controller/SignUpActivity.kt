package com.yasuhiro.ca.find.controller

import android.app.ProgressDialog
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.yasuhiro.ca.find.R
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.yasuhiro.ca.find.entity.const.Companion.UsersPath


class SignUpActivity : AppCompatActivity() {
    private var mAuth: FirebaseAuth? = null
    private var mDatabase: FirebaseDatabase? = null
    private var mDatabaseReference: DatabaseReference? = null

    private var inputUserName: EditText? = null
    private var inputEmail: EditText? = null
    private var inputPass: EditText? = null
    private var signUpButton: Button? = null
    private var userName: String? = null
    private var email: String? = null
    private var password: String? = null
    private var mProgressBar: ProgressDialog? = null
    private val TAG = "CreateAccountActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        // signUp
        signUp()
    }

    // signUp function
    private fun signUp() {
        inputUserName = findViewById<View>(R.id.inputUserName) as EditText
        inputEmail = findViewById<View>(R.id.inputEmail) as EditText
        inputPass = findViewById<View>(R.id.inputPass) as EditText
        signUpButton = findViewById(R.id.signUpButton) as Button
        mProgressBar = ProgressDialog(this)

        mAuth = FirebaseAuth.getInstance()

        mDatabase = FirebaseDatabase.getInstance()
        mDatabaseReference = mDatabase!!.reference.child(UsersPath)

        signUpButton!!.setOnClickListener{
            createAccount()
        }
    }

    // createAccount function
    private fun createAccount() {
        userName = inputUserName!!.text.toString()
        email = inputEmail!!.text.toString()
        password = inputPass!!.text.toString()

        if(!TextUtils.isEmpty(userName) && !TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)) {
            mProgressBar!!.setMessage("Registering User...")
            mProgressBar!!.show()

            mAuth!!.createUserWithEmailAndPassword(email!!, password!!).addOnCompleteListener(this) { task ->
                mProgressBar!!.hide()

                if(task.isSuccessful) {
                    Log.d(TAG,"createUserWithEmail:success")

                    val userId = mAuth!!.currentUser!!.uid

                    verifyEmail()

                    val currentUserDb = mDatabaseReference!!.child(userId)
                    currentUserDb.child("userName").setValue(userName)
                    currentUserDb.child("firstName").setValue("")
                    currentUserDb.child("lastName").setValue("")

                    updateUserInfoAndUI()
                } else {
                    Log.w(TAG, "createUserWithEmail:failure", task.exception)
                    Toast.makeText(this@SignUpActivity, "Authentication failed.", Toast.LENGTH_SHORT).show()
                }
            }

        } else {
            Toast.makeText(this, "Enter all details", Toast.LENGTH_SHORT).show()
        }
    }

    // updateUserInfoAndUI function
    private fun updateUserInfoAndUI() {
        //start next activity
        val intent = Intent(this@SignUpActivity, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)
    }

    // verifyEmail function
    private fun verifyEmail() {
        val mUser = mAuth!!.currentUser
        mUser!!.sendEmailVerification().addOnCompleteListener(this) { task ->

            if(task.isSuccessful) {
                Toast.makeText(this@SignUpActivity, "Verification email sent to " + mUser.getEmail(), Toast.LENGTH_SHORT).show()
            } else {
                Log.e(TAG, "sendEmailVerification", task.exception)
                Toast.makeText(this@SignUpActivity, "Failed to send verification email.", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
