package com.yasuhiro.ca.find.controller

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.yasuhiro.ca.find.R

class PlaceDetailActivity : AppCompatActivity() {

    private var placeImage: ImageView? = null
    private var title: TextView? = null
    private var discription: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_place_detail)

        val extras = intent.extras

        var placeName = extras.get("placeName")
        var placeDiscription = extras.get("discription")
        var placeImageUrl = extras.get("placeImageUrl")

        placeImage = findViewById(R.id.placeImage)
        title =  findViewById(R.id.title)
        discription = findViewById(R.id.discription)

        Glide.with(placeImage)
                .load(placeImageUrl)
                .into(placeImage)
        title!!.setText(placeName as String)
        discription!!.setText(placeDiscription as String)

    }
}
