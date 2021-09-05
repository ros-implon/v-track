package com.example.vehicletrack.ui.home.track

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.lifecycleScope
import com.example.vehicletrack.R
import com.example.vehicletrack.data.db.entities.User
import com.example.vehicletrack.data.model.LocationModel
import com.example.vehicletrack.databinding.TrackFragmentBinding
import com.example.vehicletrack.ui.home.HomeActivity
import com.example.vehicletrack.util.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.track_fragment.*
import kotlinx.coroutines.launch
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.ArrayList

class TrackFragment : Fragment(), KodeinAware, OnMapReadyCallback {

    override val kodein by kodein()
    private val factory: TrackViewModelFactory by instance()

    private lateinit var trackViewModel: TrackViewModel
    private lateinit var binding: TrackFragmentBinding
    private var isGPSEnabled = false
    private var currentLocation: Location? = null
    private val locationList = ArrayList<LocationModel>()
    private var trackingStatus = false
    private var user: User? = null

    private var mContext: Context? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.track_fragment, container, false)
        trackViewModel = ViewModelProviders.of(this, factory).get(TrackViewModel::class.java)

        mContext?.let {
            GpsUtils(it).turnGPSOn(object : GpsUtils.OnGpsListener {

                override fun gpsStatus(isGPSEnable: Boolean) {
                    isGPSEnabled = isGPSEnable
                }
            })
        }

        getUser()

        binding.currentLocationButton.setOnClickListener {
            fetchMapLocation()
        }

        binding.buttonTrack.setOnClickListener {
            tracking()
        }

        return binding.root
    }

    private fun invokeLocationAction() {
        when {
            !isGPSEnabled -> mContext!!.toast("GPS not enabled. Go to settings and enable GPS for this app")

            isPermissionsGranted() -> startLocationUpdate()

            shouldShowRequestPermissionRationale() -> mContext!!.toast("You have denied Location permission. Go to settings and allow Location for this app")

            else -> requestPermissions(
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION),
                LOCATION_REQUEST
            )
        }
    }

    private fun startLocationUpdate() {
        trackViewModel.getLocationData().observe(this, Observer {
            Log.e("TrackFragment", it.toString())
            currentLocation = it
            if(trackingStatus){
                locationList.add(LocationModel(it.latitude, it.longitude))
            }
            fetchMapLocation()
        })
    }

    private fun isPermissionsGranted() =
        ContextCompat.checkSelfPermission(
            requireActivity(),
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(
                    requireActivity(),
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED

    private fun shouldShowRequestPermissionRationale() =
        ActivityCompat.shouldShowRequestPermissionRationale(
            requireActivity(),
            Manifest.permission.ACCESS_FINE_LOCATION
        ) && ActivityCompat.shouldShowRequestPermissionRationale(
            requireActivity(),
            Manifest.permission.ACCESS_COARSE_LOCATION
        )

    @SuppressLint("MissingPermission")
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            LOCATION_REQUEST -> {
                invokeLocationAction()
            }
        }
    }

    private fun getUser(){
        trackViewModel.getLoggedInUser().observe(viewLifecycleOwner, Observer {
            user = it
        })
    }

    private fun fetchMapLocation(){
        if(currentLocation!=null) {
            val supportMapFragment = (childFragmentManager.findFragmentById(R.id.map_view) as SupportMapFragment?)!!
            supportMapFragment.getMapAsync(this)
        }

    }

    private fun tracking(){
        if(!trackingStatus) {
            trackingStatus = true
            binding.buttonTrack.text = getText(R.string.stop_tracking)
        }else{
            binding.homeProgressBarLayout.show()
            trackingStatus = false
            binding.buttonTrack.text = getText(R.string.start_tracking)

            val listData = locationList.toTypedArray()
            locationList.clear()
            lifecycleScope.launch {
                try {
                    val date = getFormattedDate()
                    val trackResponse = trackViewModel.addTrack(user?._id!!, listData, date)
                    Log.e("track", trackResponse.toString())
                    binding.homeProgressBarLayout.hide()
                    binding.homeRootLayout.snackbar(trackResponse.message!!)

                } catch (e: ApiException) {
                    binding.homeProgressBarLayout.hide()
                    binding.homeRootLayout.snackbar(e.message.toString())
                    e.printStackTrace()
                } catch (e: NoInternetException) {
                    binding.homeProgressBarLayout.hide()
                    binding.homeRootLayout.snackbar(e.message.toString())
                    e.printStackTrace()
                }
            }
        }
    }

    private fun getFormattedDate(): String{
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val current = LocalDateTime.now()
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
            current.format(formatter)
        } else {
            val date = Date()
            val formatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
            formatter.format(date)
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    override fun onDetach() {
        super.onDetach()
        mContext = null
    }

    override fun onStart() {
        super.onStart()
        invokeLocationAction()
    }

    override fun onMapReady(googleMap: GoogleMap?) {
        val latLng = LatLng(currentLocation!!.latitude, currentLocation!!.longitude)
        val markerOptions = MarkerOptions().position(latLng).icon(bitMapFromVector(R.drawable.ic_baseline_brightness_1_24)).title("I am here!")
        googleMap!!.animateCamera(CameraUpdateFactory.newLatLng(latLng))
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 17f))
        googleMap.clear()
        googleMap.addMarker(markerOptions)
    }

    private fun bitMapFromVector(vectorResID:Int): BitmapDescriptor {
        val vectorDrawable= ContextCompat.getDrawable(this.mContext!!, vectorResID)
        vectorDrawable!!.setBounds(0,0, vectorDrawable.intrinsicWidth,vectorDrawable.intrinsicHeight)
        val bitmap=
            Bitmap.createBitmap(vectorDrawable.intrinsicWidth,vectorDrawable.intrinsicHeight,Bitmap.Config.ARGB_8888)
        val canvas= Canvas(bitmap)
        vectorDrawable.draw(canvas)
        return BitmapDescriptorFactory.fromBitmap(bitmap)
    }


}
const val LOCATION_REQUEST = 100
const val GPS_REQUEST = 101