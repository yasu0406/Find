package com.yasuhiro.ca.find.controller

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.yasuhiro.ca.find.R
import com.yasuhiro.ca.find.adapter.ViewPagerAdapter
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.activity_place_list.*
import kotlinx.android.synthetic.main.content_place_detail.*
import kotlinx.android.synthetic.main.toolbar_backbutton.*
import kotlinx.android.synthetic.main.toolbar_humbarger.*

/*
 *
 * ClassName:PlaceDetailActivity
 * Date:2018/10/15
 * Create by: Yasuhiro Katayama
 *
 */
class PlaceDetailActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private var placeImage: ImageView? = null
    private var title: TextView? = null
    private lateinit var viewpageradapter: ViewPagerAdapter

    // variables of data
    private var placeId: String? = null
    private var placeName: String? = null
    private var imageUrl: String? = null
    private var loginUserName: String? = null
    private var cUserImageUrl: String? = null
    private var cUserName: TextView? = null
    private var navImageView: CircleImageView? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_place_detail)
        setSupportActionBar(toolbar)
        setSupportActionBar(toolbarNav)

        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbarNav, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)
        viewpageradapter = ViewPagerAdapter(supportFragmentManager)

        this.viewPager.adapter = viewpageradapter
        this.tab_layout.setupWithViewPager(this.viewPager)
        val headerView = nav_view.inflateHeaderView(R.layout.nav_header)
        navImageView = headerView.findViewById(R.id.navImageView)

        Glide.with(navImageView)
                .load(cUserImageUrl)
                .into(navImageView)

        val extras = intent.extras

        placeName = extras.getString("placeName")
        imageUrl = extras.getString("imageUrl")
        placeId = extras.getString("placeId")
        cUserImageUrl = extras.getString("cUserImageUrl")
        loginUserName = extras.getString("loginUserName")


        placeImage = findViewById(R.id.placeImage)
        title =  findViewById(R.id.title)
        cUserName = headerView.findViewById(R.id.navUserName)
        cUserName!!.setText(loginUserName)

        Glide.with(navImageView)
                .load(cUserImageUrl)
                .into(navImageView)

        Glide.with(placeImage)
                .load(imageUrl)
                .into(placeImage)
        title!!.setText(placeName)

    }

    // returnButton function
    private fun returnButton() {
        super.onBackPressed()
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        when (item.itemId) {
            R.id.action_settings -> return true
            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.nav_places -> {
                var intent = Intent(this,PlaceListActivity::class.java)
                startActivity(intent)
            }
            R.id.nav_profile -> {

            }
        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }
}
