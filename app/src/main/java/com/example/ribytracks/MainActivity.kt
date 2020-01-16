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
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.DialogTitle
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.PermissionChecker
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.example.ribytracks.database.TracksDatabase
import com.example.ribytracks.database.TracksEntity
import com.example.ribytracks.utils.VolleySingleton
import com.example.ribytracks.viewmodel.TracksViewModel
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.tasks.Task
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.lang.Exception
import java.nio.charset.StandardCharsets
import java.text.SimpleDateFormat
import java.util.*
import javax.security.auth.login.LoginException


class MainActivity : AppCompatActivity() {

    lateinit var client: FusedLocationProviderClient
    lateinit var locationRequest:LocationRequest
    lateinit var locationCallback: LocationCallback

    lateinit var startLat:String
    lateinit var startLong:String

    lateinit var stopLat:String
    lateinit var stopLong:String

    lateinit var tracksviewModel: TracksViewModel

    lateinit var tracksEntity: TracksEntity
    lateinit var mdistance:String

//    lateinit var startCoordinatesCollector:List<String>
//
//    lateinit var stopCoordinatesCollector:List<String>




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)




        //View model to observe live data
        tracksviewModel= ViewModelProviders.of(this).get(TracksViewModel::class.java)


        tracksviewModel.getAllTracks()?.observe(this, object: Observer<List<TracksEntity?>?>{
            override fun onChanged(t: List<TracksEntity?>?) {
                Toast.makeText(applicationContext, "$t", Toast.LENGTH_SHORT).show()


            }

        })

        client = LocationServices.getFusedLocationProviderClient(this)

        locationCallback = object : LocationCallback() {
            override fun onLocationResult(p0: LocationResult?) {
                for(location in p0?.locations!!){
                    stopLat = location.latitude.toString()
                    stopLong = location.longitude.toString()
//                    Toast.makeText(applicationContext, "LatUpdate: ${location.latitude}", Toast.LENGTH_LONG).show()
//                    Toast.makeText(applicationContext, "LongUpdate: ${location.longitude}", Toast.LENGTH_LONG).show()
gi
                    updateLatText.setText("Now at Lat: ${location.latitude}")
                    updateLongText.setText("and Long: ${location.longitude}")


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

            if (ContextCompat.checkSelfPermission(this,
                    ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {


                requestPerm()
                getLocation()

            } else {
                // Permission has already been granted
                getLocation()
                doLocationUpdates()
            }
        }

        stopBtn.setOnClickListener{
            stopGettingUpdates()

            stopLatText.visibility = View.VISIBLE
            stopLongText.visibility = View.VISIBLE

            stopLatText.setText("StopLat: $stopLat")
            stopLongText.setText("StopLong: $stopLong")

//            stopCoordinatesCollector = listOf(stopLat, stopLong)

            Toast.makeText(applicationContext, "StopLat: $stopLat", Toast.LENGTH_LONG).show()
            Toast.makeText(applicationContext, "StopLong: $stopLong", Toast.LENGTH_LONG).show()
            Toast.makeText(applicationContext, "Update stopped", Toast.LENGTH_LONG).show()
        }

        getDist.setOnClickListener {
            try{
                getDistanceRequest(startLat, startLong, stopLat, stopLong)

//                Toast.makeText(applicationContext, "dist", Toast.LENGTH_LONG).show()

            }
            catch (e:Exception){
                Toast.makeText(applicationContext, "${e.message}", Toast.LENGTH_LONG).show()
            }



//            Toast.makeText(applicationContext, "mditance $mdistance", Toast.LENGTH_LONG).show()

        }







    }

    override fun onStart() {
        super.onStart()
        gpsSetting()
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
                            doLocationUpdates()
                        }
                    }
                }
            }




    }


    private fun getLocation(){



            client.lastLocation.addOnSuccessListener {
                if (it != null){
                    Log.i("Location", it.latitude.toString())


                    startLat = it.latitude.toString()
                    startLong = it.longitude.toString()

//                    startCoordinatesCollector = listOf<String>(startLat, startLong)


                    startLatText.visibility = View.VISIBLE
                    startLongText.visibility = View.VISIBLE

                    updateLongText.visibility = View.VISIBLE
                    updateLatText.visibility = View.VISIBLE

                    stopLatText.visibility = View.GONE
                    stopLongText.visibility = View.GONE
                    distanceText.visibility = View.GONE
                    startLatText.setText("StartLat: $startLat")
                    startLongText.setText("StartLong: $startLong")


                    Toast.makeText(this, "StartLat: ${it.latitude}", Toast.LENGTH_LONG).show()
                    Toast.makeText(this, "StartLong: ${it.longitude}", Toast.LENGTH_LONG).show()

                }
                Log.i("Location", "null")
            }




    }

    private fun stopGettingUpdates(){
        client.removeLocationUpdates(locationCallback)
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
//            Toast.makeText(this, "task: ${locationSettingsResponse.locationSettingsStates}", Toast.LENGTH_LONG).show()
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

    fun getDistanceRequest(lat1:String, lon1:String, lat2:String, lon2:String){

                val url =
                    "https://maps.googleapis.com/maps/api/directions/json?origin=$lat1,$lon1&destination=$lat2," +
                            "$lon2&sensor=false&units=metric&mode=driving&key=${resources.getString(R.string.api_key)}"


                val request = JsonObjectRequest(Request.Method.GET, url, null,
                    Response.Listener { response ->
                        try{
                            val routes = response.getJSONArray("routes")
                            val subRoute = routes.getJSONObject(0)
                            val legs = subRoute.getJSONArray("legs")
                            val steps = legs.getJSONObject(0).getJSONArray("steps")
                            val distance = steps.getJSONObject(0).getJSONObject("distance")
                            mdistance = distance.getString("text")

                            distanceText.visibility = View.VISIBLE
                            distanceText.setText(mdistance)

                            Toast.makeText(this, mdistance, Toast.LENGTH_SHORT).show()
                            Log.i("distance", mdistance)

                            //Getting current day
                            val sdf = SimpleDateFormat("dd/MM/yyyy hh:mm:ss" )
                            val currentDate = sdf.format(Date())

                            tracksEntity= TracksEntity(lat1, lon1, lat2, lon2, currentDate)
                            tracksEntity.distance = mdistance

                            CoroutineScope(IO).launch{
                                TracksDatabase.getInstance(baseContext)?.tracksDao()?.insert(tracksEntity)
                            }



                        }catch (e: Exception){

                            Toast.makeText(this, "requestError: ${e.message}", Toast.LENGTH_SHORT).show()
                        }

                    }, Response.ErrorListener { error ->

                        if(error.networkResponse != null){

                            val errorByte = error.networkResponse.data
                            val parseError =  errorByte.toString(StandardCharsets.UTF_8)

                            val errorObj = JSONObject(parseError)

                            val errorMessage = errorObj.getString("message")



                            Toast.makeText(applicationContext, errorMessage, Toast.LENGTH_SHORT).show()

                        }
                        else{
                            Toast.makeText(applicationContext, "No network coverage", Toast.LENGTH_SHORT).show()
                        }

                    })


                VolleySingleton.getInstance(applicationContext).addToRequestQueue(request)

            }


}
