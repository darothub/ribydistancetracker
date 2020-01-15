package com.example.ribytracks

import android.Manifest
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.location.Location

import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.DialogTitle
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.PermissionChecker
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.tasks.Task
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import javax.security.auth.login.LoginException


class MainActivity : AppCompatActivity() {

    lateinit var client: FusedLocationProviderClient
    lateinit var locationRequest:LocationRequest
    lateinit var locationCallback: LocationCallback



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



        client = LocationServices.getFusedLocationProviderClient(this)

        locationCallback = object : LocationCallback() {
            override fun onLocationResult(p0: LocationResult?) {
                for(location in p0?.locations!!){
                    Toast.makeText(applicationContext, "LatUpd: ${location.latitude}", Toast.LENGTH_LONG).show()
                    Toast.makeText(applicationContext, "LongUpd: ${location.longitude}", Toast.LENGTH_LONG).show()
                }
            }

        }

        locationRequest = LocationRequest.create().apply {
            interval = 10000
            fastestInterval = 5000
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }
//
        btn.setOnClickListener {

            gpsSetting()

            doLocationUpdates()


            if (ContextCompat.checkSelfPermission(this,
                    ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

                requestPerm()

            } else {
                // Permission has already been granted
                getLocation()
            }
        }




    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)


            when(requestCode){
                200 -> {
                    if(grantResults.size > 0 && permissions[0].equals(ACCESS_FINE_LOCATION)){
                        if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                            Toast.makeText(this, "permit", Toast.LENGTH_LONG).show()
                            Log.i("Location", "permit")
                            getLocation()
                        }
                    }
                }
            }




    }


    private fun getLocation(){

            client.lastLocation.addOnSuccessListener {
                if (it != null){
                    Log.i("Location", it.latitude.toString())
//                    Toast.makeText(this, "Lat: ${it.latitude}", Toast.LENGTH_LONG).show()
//                    Toast.makeText(this, "Long: ${it.longitude}", Toast.LENGTH_LONG).show()

                }
                Log.i("Location", "null")
            }



    }

    fun requestPerm(){
        ActivityCompat.requestPermissions(this, arrayOf(ACCESS_FINE_LOCATION), 200)
    }

    private fun doLocationUpdates() {
        client.requestLocationUpdates(
            locationRequest,
            locationCallback,
            null
        )
    }
    private fun gpsSetting(){
        val builder = LocationSettingsRequest.Builder()
            .addLocationRequest(locationRequest)

        val client: SettingsClient = LocationServices.getSettingsClient(this)
        val task: Task<LocationSettingsResponse> = client.checkLocationSettings(builder.build())



        task.addOnSuccessListener { locationSettingsResponse ->
            // All location settings are satisfied. The client can initialize
            // location requests here.
            // ...
            doLocationUpdates()
            Toast.makeText(this, "task: ${locationSettingsResponse.locationSettingsStates}", Toast.LENGTH_LONG).show()
            Log.i("task", "task: ${locationSettingsResponse.locationSettingsStates}")

        }

        task.addOnFailureListener { exception ->
            if (exception is ResolvableApiException){
                // Location settings are not satisfied, but this can be fixed
                // by showing the user a dialog.
                try {
                    // Show the dialog by calling startResolutionForResult(),
                    // and check the result in onActivityResult().
                    exception.startResolutionForResult(this@MainActivity,
                        201)
                } catch (sendEx: IntentSender.SendIntentException) {
                    // Ignore the error.
                }
            }
        }


        Log.i("dataService", "$task")
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 201) {
            doLocationUpdates()
        }
    }
}
