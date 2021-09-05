package com.example.vehicletrack.data.network

import com.example.vehicletrack.data.network.requests.LoginRequest
import com.example.vehicletrack.data.network.requests.SignUpRequest
import com.example.vehicletrack.data.network.requests.TrackRequest
import com.example.vehicletrack.data.network.responses.AuthResponse
import com.example.vehicletrack.data.network.responses.TrackHistoryResponse
import com.example.vehicletrack.data.network.responses.TrackResponse
import okhttp3.OkHttpClient
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface VehicleTrackApi {


    @POST("user/login")
    suspend fun userLogin(
        @Body loginRequest: LoginRequest
    ) : Response<AuthResponse>


    @POST("/user/signup")
    suspend fun userSignup(
        @Body signUpRequest: SignUpRequest
    ) : Response<AuthResponse>

    @POST("/tracks/addtrack")
    suspend fun addTrack(
        @Body trackRequest: TrackRequest
    ) : Response<TrackResponse>

    @FormUrlEncoded
    @POST("/tracks/getTracks")
    suspend fun getTracks(
        @Field("id") userId: String
    ) : Response<TrackHistoryResponse>


    companion object{
        operator fun invoke(
            networkConnectionInterceptor: NetworkConnectionInterceptor
        ) : VehicleTrackApi{

            val okkHttpclient = OkHttpClient.Builder()
                .addInterceptor(networkConnectionInterceptor)
                .build()

            return Retrofit.Builder()
                .client(okkHttpclient)
                .baseUrl("http://192.168.43.244:3000/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(VehicleTrackApi::class.java)
        }
    }
}