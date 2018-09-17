package com.yasuhiro.ca.find.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter

import com.yasuhiro.ca.find.model.Place
import android.widget.TextView
import com.yasuhiro.ca.find.R


class ListAdapter : BaseAdapter() {

    private var mlayoutInflater: LayoutInflater? = null
    private var mListArray: ArrayList<Place>? = null

    private fun ChatListAdapter(context: Context) {
        mlayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater?
    }

    override fun getCount(): Int {
        return mListArray!!.size
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, viewGroup: ViewGroup?): View {

        if(convertView == null) {
           var convertView = mlayoutInflater!!.inflate(R.layout.listview_row, viewGroup, false)
        }

        var titleList = convertView!!.findViewById<TextView>(R.id.textList)
        titleList.setText(mListArray!!.get(position).placeName)
        var content = convertView!!.findViewById<TextView>(R.id.textList)
        content.setText(mListArray!!.get(position).discription)

        return convertView
    }
    
    private fun setlistArray(listArray: ArrayList<Place>) {
        mListArray = listArray
    }

    override fun getItem(p0: Int): Any {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}