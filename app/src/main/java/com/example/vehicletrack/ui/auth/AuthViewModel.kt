package com.example.vehicletrack.ui.auth

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.vehicletrack.data.db.entities.User
import com.example.vehicletrack.data.repositories.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AuthViewModel(
    private val repository: UserRepository
): ViewModel() {

    fun getLoggedInUser() = repository.getUser()

    suspend fun userLogin(
        email: String,
        password: String
    ) = withContext(Dispatchers.IO) {
        repository.userLogin(email, password) }

    suspend fun userSignUp(
        firstname: String,
        lastname: String,
        email: String,
        password: String
    ) = withContext(Dispatchers.IO) { repository.userSignUp(firstname, lastname, email, password) }

    suspend fun saveLoggedInUser(user: User) = repository.saveUser(user)

    suspend fun deleteLoggedInUser() = repository.deleteUser()


}