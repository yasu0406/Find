package com.yasuhiro.ca.find.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import com.yasuhiro.ca.find.model.Place
import android.widget.TextView
import com.bumptech.glide.Glide
import com.yasuhiro.ca.find.R

/*
 * ClassName:PlacesAdapter
 * Date:2018/09/10
 * Create by: Yasuhiro Katayama
 *
 */
class PlacesAdapter(context: Context) : BaseAdapter() {

    private var mlayoutInflater: LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    private var mListArray: ArrayList<Place>? = null

    override fun getCount(): Int {
        return mListArray!!.size
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, viewGroup: ViewGroup?): View {

        val convertView = mlayoutInflater!!.inflate(R.layout.listview_row, viewGroup, false)


        var placeName = convertView!!.findViewById<TextView>(R.id.placeName)
        placeName.setText(mListArray!!.get(position).placeName)

        var discription = convertView!!.findViewById<TextView>(R.id.discription)
        discription.setText(mListArray!!.get(position).discription)

        var imageList = convertView!!.findViewById<ImageView>(R.id.imageListView)
        Glide.with(imageList)
                .load(mListArray!!.get(position).imageUrl)
                .into(imageList)

        return convertView
    }
    
    fun setlistArray(listArray: ArrayList<Place>?) {
        mListArray = listArray
    }

    override fun getItem(position: Int): Any {
        return mListArray!!.get(position)
    }

}