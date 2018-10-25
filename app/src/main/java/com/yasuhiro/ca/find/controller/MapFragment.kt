package com.yasuhiro.ca.find.controller

import android.location.Address
import android.location.Geocoder
import android.support.v4.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.yasuhiro.ca.find.R
import java.io.IOException
import java.util.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback




/*
 *
 * ClassName:MapFragment
 * Date:2018/10/19
 * Create by: Yasuhiro Katayama
 *
 */
class MapFragment: Fragment(), OnMapReadyCallback {
    private var relMain: RelativeLayout? = null
    private var mMap: MapView? = null
    private var lstAddr: List<Address>? = null
    private var search_key: String? = null
    private var maxResults = 1
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var view: View? = inflater!!.inflate(R.layout.fragment_map, container, false)
        relMain = view?.findViewById(R.id.rel_main) as RelativeLayout
        mMap = view?.findViewById(R.id.map) as MapView
        mMap!!.onCreate(savedInstanceState)
        mMap!!.onResume()
        try {
            MapsInitializer.initialize(activity!!.applicationContext)
        } catch (e : Exception) {
            e.printStackTrace()
        }

        val extras = activity!!.intent.extras
        search_key = extras.getString("address")

        mMap!!.getMapAsync(this)

        return view
    }

    override fun onMapReady(mMap: GoogleMap?) {
        var map = mMap
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        var gcoder = Geocoder(this.context, Locale.getDefault())
        try {
            lstAddr = gcoder.getFromLocationName(search_key, maxResults)
        } catch (e: IOException) {
            e.printStackTrace()
        }

        if (lstAddr != null && lstAddr!!.size > 0) {
            val addr = lstAddr!![0]
            val latitude = addr.latitude
            val longitude = addr.longitude

            val zoomLevel = 16.0f //This goes up to 21
            val location = LatLng(latitude, longitude)
            map!!.addMarker(MarkerOptions().position(location))
            map!!.moveCamera(CameraUpdateFactory.newLatLngZoom(location, zoomLevel))
        }
    }

    override fun onResume() {
        super.onResume()
        mMap!!.onResume()
    }

    override fun onPause() {
        super.onPause()
        mMap!!.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        mMap!!.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mMap!!.onLowMemory()
    }
}