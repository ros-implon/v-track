package com.example.vehicletrack.data.model

import com.google.gson.annotations.SerializedName

data class LocationModel(
    @SerializedName("lat")
    var latitude: Double,
    @SerializedName("lng")
    var longitude: Double
)