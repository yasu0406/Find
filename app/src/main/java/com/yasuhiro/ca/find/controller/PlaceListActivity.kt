package com.yasuhiro.ca.find.controller

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.ListView
import com.google.firebase.database.*
import com.yasuhiro.ca.find.R
import com.yasuhiro.ca.find.adapter.PlacesAdapter
import com.yasuhiro.ca.find.entity.Const.Companion.PLACE_DBPATH
import com.yasuhiro.ca.find.model.Place
import kotlinx.android.synthetic.main.activity_place_list.*
import kotlinx.android.synthetic.main.toolbar_humbarger.*


/*
 *
 * ClassName:PlaceListActivity
 * Date:2018/09/10
 * Create by: Yasuhiro Katayama
 *
 */
class PlaceListActivity : AppCompatActivity() {

    // variables of Firebase
    private var mDatabaseReference: DatabaseReference? = null
    private var mListView: ListView? = null
    private var listPlaceArrayList: ArrayList<Place>? = null
    private var placeAddapter: PlacesAdapter? = null

    // variables of data
    private var uid: String? = null
    private var placeId: String? = null
    private var placeName: String? = null
    private var discription: String? = null
    private var address: String? = null
    private var imageUrl: String? = null
    private var image: String? = null
    private var placeMap: MutableMap<String, Any>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_place_list)
        setSupportActionBar(toolbar)

        fab.setOnClickListener { view ->
            var intent = Intent(this,RegistPlaceActivity::class.java)
            startActivity(intent)
        }
        // call FirebaseDatabase
        mDatabaseReference = FirebaseDatabase.getInstance().getReference(PLACE_DBPATH)
        // call listView
        mListView = findViewById(R.id.listPlacesView)
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
            intent.putExtra("placeImageUrl", place.imageUrl)
            startActivity(intent)
        }
    }

}
