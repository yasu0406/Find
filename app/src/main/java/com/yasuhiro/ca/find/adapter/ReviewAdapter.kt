package com.yasuhiro.ca.find.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.yasuhiro.ca.find.R
import com.yasuhiro.ca.find.model.Review

/*
 * ClassName:ReviewAdapter
 * Date:2018/10/23
 * Create by: Yasuhiro Katayama
 *
 */
class ReviewAdapter(context: Context) : BaseAdapter() {

    private var mlayoutInflater: LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    private var mListArray: ArrayList<Review>? = null

    override fun getCount(): Int {
        return mListArray!!.size
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, viewGroup: ViewGroup?): View {

        val convertView = mlayoutInflater!!.inflate(R.layout.listview_review, viewGroup, false)


        var reviewTitle = convertView!!.findViewById<TextView>(R.id.reviewTitle)
        reviewTitle.setText(mListArray!!.get(position).reviewTitle)

        var reviewContent = convertView!!.findViewById<TextView>(R.id.reviewContent)
        reviewContent.setText(mListArray!!.get(position).reviewContent)

//        var imageList = convertView!!.findViewById<ImageView>(R.id.imageListView)
//        Glide.with(imageList)
//                .load(mListArray!!.get(position).imageUrl)
//                .into(imageList)

        return convertView
    }
    
    fun setlistArray(listArray: ArrayList<Review>?) {
        mListArray = listArray
    }

    override fun getItem(position: Int): Any {
        return mListArray!!.get(position)
    }

}