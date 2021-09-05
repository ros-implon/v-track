package com.example.vehicletrack.ui.home.track

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.vehicletrack.data.model.LocationModel
import com.example.vehicletrack.data.repositories.TrackRepository
import com.example.vehicletrack.data.repositories.UserRepository
import com.example.vehicletrack.util.LocationLiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class TrackViewModel(
    application: Application,
    private val trackRepository: TrackRepository,
    private val userRepository: UserRepository
) : AndroidViewModel(application) {

    private val locationData = LocationLiveData(application)

    fun getLocationData() = locationData

    fun getLoggedInUser() = userRepository.getUser()

    suspend fun logoutUser(): Int = userRepository.deleteUser()

    suspend fun addTrack(
        user_id: String,
        locationList: Array<LocationModel>,
        date: String
    ) = withContext(Dispatchers.IO){

        trackRepository.addTrack(user_id, locationList, date)
    }
}