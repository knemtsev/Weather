package com.nnsoft.weather.ui

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import androidx.databinding.Observable
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.Task
import com.nnsoft.weather.R
import com.nnsoft.weather.databinding.MainActivityBinding
import io.reactivex.disposables.CompositeDisposable
import java.util.*


class MainActivity : AppCompatActivity() {
    private lateinit var viewModel: MainViewModel

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var locationTask: Task<Location>?=null
    lateinit var bind: MainActivityBinding

    val compositeDisposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bind = DataBindingUtil.setContentView(this, R.layout.main_activity)

        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        bind.vm=viewModel

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)


        val availability= GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(this)
        if(availability== ConnectionResult.SUCCESS){
            bind.gplayMessage.visibility= View.GONE
            fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

            bind.refresh.setOnRefreshListener {
                refresh(true)
                bind.refresh.isRefreshing=false
            }

            bind.butRefresh.setOnClickListener {
                bind.refresh.isRefreshing=true
                refresh(true)
                bind.refresh.isRefreshing=false
            }

            locationTask=getLocationTask()

            if(locationTask!=null){
                subscribeOnLocation()
            }

            val onPropertyChangedCallback=object: Observable.OnPropertyChangedCallback() {
                override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
                    Log.i("PROPERTY CHANGED", "Prop id=" + propertyId)
                    bind.invalidateAll()

                    val weatherData=viewModel.data.get()
                    if(weatherData!=null) {
                        val imgURL="https://openweathermap.org/img/w/"+weatherData.iconId+".png"
                        val options: RequestOptions = RequestOptions()
                            .fitCenter()
                            .placeholder(R.drawable.ic_baseline_refresh_24)
                            .error(R.drawable.ic_baseline_refresh_24)
                        Glide.with(this@MainActivity)
                            .load(imgURL)
                            .apply(options)
                            .into(bind.weatherIcon)
                    }
                }
            }
            viewModel.data.addOnPropertyChangedCallback(onPropertyChangedCallback)
            viewModel.errorMessage.addOnPropertyChangedCallback(onPropertyChangedCallback)

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

    override fun onResume() {
        super.onResume()
        refresh(false)
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.dispose()
    }
    private fun newLocation(location: Location, force: Boolean = false){
        Log.i("NEW LOCATION", ""+location.latitude+" "+location.longitude)

        viewModel.refresh(location, force)

        bind.vm=viewModel
        bind.invalidateAll()
    }

    private fun refresh(force: Boolean = false) {
        Log.i("refresh", "start")
        getLocationTask()?.addOnSuccessListener { location ->
            location?.let { newLocation(location, force)}
        }
    }

    private fun subscribeOnLocation() {
        locationTask?.addOnSuccessListener { location: Location? ->
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


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode){
            LOCATION_REQUEST -> {
                if(grantResults.size>0) {
                    if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        Log.i("LOCATION_REQUEST", "Agree location permission")
                        subscribeOnLocation()
                    } else
                        Log.i("LOCATION_REQUEST", "Not agree location permission")
                } else {
                    Log.i("LOCATION_REQUEST", "grantResults size is 0")
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_main, menu)

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.action_settings -> {
                val intent = Intent(this, SettingsActivity::class.java)
                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
    }
}