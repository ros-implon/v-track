package com.example.vehicletrack.data.network.requests

import com.example.vehicletrack.data.model.LocationModel
import com.google.gson.annotations.SerializedName
import java.sql.Date

data class TrackRequest(
    @SerializedName("user_id")
    var userId: String,
    @SerializedName("date")
    var date: String
){

    @SerializedName("track")
    var track: Array<LocationModel>? = null


}