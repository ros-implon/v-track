package com.example.vehicletrack.ui.home.track

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.vehicletrack.R
import com.example.vehicletrack.data.db.entities.Track
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.PolylineOptions
import kotlinx.android.synthetic.main.item_track_history.view.*
import java.text.SimpleDateFormat
import java.util.*


class TrackItemAdapter(private val tracks: List<Track>) : RecyclerView.Adapter<TrackItemAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_track_history, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = tracks.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(tracks[position])


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), OnMapReadyCallback {

        var gMap: GoogleMap? = null
        var map: MapView? = null

        @SuppressLint("SimpleDateFormat")
        fun bind(track: Track) {
            with(itemView){
                val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
                val outputFormat =  SimpleDateFormat("dd-MM-yyyy, HH:mm")

                date.text = outputFormat.format(inputFormat.parse(track.date)!!)


                map = map_recycler as MapView
                map!!.onCreate(null)
                map!!.onResume()
                map!!.getMapAsync(this@ViewHolder)

            }

        }

        override fun onMapReady(p0: GoogleMap?) {
            gMap = p0

            val polylineOptions = PolylineOptions().geodesic(true).color(Color.BLUE).width(10f)
            val position = position
            val builder = LatLngBounds.Builder()

            for (i in tracks[position].track!!.indices){
                val latitude = tracks[position].track!![i].latitude
                val longitude = tracks[position].track!![i].longitude
                builder.include(LatLng(latitude, longitude))
                polylineOptions.add(LatLng(latitude, longitude))
            }
            val bounds = builder.build()
            gMap!!.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 10))
            gMap!!.addPolyline(polylineOptions)
        }


    }
}