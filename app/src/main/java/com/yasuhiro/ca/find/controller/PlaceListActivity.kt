package com.yasuhiro.ca.find.controller

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.ListView
import com.google.firebase.database.*
import com.yasuhiro.ca.find.R
import com.yasuhiro.ca.find.adapter.PlacesAdapter
import com.yasuhiro.ca.find.entity.const.Companion.PlacesPath
import com.yasuhiro.ca.find.model.Place

import kotlinx.android.synthetic.main.activity_place_list.*

class PlaceListActivity : AppCompatActivity() {

    private var mDatabaseReference: DatabaseReference? = null
    private var mListView: ListView? = null
    private var listPlaceArrayList: ArrayList<Place>? = null
    private var placeAddapter: PlacesAdapter? = null

    private var uid: String? = null
    private var placeName: String? = null
    private var discription: String? = null
    private var address: String? = null
    private var placeMap: MutableMap<String, Any>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_place_list)
        setSupportActionBar(toolbar)

        fab.setOnClickListener { view ->
            var intent = Intent(this,RegistPlaceActivity::class.java)
            startActivity(intent)
        }
        mDatabaseReference = FirebaseDatabase.getInstance().getReference(PlacesPath)
        mListView = findViewById(R.id.listPlacesView) as ListView
        listPlaceArrayList = ArrayList<Place>()
        placeAddapter = PlacesAdapter(this)
        listPlaceArrayList!!.clear()
        placeAddapter!!.setlistArray(listPlaceArrayList)
        mListView!!.setAdapter(placeAddapter)
        placeList()

    }

    private fun placeList() {
        var mChildEventListener =
        object : ChildEventListener {
            override fun onChildAdded(dataSnapshot: DataSnapshot, p1: String?) {
                placeMap = dataSnapshot.getValue() as MutableMap<String, Any>
                uid = placeMap!!.get("uid") as String
                placeName = placeMap!!.get("placeName") as String
                discription = placeMap!!.get("discription") as String
                address = placeMap!!.get("address") as String

                var place = Place(placeName, discription, address)
                listPlaceArrayList!!.add(place)
                placeAddapter!!.notifyDataSetChanged()
            }

            override fun onChildChanged(dataSnapshot: DataSnapshot, p1: String?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
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

}
