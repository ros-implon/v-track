package com.example.vehicletrack.data.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import java.util.*

const val CURRENT_USER_ID = 0

@Entity
data class User(
    @SerializedName("_id")
    var _id: String? = null,
    @SerializedName("firstName")
    var first_name: String? = null,
    @SerializedName("lastName")
    var last_name: String? = null,
    @SerializedName("email")
    var email: String? = null,
    @SerializedName("password")
    var password: String? = null,
    @SerializedName("isVerified")
    var is_verified: Boolean? = null
){
    @PrimaryKey(autoGenerate = false)
    var uid: Int = CURRENT_USER_ID
}