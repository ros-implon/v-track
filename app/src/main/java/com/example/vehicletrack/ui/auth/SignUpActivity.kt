package com.example.vehicletrack.ui.auth

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.vehicletrack.R
import com.example.vehicletrack.databinding.ActivitySignUpBinding
import com.example.vehicletrack.util.*
import kotlinx.coroutines.launch
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance

class SignUpActivity : AppCompatActivity(), KodeinAware {

    override val kodein by kodein()
    private val factory: AuthViewModelFactory by instance()

    private lateinit var binding: ActivitySignUpBinding
    private lateinit var viewModel: AuthViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_sign_up)
        viewModel = ViewModelProvider(this, factory).get(AuthViewModel::class.java)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = ""

        binding.buttonSignup.setOnClickListener {
            signUpUser()
        }

        binding.loginText.setOnClickListener {
            onBackPressed()
        }

    }

    private fun signUpUser(){

        binding.progressBarLayout.show()

        val firstname = binding.fnameEdit.text.toString().trim()
        val lastname = binding.lnameEdit.text.toString().trim()
        val email = binding.emailEdit.text.toString().trim()
        val password = binding.passwordEdit.text.toString().trim()

        Log.e("SignUpActivity", firstname + lastname)

        lifecycleScope.launch {
            try {
                val authResponse = viewModel.userSignUp(firstname, lastname, email, password)
                binding.progressBarLayout.hide()
                binding.rootLayout.snackbar(authResponse.message!!)
            } catch (e: ApiException) {
                e.printStackTrace()
            } catch (e: NoInternetException) {
                e.printStackTrace()
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}