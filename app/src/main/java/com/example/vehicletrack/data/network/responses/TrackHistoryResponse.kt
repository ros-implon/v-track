package com.example.vehicletrack.data.network.responses

import com.example.vehicletrack.data.db.entities.Track
import com.example.vehicletrack.data.db.entities.User

data class TrackHistoryResponse(
    val message: String?,
    val tracks: List<Track>?
){
}