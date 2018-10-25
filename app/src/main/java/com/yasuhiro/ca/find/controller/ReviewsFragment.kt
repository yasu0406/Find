package com.yasuhiro.ca.find.controller

import android.content.Intent
import android.support.v4.app.Fragment
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import android.widget.RelativeLayout
import com.google.firebase.database.*
import com.yasuhiro.ca.find.R
import com.yasuhiro.ca.find.adapter.ReviewAdapter
import com.yasuhiro.ca.find.entity.Const.Companion.PLACE_DBPATH
import com.yasuhiro.ca.find.entity.Const.Companion.REVIEW_DBPATH
import com.yasuhiro.ca.find.model.Review

/*
 *
 * ClassName:ReviewsFragment
 * Date:2018/10/19
 * Create by: Yasuhiro Katayama
 *
 */
class ReviewsFragment: Fragment() {
    // variables of Firebase
    private var mDatabaseReference: DatabaseReference? = null
    private var mListView: ListView? = null
    private var listReviewArrayList: ArrayList<Review>? = null
    private var reviewAddapter: ReviewAdapter? = null

    private var uid: String? = null
    private var placeId: String? = null
    private var reviewTitle: String? = null
    private var reviewContent: String? = null
    private var reviesMap: MutableMap<String, Any>? = null
    private var placeName: String? = null
    private var discription: String? = null
    private var address: String? = null
    private var imageUrl: String? = null

    var relMain: RelativeLayout? = null
    var fab: FloatingActionButton? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var view: View? = inflater!!.inflate(R.layout.fragment_reviews, container, false)

        relMain = view?.findViewById(R.id.rel_main)
        fab = view!!.findViewById(R.id.fab)
        val extras = activity!!.intent.extras

        placeId = extras.getString("placeId")
        placeName = extras.getString("placeName")
        imageUrl = extras.getString("imageUrl")
        discription = extras.getString("discription")
        address = extras.getString("address")

        // call FirebaseDatabase
        mDatabaseReference = FirebaseDatabase.getInstance().getReference(PLACE_DBPATH)
        // call listView
        mListView = view.findViewById(R.id.reviewList)
        mListView!!.setDivider(null)
        // set ArrayList<Review>
        listReviewArrayList = ArrayList<Review>()
        // set ReviewAdapter
        reviewAddapter = ReviewAdapter(view.context)
        // clear listReviewArrayList
        listReviewArrayList!!.clear()
        // set listReviewArrayList for reviewAddapter
        reviewAddapter!!.setlistArray(listReviewArrayList)
        // set reviewAddapter for mListView
        mListView!!.setAdapter(reviewAddapter)

        showReviews()

        fab!!.setOnClickListener {view ->
            var intent = Intent(view.context, ReviewWriteActivity::class.java)
            intent.putExtra("placeId", placeId)
            intent.putExtra("placeName", placeName)
            intent.putExtra("discription", discription)
            intent.putExtra("address", address)
            intent.putExtra("imageUrl", imageUrl)
            startActivity(intent)
        }
        return view
    }

    private fun showReviews() {
        var mChildEventListener =
                object : ChildEventListener {
                    override fun onChildAdded(dataSnapshot: DataSnapshot, p1: String?) {
                        reviesMap = dataSnapshot.getValue() as MutableMap<String, Any>
                        uid = reviesMap!!.get("uid") as String
                        reviewTitle = reviesMap!!.get("reviewTitle") as String
                        reviewContent = reviesMap!!.get("reviewContent") as String

                        var review = Review(uid, placeId, reviewTitle, reviewContent)
                        listReviewArrayList!!.add(review)
                        reviewAddapter!!.notifyDataSetChanged()
                    }

                    override fun onChildChanged(dataSnapshot: DataSnapshot, p1: String?) {
                        reviesMap = dataSnapshot.getValue() as MutableMap<String, Any>
                        uid = reviesMap!!.get("uid") as String
                        reviewTitle = reviesMap!!.get("reviewTitle") as String
                        reviewContent = reviesMap!!.get("reviewContent") as String

                        var review = Review(uid, placeId, reviewTitle, reviewContent)
                        listReviewArrayList!!.add(review)
                        reviewAddapter!!.notifyDataSetChanged()
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

        mDatabaseReference!!.child(placeId!!).child(REVIEW_DBPATH).addChildEventListener(mChildEventListener)
    }
}