package com.yasuhiro.ca.find.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.bumptech.glide.Glide
import com.yasuhiro.ca.find.R
import com.yasuhiro.ca.find.model.Review
import de.hdodenhof.circleimageview.CircleImageView

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

        var userName = convertView!!.findViewById<TextView>(R.id.userName)
        userName.setText(mListArray!!.get(position).userName)

        var reviewContent = convertView!!.findViewById<TextView>(R.id.reviewContent)
        reviewContent.setText(mListArray!!.get(position).reviewContent)

        var imageList = convertView!!.findViewById<CircleImageView>(R.id.reviewImageView)
        Glide.with(imageList)
                .load(mListArray!!.get(position).userImageUrl)
                .into(imageList)

        return convertView
    }
    
    fun setlistArray(listArray: ArrayList<Review>?) {
        mListArray = listArray
    }

    override fun getItem(position: Int): Any {
        return mListArray!!.get(position)
    }

}