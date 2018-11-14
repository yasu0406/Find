package com.yasuhiro.ca.find.controller

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.widget.ListView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.yasuhiro.ca.find.R
import com.yasuhiro.ca.find.adapter.PlacesAdapter
import com.yasuhiro.ca.find.entity.Const.Companion.PLACE_DBPATH
import com.yasuhiro.ca.find.entity.Const.Companion.USER_DBPATH
import com.yasuhiro.ca.find.model.Place
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.activity_place_list.*
import kotlinx.android.synthetic.main.app_bar_place_list.*
import kotlinx.android.synthetic.main.toolbar_humbarger.*




/*
 *
 * ClassName:PlaceListActivity
 * Date:2018/09/10
 * Create by: Yasuhiro Katayama
 *
 */
class PlaceListActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    // variables of Firebase
    private var mDatabaseReference: DatabaseReference? = null
    private var uDatabaseReference: DatabaseReference? = null
    private var mListView: ListView? = null
    private var listPlaceArrayList: ArrayList<Place>? = null
    private var placeAddapter: PlacesAdapter? = null

    // variables of data
    private var userId: String? = null
    private var uid: String? = null
    private var placeId: String? = null
    private var placeName: String? = null
    private var discription: String? = null
    private var address: String? = null
    private var imageUrl: String? = null
    private var loginUserName: String? = null
    private var cUserImageUrl: String? = null
    private var cUserName: TextView? = null
    private var navImageView: CircleImageView? = null
    private var placeMap: MutableMap<String, Any>? = null
    private var userMap: MutableMap<String, Any>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_place_list)
        setSupportActionBar(toolbarNav)

        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbarNav, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)

        // call FirebaseDatabase
        mDatabaseReference = FirebaseDatabase.getInstance().getReference(PLACE_DBPATH)
        uDatabaseReference = FirebaseDatabase.getInstance().getReference(USER_DBPATH)

        getUser()

        // call listView
        mListView = findViewById(R.id.listPlacesView)
        mListView!!.setDivider(null)
        // set ArrayList<Place>
        listPlaceArrayList = ArrayList<Place>()
        // set PlacesAdapter(this)
        placeAddapter = PlacesAdapter(this)
        // clear listPlaceArrayList
        listPlaceArrayList!!.clear()
        // set listPlaceArrayList for placeAddapter
        placeAddapter!!.setlistArray(listPlaceArrayList)
        // set placeAddapter for mListView
        mListView!!.setAdapter(placeAddapter)
        // call placeList fun
        showPlaceList()

        // call mListViewClick fun
        mListViewClick()

        fab.setOnClickListener { view ->
            var intent = Intent(this,RegistPlaceActivity::class.java)
            startActivity(intent)
        }

    }


    private fun getUser() {
        // get loginUser
        val user = FirebaseAuth.getInstance().currentUser
        userId = user!!.uid

        if (user == null) {
            val intent = Intent(applicationContext, MainActivity::class.java)
            startActivity(intent)
        } else {
            var uChildEventListener =
                    object : ValueEventListener {
                        override fun onDataChange(userDataSnapshot: DataSnapshot) {
                            userMap = userDataSnapshot.getValue() as MutableMap<String, Any>
                            loginUserName = userMap!!.get("userName") as String
                            cUserImageUrl = userMap!!.get("imageUrl") as String
                            val headerView = nav_view.inflateHeaderView(R.layout.nav_header)
                            navImageView = headerView.findViewById(R.id.navImageView)
                            cUserName = headerView.findViewById(R.id.navUserName)
                            cUserName!!.setText(loginUserName)

                            Glide.with(navImageView)
                                    .load(cUserImageUrl)
                                    .into(navImageView)
                        }

                        override fun onCancelled(userDataSnapshot: DatabaseError) {

                        }

                    }
            uDatabaseReference!!.child(userId!!).addListenerForSingleValueEvent(uChildEventListener)
        }
    }

    /*
     *
     *
     *
     */
    private fun showPlaceList() {
        var mChildEventListener =
        object : ChildEventListener {
            override fun onChildAdded(dataSnapshot: DataSnapshot, p1: String?) {
                placeMap = dataSnapshot.getValue() as MutableMap<String, Any>
                uid = placeMap!!.get("uid") as String
                placeId = dataSnapshot.key as String
                placeName = placeMap!!.get("placeName") as String
                discription = placeMap!!.get("discription") as String
                address = placeMap!!.get("address") as String
                imageUrl = placeMap!!.get("imageUrl") as String

                var place = Place(placeId, placeName, discription, address, imageUrl)
                listPlaceArrayList!!.add(place)
                placeAddapter!!.notifyDataSetChanged()
            }

            override fun onChildChanged(dataSnapshot: DataSnapshot, p1: String?) {
                placeMap = dataSnapshot.getValue() as MutableMap<String, Any>
                uid = placeMap!!.get("uid") as String
                placeId = dataSnapshot.key as String
                placeName = placeMap!!.get("placeName") as String
                discription = placeMap!!.get("discription") as String
                address = placeMap!!.get("address") as String
                imageUrl = placeMap!!.get("imageUrl") as String

                placeAddapter!!.notifyDataSetChanged()
            }

            override fun onCancelled(databaseError: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onChildRemoved(dataSnapshot: DataSnapshot) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onChildMoved(dataSnapshot: DataSnapshot, p1: String?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }
        }

        mDatabaseReference!!.addChildEventListener(mChildEventListener)

    }


    /*
     *
     *
     *
     */
    private fun mListViewClick() {
        mListView!!.setOnItemClickListener { parent, view, position, id ->
            var intent = Intent(view.context, PlaceDetailActivity::class.java)
            var place = this.listPlaceArrayList!![position]
            intent.putExtra("placeId", place.placeId)
            intent.putExtra("placeName", place.placeName)
            intent.putExtra("discription", place.discription)
            intent.putExtra("address", place.address)
            intent.putExtra("imageUrl", place.imageUrl)
            startActivity(intent)
        }
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
                intent.putExtra("cUserImageUrl", cUserImageUrl)
                intent.putExtra("loginUserName", loginUserName)
                startActivity(intent)
            }
            R.id.nav_profile -> {

            }
        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

}
