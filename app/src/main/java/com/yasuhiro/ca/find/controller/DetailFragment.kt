package com.yasuhiro.ca.find.controller

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import com.yasuhiro.ca.find.R

/*
 *
 * ClassName:DetailFragment
 * Date:2018/10/19
 * Create by: Yasuhiro Katayama
 *
 */
class DetailFragment : Fragment() {

    var detailText: TextView? = null
    var relMain: RelativeLayout? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var view: View? = inflater!!.inflate(R.layout.fragment_detail, container, false)

        relMain = view?.findViewById(R.id.rel_main) as RelativeLayout
        detailText = view?.findViewById(R.id.detailTextView) as TextView

        val extras = activity!!.intent.extras

        var discription = extras.getString("discription")
        detailText!!.text = discription

        return view
    }



}
