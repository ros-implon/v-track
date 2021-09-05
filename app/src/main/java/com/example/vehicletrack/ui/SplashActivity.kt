package com.example.vehicletrack.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.vehicletrack.R
import com.example.vehicletrack.ui.auth.AuthViewModel
import com.example.vehicletrack.ui.auth.AuthViewModelFactory
import com.example.vehicletrack.ui.auth.LoginActivity
import com.example.vehicletrack.ui.home.HomeActivity
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance

class SplashActivity : AppCompatActivity(), KodeinAware {

    override val kodein by kodein()
    private val factory: AuthViewModelFactory by instance()
    private val SPLASH_TIME_OUT: Long = 3000 // 1 sec
    private lateinit var viewModel: AuthViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        viewModel = ViewModelProvider(this, factory).get(AuthViewModel::class.java)

        viewModel.getLoggedInUser().observe(this, Observer {
            val intent: Intent? = if(it!=null){
                Intent(this, HomeActivity::class.java)
            } else{
                Intent(this, LoginActivity::class.java)
            }
            Handler().postDelayed({
                startActivity(intent)
                finish()
            }, SPLASH_TIME_OUT)
        })

    }
}