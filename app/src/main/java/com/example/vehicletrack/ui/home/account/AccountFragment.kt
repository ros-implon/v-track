package com.example.vehicletrack.ui.home.account

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.vehicletrack.R
import com.example.vehicletrack.data.db.entities.Track
import com.example.vehicletrack.data.db.preferences.PreferenceProvider
import com.example.vehicletrack.databinding.AccountFragmentBinding
import com.example.vehicletrack.ui.home.track.TrackItemAdapter
import com.example.vehicletrack.util.*
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import kotlinx.coroutines.launch
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance

class AccountFragment : Fragment(), KodeinAware {

    override val kodein by kodein()

    private lateinit var accountViewModel: AccountViewModel
    private val factory: AccountViewModelFactory by instance()
    private val preferenceProvider: PreferenceProvider by instance()

    private lateinit var binding: AccountFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.account_fragment, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        accountViewModel = ViewModelProviders.of(this, factory).get(AccountViewModel::class.java)
        binding.viewmodel = accountViewModel
        binding.lifecycleOwner = this
        bindUI()
    }

    private fun bindUI() = Coroutines.main {
        binding.progressbarRoutes.show()
        lifecycleScope.launch {
            try {
                val trackHistoryResponse = accountViewModel.getTracks(preferenceProvider.getUser()!!._id.toString())
                if (trackHistoryResponse.tracks!!.isNotEmpty()) {
                    binding.noroutesText.hide()
                    binding.progressbarRoutes.hide()
                    initRecyclerView(trackHistoryResponse.tracks)
                } else {
                    binding.progressbarRoutes.hide()
                    binding.noroutesText.show()
                }
            } catch (e: ApiException) {
                e.printStackTrace()
            } catch (e: NoInternetException) {
                e.printStackTrace()
            }
        }
    }

    private fun initRecyclerView(tracks: List<Track>) {

        val mAdapter = TrackItemAdapter(tracks)


        binding.recyclerViewTh.apply {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            adapter = mAdapter
        }

    }


}
