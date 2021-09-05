package com.example.vehicletrack.data.repositories

import com.example.vehicletrack.data.db.AppDatabase
import com.example.vehicletrack.data.model.LocationModel
import com.example.vehicletrack.data.network.SafeApiRequest
import com.example.vehicletrack.data.network.VehicleTrackApi
import com.example.vehicletrack.data.network.requests.TrackRequest
import com.example.vehicletrack.data.network.responses.TrackHistoryResponse
import com.example.vehicletrack.data.network.responses.TrackResponse

class TrackRepository(
    private val db: AppDatabase,
    private val api: VehicleTrackApi,
    private val trackRequest: TrackRequest
) : SafeApiRequest() {

    suspend fun addTrack(
        user_id: String,
        locationList: Array<LocationModel>,
        date: String
    ): TrackResponse{
        var response: TrackResponse
        try{
            trackRequest.userId = user_id
            trackRequest.track = locationList
            trackRequest.date = date
            response =  apiRequest { api.addTrack(trackRequest) }
        }catch (e: Exception){
            response =  TrackResponse(e.message)
        }
        return response
    }

    suspend fun getTracks(
        uid: String
    ): TrackHistoryResponse {
        var response: TrackHistoryResponse
        try{
            response =  apiRequest { api.getTracks(uid) }
        }catch (e: Exception){
            response =  TrackHistoryResponse(e.message, null)
        }
        return response
    }

}