package com.yasuhiro.ca.find.controller

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.yasuhiro.ca.find.R
import com.yasuhiro.ca.find.entity.Const
import com.yasuhiro.ca.find.entity.Const.Companion.REVIEW_DBPATH

import kotlinx.android.synthetic.main.toolbar_backbutton.*

class ReviewWriteActivity : AppCompatActivity(), View.OnClickListener {

    private var mAuth: FirebaseAuth? = null
    private var mDatabase: FirebaseDatabase? = null
    private var mDatabaseReference: DatabaseReference? = null

    private var inputReviewTitle: EditText? = null
    private var inputReviewContent: EditText? = null
    private var returnButton: TextView? = null
    private var registButton: Button? = null
    private var userName: String? = null
    private var reviewTitle: String? = null
    private var reviewContent: String? = null
    private var mProgressBar: ProgressDialog? = null
    private var placeId: String? = null

    private val TAG = "CreateReviewWriteActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_review_write)
        setSupportActionBar(toolbar)
        val extras = intent.extras

        placeId = extras.get("placeId") as String

        returnButton = findViewById(R.id.backButton)
        returnButton!!.setOnClickListener(this)

        signUp()

    }

    // signUp function
    private fun signUp() {
        inputReviewTitle = findViewById(R.id.inputReviewTitle)
        inputReviewContent = findViewById(R.id.inputReviewContent)
        registButton = findViewById(R.id.registButton)
        mProgressBar = ProgressDialog(this)

        mAuth = FirebaseAuth.getInstance()

        mDatabase = FirebaseDatabase.getInstance()
        mDatabaseReference = mDatabase!!.reference.child(Const.PLACE_DBPATH).child(placeId!!)

        registButton!!.setOnClickListener(this)
    }

    // createAccount function
    private fun createReview() {
        reviewTitle = inputReviewTitle!!.text.toString()
        reviewContent = inputReviewContent!!.text.toString()

        if (!TextUtils.isEmpty(reviewTitle) && !TextUtils.isEmpty(reviewContent)) {
            mProgressBar!!.setMessage("Registering User...")
            mProgressBar!!.show()

            writeNewPlaceToDB(reviewTitle, reviewContent)

        }

        updateReview()
    }

    // updateUserInfoAndUI function
    private fun updateReview() {
        //start next activity
        val intent = Intent(this@ReviewWriteActivity, PlaceDetailActivity::class.java)
        startActivity(intent)
    }

    private fun writeNewPlaceToDB(reviewTitle: String?, reviewContent: String?) {
        val userId = mAuth!!.currentUser!!.uid
        var data = mutableMapOf<String,Any>()
        data!!.put("uid", userId)
        data!!.put("reviewTitle", reviewTitle!!)
        data!!.put("reviewContent", reviewContent!!)
        mDatabaseReference!!.child(REVIEW_DBPATH).push().setValue(data)
    }

    // returnButton function
    private fun returnButton() {
        super.onBackPressed()
    }

    override fun onClick(v: View) {
        when(v.id) {

            R.id.backButton -> returnButton()

            R.id.registButton -> createReview()
        }
    }
}
