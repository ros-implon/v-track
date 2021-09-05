package com.example.vehicletrack.data.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.vehicletrack.data.model.LocationModel
import com.google.gson.annotations.SerializedName


@Entity
data class Track(
    @PrimaryKey(autoGenerate = false)
    @SerializedName("_id")
    var _id: String,
    @SerializedName("user_id")
    var user_id: String,
    @SerializedName("date")
    var date: String
){
    @SerializedName("track")
    var track: Array<LocationModel>? = null
}