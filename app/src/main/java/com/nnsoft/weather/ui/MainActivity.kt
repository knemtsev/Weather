package com.nnsoft.weather.ui

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationListener
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.Task
import com.nnsoft.weather.R
import com.nnsoft.weather.data.repository.WeatherRepository
import com.nnsoft.weather.databinding.MainActivityBinding
import com.nnsoft.weather.ui.di.DaggerAppComponent
import com.nnsoft.weather.util.locRound
import dagger.android.support.DaggerAppCompatActivity
import javax.inject.Inject

class MainActivity : AppCompatActivity() {
    private lateinit var viewModel: MainViewModel

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var locationTask: Task<Location>?=null
    lateinit var bind: MainActivityBinding

    var firstTime=true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bind = DataBindingUtil.setContentView(this, R.layout.main_activity)

        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        val availability= GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(this)
        if(availability== ConnectionResult.SUCCESS){
            bind.gplayMessage.visibility= View.GONE
            fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

            bind.refresh.setOnRefreshListener {
                refresh()
                bind.refresh.isRefreshing=false
            }

            bind.butRefresh.setOnClickListener {
                bind.refresh.isRefreshing=true
                refresh()
                bind.refresh.isRefreshing=false
            }

            locationTask=getLocationTask()
            if(locationTask!=null){
                subscribeOnLocation()
            }

        } else {
            bind.gplayMessage.text=
                when(availability){
                    ConnectionResult.SERVICE_MISSING -> getString(R.string.gplay_is_missing)
                    ConnectionResult.SERVICE_DISABLED -> getString(R.string.gplay_disabled)
                    else -> getString(R.string.gplay_problem)
                }
            bind.gplayMessage.visibility= View.VISIBLE
        }

    }

    private fun newLocation(location: Location){
        bind.textLocation.text = "lat=${location.latitude} lon=${location.longitude}"
        Log.i("NEW LOCATION",bind.textLocation.text.toString())
        if(firstTime)
            viewModel.initData(location)
        else
            viewModel.refresh(location)

        bind.vm=viewModel
        bind.invalidateAll()
        firstTime=false
    }

    private fun refresh() {
        getLocationTask()?.addOnSuccessListener { location ->
            location?.let { newLocation(location)}
        }
    }

    private fun subscribeOnLocation() {
        locationTask?.addOnSuccessListener { location : Location? ->
            location?.let { newLocation(location)}
        }
    }


    private val LOCATION_REQUEST=101

    private fun getLocationTask(): Task<Location>? {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(this, LOCATION_PERMS, LOCATION_REQUEST)
            return null
        }

        return fusedLocationClient.lastLocation
    }

    val LOCATION_PERMS = arrayOf(
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.ACCESS_FINE_LOCATION
    )


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode){
            LOCATION_REQUEST -> {
                if( grantResults[0] == PackageManager.PERMISSION_GRANTED ) {
                    Log.i("", "Agree ext write permission")
                    subscribeOnLocation()
                } else
                    Log.i("","Not agree ext write permission")
            }
        }
    }

}