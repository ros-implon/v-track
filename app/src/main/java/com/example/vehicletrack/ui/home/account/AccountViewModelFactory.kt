package com.example.vehicletrack.ui.home.account

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.vehicletrack.data.repositories.TrackRepository
import com.example.vehicletrack.data.repositories.UserRepository

@Suppress("UNCHECKED_CAST")
class AccountViewModelFactory(
    private val userRepository: UserRepository,
    private val trackRepository: TrackRepository
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return AccountViewModel(userRepository, trackRepository) as T
    }
}