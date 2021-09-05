package com.example.vehicletrack.data.db.preferences

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import com.example.vehicletrack.data.db.entities.User
import com.google.gson.Gson

private const val KEY_SAVED_AT = "key_user"

class PreferenceProvider(
    context: Context
) {

    private val appContext = context.applicationContext

    private val preference: SharedPreferences
        get() = PreferenceManager.getDefaultSharedPreferences(appContext)


    fun saveUser(user: User) {
        val userString = Gson().toJson(user)
        preference.edit().putString(
            KEY_SAVED_AT,
            userString
        ).apply()
    }

    fun getUser(): User? {
        return Gson().fromJson(preference.getString(KEY_SAVED_AT, null), User::class.java)
    }

    fun deleteUser(){
        preference.edit().clear().apply()
    }

}