package com.example.vehicletrack.ui.home.account

import androidx.lifecycle.ViewModel
import com.example.vehicletrack.data.repositories.TrackRepository
import com.example.vehicletrack.data.repositories.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AccountViewModel(
    private val userRepository: UserRepository,
    private val trackRepository: TrackRepository
) : ViewModel() {

    val user = userRepository.getUser()

    suspend fun getTracks(
        uid: String
    ) = withContext(Dispatchers.IO){
        trackRepository.getTracks(uid)
    }


}