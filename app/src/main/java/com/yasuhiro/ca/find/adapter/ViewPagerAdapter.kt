package com.yasuhiro.ca.find.adapter

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import com.yasuhiro.ca.find.controller.DetailFragment
import com.yasuhiro.ca.find.controller.MapFragment
import com.yasuhiro.ca.find.controller.ReviewsFragment


/*
 *
 * ClassName:ViewPagerAdapter
 * Date:2018/10/19
 * Create by: Yasuhiro Katayama
 *
 */
class ViewPagerAdapter(fm: FragmentManager): FragmentPagerAdapter(fm) {
    override fun getItem(position: Int): Fragment? {
        var fragment: Fragment? = null
        if(position == 0) {
            fragment = DetailFragment()
        } else if(position == 1) {
            fragment = ReviewsFragment()
        } else if(position == 2) {
            fragment = MapFragment()
        }

        return fragment
    }

    override fun getCount(): Int {
        return 3
    }

    override fun getPageTitle(position: Int): CharSequence? {
        var title: String? = null
        if(position == 0) {
            title = "Detail"
        } else if(position == 1) {
            title = "Reviews"
        } else if(position == 2) {
            title = "Map"
        }
        return title
    }
}