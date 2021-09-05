package com.example.vehicletrack.data.repositories

import com.example.vehicletrack.data.db.AppDatabase
import com.example.vehicletrack.data.db.entities.User
import com.example.vehicletrack.data.network.SafeApiRequest
import com.example.vehicletrack.data.network.VehicleTrackApi
import com.example.vehicletrack.data.network.requests.LoginRequest
import com.example.vehicletrack.data.network.requests.SignUpRequest
import com.example.vehicletrack.data.network.responses.AuthResponse

class UserRepository(
    private val api: VehicleTrackApi,
    private val db: AppDatabase,
    private val loginRequest: LoginRequest,
    private val signUpRequest: SignUpRequest
) : SafeApiRequest() {

    suspend fun userLogin(email: String, password: String): AuthResponse {
        var response: AuthResponse
        try{
            loginRequest.email = email
            loginRequest.password = password
            response =  apiRequest { api.userLogin(loginRequest) }
        }catch (e: Exception){
            response =  AuthResponse(null, e.message, null)
        }
        return response
    }

    suspend fun userSignUp(
        firstname: String,
        lastname: String,
        email: String,
        password: String
    ) : AuthResponse {
        var response: AuthResponse
        try{
            signUpRequest.firstName = firstname
            signUpRequest.lastName = lastname
            signUpRequest.email = email
            signUpRequest.password = password
            response =  apiRequest { api.userSignup(signUpRequest) }
        }catch (e: Exception){
            response =  AuthResponse(null, e.message, null)
        }
        return response
    }

    suspend fun saveUser(user: User) = db.getUserDao().upsert(user)

    fun getUser() = db.getUserDao().getuser()

    suspend fun deleteUser(): Int = db.getUserDao().deleteUser()

}