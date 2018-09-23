package com.yasuhiro.ca.find.controller

import android.app.ProgressDialog
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.yasuhiro.ca.find.R
import com.yasuhiro.ca.find.entity.const.Companion.PlacesPath

class RegistPlaceActivity : AppCompatActivity(), View.OnClickListener {

    private var inputPlaceName: EditText? = null
    private var inputDiscription: EditText? = null
    private var inputAddress: EditText? = null
    private var image1: Byte? = null
    private var registButton: Button? = null
    private var retunButton: TextView? = null

    private var mAuth: FirebaseAuth? = null
    private var mDatabase: FirebaseDatabase? = null
    private var mDatabaseReference: DatabaseReference? = null
    private var mProgressBar: ProgressDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_regist_place)
        registButton = findViewById(R.id.registPlaceButton) as Button
        retunButton = findViewById<View>(R.id.backButton) as TextView

        regist()

    }

    private fun regist() {
        inputPlaceName = findViewById<View>(R.id.inputPlaceName) as EditText?
        inputDiscription = findViewById<View>(R.id.inputDiscription) as EditText?
        inputAddress = findViewById<View>(R.id.inputAddress) as EditText?

        mAuth = FirebaseAuth.getInstance()
        mDatabase = FirebaseDatabase.getInstance()
        mDatabaseReference = mDatabase!!.reference.child(PlacesPath)

        registButton!!.setOnClickListener(this)
        retunButton!!.setOnClickListener(this)

    }

    private fun createPlaces() {
        var placeName = inputPlaceName!!.text.toString()
        var discription = inputDiscription!!.text.toString()
        var address = inputAddress!!.text.toString()

        if((!TextUtils.isEmpty(placeName) && !TextUtils.isEmpty(discription) && !TextUtils.isEmpty(address))) {

            val userId = mAuth!!.currentUser!!.uid
            var data = mutableMapOf<String,Any>()
                data!!.put("uid", userId)
                data!!.put("placeName", placeName)
                data!!.put("discription", discription)
                data!!.put("address", address)
                mDatabaseReference!!.push().setValue(data)
            updateUserInfoAndUI()
        } else {
            Toast.makeText(this@RegistPlaceActivity, "RegistPlace failed.", Toast.LENGTH_SHORT).show()
        }
    }

    // updateUserInfoAndUI function
    private fun updateUserInfoAndUI() {
        //start next activity
        val intent = Intent(this@RegistPlaceActivity, PlaceListActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)
    }

    private fun returnButton() {
        super.onBackPressed()
    }

    override fun onClick(v: View) {
        when(v.id) {
            R.id.registPlaceButton -> createPlaces()

            R.id.backButton -> returnButton()

        }
    }
}
