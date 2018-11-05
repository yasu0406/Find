package com.yasuhiro.ca.find.controller

import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.yasuhiro.ca.find.R
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageMetadata
import com.google.firebase.storage.StorageReference
import com.yasuhiro.ca.find.entity.Const.Companion.USER_DBPATH
import com.yasuhiro.ca.find.entity.Const.Companion.USER_STPATH
import com.yasuhiro.ca.find.model.User
import java.io.IOException

/*
 *
 * ClassName:PlaceListActivity
 * Date:2018/09/10
 * Create by: Yasuhiro Katayama
 *
 */
class SignUpActivity : AppCompatActivity() {
    // variables of Firebase
    private var mAuth: FirebaseAuth? = null
    private var mDatabase: FirebaseDatabase? = null
    private var mDatabaseReference: DatabaseReference? = null
    private var mStorageReference: StorageReference? = null

    private var inputUserName: EditText? = null
    private var inputEmail: EditText? = null
    private var inputPass: EditText? = null
    private var choseButton: Button? = null
    private var signUpButton: Button? = null
    private var userId: String? = null
    private var userName: String? = null
    private var email: String? = null
    private var password: String? = null
    private var showImageView: ImageView? = null
    private var FilePathUri: Uri? = null
    private var mProgressBar: ProgressDialog? = null
    private val Image_Request_Code: Int = 7
    private val TAG = "CreateAccountActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        // call signUp
        signUp()
    }

    // signUp function
    private fun signUp() {
        inputUserName = findViewById(R.id.inputUserName)
        inputEmail = findViewById(R.id.inputEmail)
        inputPass = findViewById(R.id.inputPass)
        signUpButton = findViewById(R.id.signUpButton)
        choseButton = findViewById(R.id.choseButton)
        showImageView = findViewById(R.id.showImageView)
        mProgressBar = ProgressDialog(this)

        mAuth = FirebaseAuth.getInstance()

        mDatabase = FirebaseDatabase.getInstance()
        mDatabaseReference = mDatabase!!.reference.child(USER_DBPATH)
        mStorageReference = FirebaseStorage.getInstance().reference.child(USER_STPATH)

        choseButton!!.setOnClickListener {
            // Creating intent
            var intent = Intent()

            // Setting intent type as image to select image from phone storage.
            intent.setType("image/*")
            intent.setAction(Intent.ACTION_GET_CONTENT)
            startActivityForResult(Intent.createChooser(intent, "Please Select Image"), Image_Request_Code)
        }
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
//            mProgressBar!!.setMessage("Registering User...")
//            mProgressBar!!.show()

            mAuth!!.createUserWithEmailAndPassword(email!!, password!!).addOnCompleteListener(this) { task ->
                mProgressBar!!.hide()

                if(task.isSuccessful) {
                    Log.d(TAG,"createUserWithEmail:success")

                    userId = mAuth!!.currentUser!!.uid

                    verifyEmail()

                    UploadImageFileToFirebaseStorage()

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
                Toast.makeText(this@SignUpActivity, "Verification email sent to " + mUser.email, Toast.LENGTH_SHORT).show()
            } else {
                Log.e(TAG, "sendEmailVerification", task.exception)
                Toast.makeText(this@SignUpActivity, "Failed to send verification email.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Creating UploadImageFileToFirebaseStorage method to upload image on storage.
    private fun UploadImageFileToFirebaseStorage() {
        // Checking whether FilePathUri Is empty or not.
        if(FilePathUri != null) {
            val fileName = userName!!.toString()


//            val progressDialog = ProgressDialog(this)
//            progressDialog.setTitle("Uploading")
//            progressDialog.show()

            // Creating second StorageReference.
            val storageReference2nd = mStorageReference!!.child(fileName)
            // Create the file metadata
            var metadata = StorageMetadata.Builder()
                    .setContentType("image/jpeg")
                    .build()

            // Adding addOnSuccessListener to second StorageReference.
            storageReference2nd.putFile(FilePathUri!!, metadata).addOnSuccessListener{ taskSnapshot ->
                //progressDialog.dismiss()

                // Showing toast message after done uploading.
                Toast.makeText(applicationContext, "Image Uploaded Successfully ", Toast.LENGTH_SHORT).show()


                storageReference2nd!!.downloadUrl.addOnCompleteListener { taskSnapshot ->
                    var url = taskSnapshot.result.toString()
                    val currentUserDb = mDatabaseReference!!.child(userId!!)
                    currentUserDb.child("userName").setValue(userName)
                    currentUserDb.child("email").setValue(email)
                    currentUserDb.child("imageUrl").setValue(url)
                    currentUserDb.child("firstName").setValue("")
                    currentUserDb.child("lastName").setValue("")
                }

            }.addOnFailureListener{ exception ->

                // Showing exception erro message.
                Toast.makeText(this@SignUpActivity, exception.message, Toast.LENGTH_SHORT).show()

            }
        } else {
            Toast.makeText(this@SignUpActivity, "Please Select Image or Add Image Name", Toast.LENGTH_SHORT).show()
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == Image_Request_Code && resultCode == RESULT_OK && data != null && data.data != null) {
            FilePathUri = data.data

            try {
                // Getting selected image into Bitmap.
                var bitmap: Bitmap = MediaStore.Images.Media.getBitmap(contentResolver, FilePathUri)

                // Setting up bitmap selected image into ImageView.
                showImageView!!.setImageBitmap(bitmap)

            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }
}
