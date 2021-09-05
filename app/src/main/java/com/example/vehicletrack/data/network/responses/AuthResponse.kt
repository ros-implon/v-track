package com.example.vehicletrack.data.network.responses

import com.example.vehicletrack.data.db.entities.User

data class AuthResponse(
    val token: String?,
    val message: String?,
    val user: User?

)