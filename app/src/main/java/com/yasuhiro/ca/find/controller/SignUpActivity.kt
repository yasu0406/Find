package com.yasuhiro.ca.find.controller

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import com.google.firebase.auth.FirebaseAuth
import com.yasuhiro.ca.find.R
import com.google.firebase.database.DatabaseReference
import com.google.firebase.auth.AuthResult
import com.google.android.gms.tasks.OnCompleteListener



class SignUpActivity : AppCompatActivity() {
    var mAuth: FirebaseAuth? = null
    var mCreateAccountListener: OnCompleteListener<AuthResult>? = null
    var mLoginListener: OnCompleteListener<AuthResult>? = null
    var mDatabaseReference: DatabaseReference? = null

    private var userName: EditText? = null
    private var etEmail: EditText? = null
    private var etPassword: EditText? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)


    }
}
