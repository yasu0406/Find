package com.yasuhiro.ca.find.controller

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.yasuhiro.ca.find.R
import com.yasuhiro.ca.find.entity.Const
import com.yasuhiro.ca.find.entity.Const.Companion.REVIEW_DBPATH

import kotlinx.android.synthetic.main.toolbar_backbutton.*

class ReviewWriteActivity : AppCompatActivity(), View.OnClickListener {
    // variables of Firebase
    private var mAuth: FirebaseAuth? = null
    private var mDatabase: FirebaseDatabase? = null
    private var mDatabaseReference: DatabaseReference? = null
    private var uDatabaseReference: DatabaseReference? = null

    // variables of widget
    private var inputReviewContent: EditText? = null
    private var returnButton: TextView? = null
    private var registButton: Button? = null

    // variables of type
    private var userName: String? = null
    private var reviewContent: String? = null
    private var mProgressBar: ProgressDialog? = null
    private var placeId: String? = null
    private var placeName: String? = null
    private var discription: String? = null
    private var address: String? = null
    private var imageUrl: String? = null
    private var uImageUrl: String? = null
    private var userMap: MutableMap<String, Any>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_review_write)
        setSupportActionBar(toolbar)
        val extras = intent.extras

        placeId = extras.getString("placeId")
        placeName = extras.getString("placeName")
        discription = extras.getString("discription")
        address = extras.getString("address")
        imageUrl = extras.getString("imageUrl")
        uImageUrl = extras.getString("uImageUrl")
        userName = extras.getString("userName")

        returnButton = findViewById(R.id.backButton)
        returnButton!!.setOnClickListener(this)

        signUp()

    }

    // currentUser
    private fun currentUser() {
        uDatabaseReference = FirebaseDatabase.getInstance().getReference(Const.USER_DBPATH)
        val userId = mAuth!!.currentUser!!.uid
        var uChildEventListener =
        object : ValueEventListener {
            override fun onDataChange(userDataSnapshot: DataSnapshot) {
                userMap = userDataSnapshot.getValue() as MutableMap<String, Any>
                userName = userMap!!.get("userName") as String
                uImageUrl = userMap!!.get("imageUrl") as String
            }

            override fun onCancelled(databaseError: DatabaseError) {
            }

        }
        uDatabaseReference!!.child(userId!!).addValueEventListener(uChildEventListener)
    }

    // signUp function
    private fun signUp() {
        inputReviewContent = findViewById(R.id.inputReviewContent)
        registButton = findViewById(R.id.registButton)
        mProgressBar = ProgressDialog(this)

        mAuth = FirebaseAuth.getInstance()

        currentUser()

        mDatabase = FirebaseDatabase.getInstance()
        mDatabaseReference = mDatabase!!.reference.child(Const.PLACE_DBPATH).child(placeId!!)

        registButton!!.setOnClickListener(this)
    }

    // createAccount function
    private fun createReview() {
        reviewContent = inputReviewContent!!.text.toString()

        if (!TextUtils.isEmpty(reviewContent)) {
            mProgressBar!!.setMessage("Registering User...")
            mProgressBar!!.show()

            writeNewPlaceToDB(reviewContent)

        }

        updateReview()
    }

    // updateUserInfoAndUI function
    private fun updateReview() {
        //start next activity
        val intent = Intent(this@ReviewWriteActivity, PlaceDetailActivity::class.java)
        intent.putExtra("placeId", placeId)
        intent.putExtra("placeName", placeName)
        intent.putExtra("discription", discription)
        intent.putExtra("address", address)
        intent.putExtra("imageUrl", imageUrl)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)
    }

    // writeNewPlaceToDB function
    private fun writeNewPlaceToDB(reviewContent: String?) {
        val userId = mAuth!!.currentUser!!.uid
        var data = mutableMapOf<String,Any>()
        data!!.put("uid", userId)
        data!!.put("userName", userName!!)
        data!!.put("reviewContent", reviewContent!!)
        data!!.put("uImageUrl", uImageUrl!!)
        mDatabaseReference!!.child(REVIEW_DBPATH).push().setValue(data)
    }

    // returnButton function
    private fun returnButton() {
        super.onBackPressed()
    }

    // conCLick function
    override fun onClick(v: View) {
        when(v.id) {

            R.id.backButton -> returnButton()

            R.id.registButton -> createReview()
        }
    }
}