package com.yasuhiro.ca.find.controller

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.yasuhiro.ca.find.R
import com.yasuhiro.ca.find.adapter.ViewPagerAdapter
import kotlinx.android.synthetic.main.content_place_detail.*
import kotlinx.android.synthetic.main.toolbar_backbutton.*

/*
 *
 * ClassName:PlaceDetailActivity
 * Date:2018/10/15
 * Create by: Yasuhiro Katayama
 *
 */
class PlaceDetailActivity : AppCompatActivity(), View.OnClickListener {

    private var placeImage: ImageView? = null
    private var title: TextView? = null
    private lateinit var viewpageradapter: ViewPagerAdapter

    private var retunButton: TextView? = null

    // variables of data
    private var placeId: String? = null
    private var placeName: String? = null
    private var imageUrl: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_place_detail)
        setSupportActionBar(toolbar)
        viewpageradapter = ViewPagerAdapter(supportFragmentManager)

        this.viewPager.adapter = viewpageradapter
        this.tab_layout.setupWithViewPager(this.viewPager)

        val extras = intent.extras

        placeName = extras.getString("placeName")
        imageUrl = extras.getString("imageUrl")
        placeId = extras.getString("placeId")

        placeImage = findViewById(R.id.placeImage)
        title =  findViewById(R.id.title)

        Glide.with(placeImage)
                .load(imageUrl)
                .into(placeImage)
        title!!.setText(placeName)

        retunButton = findViewById(R.id.backButton)
        retunButton!!.setOnClickListener(this)

    }

    // returnButton function
    private fun returnButton() {
        super.onBackPressed()
    }

    // onClick function
    override fun onClick(v: View) {
        when(v.id) {

            R.id.backButton -> returnButton()

        }
    }
}
