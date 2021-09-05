package com.example.vehicletrack.data.network.requests

import com.google.gson.annotations.SerializedName

data class SignUpRequest(
    @SerializedName("firstName")
    var firstName: String? = null,
    @SerializedName("lastName")
    var lastName: String? = null,
    @SerializedName("email")
    var email: String? = null,
    @SerializedName("password")
    var password: String? = null
){


}