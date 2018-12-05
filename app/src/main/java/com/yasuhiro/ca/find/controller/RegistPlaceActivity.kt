package com.yasuhiro.ca.find.controller

import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageMetadata
import com.google.firebase.storage.StorageReference
import com.yasuhiro.ca.find.R
import com.yasuhiro.ca.find.entity.Const.Companion.PLACE_DBPATH
import com.yasuhiro.ca.find.entity.Const.Companion.PLACE_STPATH
import kotlinx.android.synthetic.main.toolbar_backbutton.*

/*
 *
 * ClassName:RegistPlaceActivity
 * Date:2018/09/13
 * Create by: Yasuhiro Katayama
 *
 */
class RegistPlaceActivity : AppCompatActivity(), View.OnClickListener {
    // variables of Firebase
    private var mAuth: FirebaseAuth? = null
    private var mDatabase: FirebaseDatabase? = null
    private var mDatabaseReference: DatabaseReference? = null
    private var mStorageReference: StorageReference? = null

    // variables of widget
    private var inputPlaceName: EditText? = null
    private var inputDiscription: EditText? = null
    private var inputAddress: EditText? = null
    private var registButton: Button? = null
    private var returnButton: TextView? = null
    private var mProgressBar: ProgressDialog? = null
    private var FilePathUri: Uri? = null
    private val Image_Request_Code: Int = 7
    private var imgButton1: Button? = null
    private var imgButton2: Button? = null
    private var imgButton3: Button? = null
    private var imgButton4: Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_regist_place)
        setSupportActionBar(toolbar)
        registButton = findViewById(R.id.registPlaceButton)
        returnButton = findViewById(R.id.backButton)
        imgButton1 = findViewById(R.id.imgButton1)
//        imgButton2 = findViewById(R.id.imgButton2) as Button
//        imgButton3 = findViewById(R.id.imgButton3) as Button
//        imgButton4 = findViewById(R.id.imgButton4) as Button

        regist()

    }

    private fun regist() {
        inputPlaceName = findViewById<View>(R.id.inputPlaceName) as EditText?
        inputDiscription = findViewById<View>(R.id.inputDiscription) as EditText?
        inputAddress = findViewById<View>(R.id.inputAddress) as EditText?

        mAuth = FirebaseAuth.getInstance()
        mDatabase = FirebaseDatabase.getInstance()
        mDatabaseReference = mDatabase!!.reference.child(PLACE_DBPATH)
        mStorageReference = FirebaseStorage.getInstance().reference.child(PLACE_STPATH)

        registButton!!.setOnClickListener(this)
        returnButton!!.setOnClickListener(this)
        imgButton1!!.setOnClickListener(this)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == Image_Request_Code && resultCode == RESULT_OK && data != null && data.data != null) {
            FilePathUri = data.data
        }
    }

    //
    private fun createPlaces() {
        // get EditText of inputs
        var placeName = inputPlaceName!!.text.toString()
        var discription = inputDiscription!!.text.toString()
        var address = inputAddress!!.text.toString()

        if((!TextUtils.isEmpty(placeName) && !TextUtils.isEmpty(discription) && !TextUtils.isEmpty(address))) {


            if(FilePathUri != null) {
                UploadImageFileToFirebaseStorage(placeName, discription, address)

            } else {
                Toast.makeText(this@RegistPlaceActivity, "Please Select Image or Add Image Name", Toast.LENGTH_SHORT).show()
            }

        } else {
            Toast.makeText(this@RegistPlaceActivity, "RegistPlace failed.", Toast.LENGTH_SHORT).show()
        }
        updateUserInfoAndUI()
    }

    // updateUserInfoAndUI function
    private fun updateUserInfoAndUI() {
        //start next activity
        val intent = Intent(this@RegistPlaceActivity, PlaceListActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)
    }

    // UploadImageFileToFirebaseStorage
    private fun UploadImageFileToFirebaseStorage(placeName: String?, discription: String?, address: String?) {
        // Checking whether FilePathUri Is empty or not.
        // Creating second StorageReference.
        mStorageReference = mStorageReference!!.child(placeName!!)
        // Create the file metadata
        var metadata = StorageMetadata.Builder().setContentType("image/jpeg").build()
        // Adding addOnSuccessListener to second StorageReference.
        mStorageReference!!.putFile(FilePathUri!!, metadata).addOnSuccessListener { taskSnapshot ->
//        mProgressBar!!.dismiss()

            // Showing toast message after done uploading.
            Toast.makeText(applicationContext, "Image Uploaded Successfully ", Toast.LENGTH_SHORT).show()

            mStorageReference!!.downloadUrl.addOnCompleteListener{taskSnapshot ->
                var url = taskSnapshot.result.toString()
                writeNewPlaceToDB(placeName, discription, address, url)
            }.addOnFailureListener{ exception ->

                // Showing exception erro message.
                Toast.makeText(this@RegistPlaceActivity, exception.message, Toast.LENGTH_SHORT).show()

            }
        }
    }

    private fun chooseImage() {
        // Creating intent
        var intent = Intent()

        // Setting intent type as image to select image from phone storage.
        intent.setType("image/*")
        intent.setAction(Intent.ACTION_GET_CONTENT)
        startActivityForResult(Intent.createChooser(intent, "Please Select Image"), Image_Request_Code)
    }

    // returnButton function
    private fun returnButton() {
        super.onBackPressed()
    }

    private fun writeNewPlaceToDB(placeName: String?, discription: String?, address: String?, url:String?) {
        val userId = mAuth!!.currentUser!!.uid
        var data = mutableMapOf<String,Any>()
        data!!.put("uid", userId)
        data!!.put("placeName", placeName!!)
        data!!.put("discription", discription!!)
        data!!.put("address", address!!)
        data!!.put("imageUrl", url!!)
        mDatabaseReference!!.push().setValue(data)
    }

    // onClick function
    override fun onClick(v: View) {
        when(v.id) {
            R.id.registPlaceButton -> createPlaces()

            R.id.backButton -> returnButton()

            R.id.imgButton1 -> chooseImage()

        }
    }
}
