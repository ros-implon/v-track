package com.example.vehicletrack

import android.app.Application
import com.example.vehicletrack.data.db.AppDatabase
import com.example.vehicletrack.data.db.preferences.PreferenceProvider
import com.example.vehicletrack.data.network.NetworkConnectionInterceptor
import com.example.vehicletrack.data.network.VehicleTrackApi
import com.example.vehicletrack.data.network.requests.LoginRequest
import com.example.vehicletrack.data.network.requests.SignUpRequest
import com.example.vehicletrack.data.network.requests.TrackRequest
import com.example.vehicletrack.data.repositories.TrackRepository
import com.example.vehicletrack.data.repositories.UserRepository
import com.example.vehicletrack.ui.auth.AuthViewModelFactory
import com.example.vehicletrack.ui.home.account.AccountViewModel
import com.example.vehicletrack.ui.home.account.AccountViewModelFactory
import com.example.vehicletrack.ui.home.track.TrackViewModelFactory
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.androidXModule
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton

class MyApplication: Application(), KodeinAware{

    override val kodein = Kodein.lazy {
        import(androidXModule(this@MyApplication))

        bind() from singleton { NetworkConnectionInterceptor(instance()) }
        bind() from singleton { VehicleTrackApi(instance()) }
        bind() from singleton { AppDatabase(instance()) }
        bind() from provider { LoginRequest("", "") }
        bind() from provider { SignUpRequest("","", "", "") }
        bind() from provider { TrackRequest("", "") }
        //bind() from singleton { PreferenceProvider(instance()) }
        bind() from singleton { UserRepository(instance(), instance(), instance(), instance())}
        bind() from singleton { TrackRepository(instance(), instance(), instance())}
        bind() from provider { AuthViewModelFactory(instance()) }
        bind() from provider { TrackViewModelFactory(instance(), instance(), instance()) }
        bind() from provider { AccountViewModelFactory(instance(), instance()) }
        bind() from singleton { PreferenceProvider(instance()) }
    }
}