package com.example.vehicletrack.ui.home.track

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.vehicletrack.data.repositories.TrackRepository
import com.example.vehicletrack.data.repositories.UserRepository

class TrackViewModelFactory(
    private val application: Application,
    private val userRepository: UserRepository,
    private val trackRepository: TrackRepository
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return TrackViewModel(application, trackRepository, userRepository) as T
    }
}