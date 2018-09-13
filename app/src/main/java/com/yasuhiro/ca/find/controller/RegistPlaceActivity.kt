package com.yasuhiro.ca.find.controller

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.yasuhiro.ca.find.R

class RegistPlaceActivity : AppCompatActivity() {

    private var retunButtom: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_regist_place)
        retunButtom = findViewById(R.id.backButton) as TextView
        retunButtom!!.setOnClickListener {
            returnButton()
        }

    }

    private fun returnButton() {
        super.onBackPressed()
    }
}
