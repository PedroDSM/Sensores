package com.example.sensores.presentation

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.activity.ComponentActivity
import androidx.core.app.ActivityCompat
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.sensores.R
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import org.json.JSONObject

class GPS : ComponentActivity() {

    private lateinit var latitud: EditText
    private lateinit var longitud: EditText
    private lateinit var btnEnviar: Button
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private val locationRequest: LocationRequest by lazy {
        LocationRequest.create().apply {
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            interval = 10000
            fastestInterval = 5000
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gps)

        longitud = findViewById(R.id.lon)
        latitud = findViewById(R.id.lat)
        btnEnviar = findViewById(R.id.enviar)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ),
                1
            )
        } else {
            startLocationUpdates()
        }

        btnEnviar.setOnClickListener {
            enviarDatos()
        }
    }

    private fun enviarDatos() {
        val longitudEditText = findViewById<EditText>(R.id.lon)
        val lonValue = longitudEditText.text.toString()
        println("lonValue: $lonValue")

        val latitudEditText = findViewById<EditText>(R.id.lat)
        val latValue = latitudEditText.text.toString()
        println("latValue: $latValue")

        val url = "http://192.168.80.134:8000/api/gps"

        val jsonObject = JSONObject()
        jsonObject.put("longitud", lonValue)
        jsonObject.put("latitud", latValue)

        val request = JsonObjectRequest(
            Request.Method.POST, url, jsonObject,
            Response.Listener { response ->
                // Handle the server response
                println("Server response: $response")
            },
            Response.ErrorListener { error ->
                // Handle the request error
                println("Error sending request: ${error.message}")
            })

        // Add the request to the Volley request queue
        Volley.newRequestQueue(this).add(request)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startLocationUpdates()
            } else {
                Log.e("GPS", "Location Permission denied")
            }
        }
    }

    private fun startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        fusedLocationClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            null
        )
    }

    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            super.onLocationResult(locationResult)
            val location = locationResult.lastLocation
            if (location != null) {
                val lat = location.latitude
                val lng = location.longitude
                val formattedlgn = "$lng"
                longitud.text = Editable.Factory.getInstance().newEditable(formattedlgn)
                val formattedlat = "$lat"
                latitud.text = Editable.Factory.getInstance().newEditable(formattedlat)
            }
        }
    }
}