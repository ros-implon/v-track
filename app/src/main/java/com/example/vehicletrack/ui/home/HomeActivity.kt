package com.example.vehicletrack.ui.home


import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import com.example.vehicletrack.R
import com.example.vehicletrack.data.db.preferences.PreferenceProvider
import com.example.vehicletrack.ui.auth.AuthViewModel
import com.example.vehicletrack.ui.auth.AuthViewModelFactory
import com.example.vehicletrack.ui.auth.LoginActivity
import com.example.vehicletrack.util.*
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.coroutines.launch
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance


class HomeActivity : AppCompatActivity(), KodeinAware{

    override val kodein by kodein()
    private val preferenceProvider: PreferenceProvider by instance()
    private val factory: AuthViewModelFactory by instance()

    private lateinit var viewModel: AuthViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        setSupportActionBar(toolbar)
        supportActionBar!!.title = ""

        viewModel = ViewModelProvider(this, factory).get(AuthViewModel::class.java)

        val navController = Navigation.findNavController(this, R.id.fragment)
        NavigationUI.setupWithNavController(nav_view, navController)
        NavigationUI.setupActionBarWithNavController(this,navController, drawer_layout)
        nav_view.menu.findItem(R.id.logout).setOnMenuItemClickListener {
            logout()
            return@setOnMenuItemClickListener true
        }
    }

    private fun logout(){
        progress_bar_home.show()

        lifecycleScope.launch {
            try {
                viewModel.deleteLoggedInUser()
                preferenceProvider.deleteUser()
                startActivity(Intent(this@HomeActivity, LoginActivity::class.java))
                finish()
            }catch (e: Exception){
                progress_bar_home.hide()
                e.printStackTrace()
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return NavigationUI.navigateUp(
            Navigation.findNavController(this, R.id.fragment),
            drawer_layout
        )
    }

}