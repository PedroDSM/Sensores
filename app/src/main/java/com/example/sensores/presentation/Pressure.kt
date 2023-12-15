package com.example.sensores.presentation

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.text.Editable
import android.widget.Button
import android.widget.EditText
import androidx.activity.ComponentActivity
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.sensores.R
import org.json.JSONObject

class Pressure : ComponentActivity(), SensorEventListener {
    private lateinit var sensorManager: SensorManager
    private var pressureSensor: Sensor? = null
    private lateinit var pressureDataEditText: EditText
    private lateinit var btnEnviar: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pressure)

        pressureDataEditText = findViewById(R.id.presion)
        btnEnviar = findViewById(R.id.enviar)
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        pressureSensor = sensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE)

        pressureSensor?.let {
            sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_NORMAL)
        } ?: run {
            // Maneja la situación en la que el sensor de humedad no está disponible
        }

        btnEnviar.setOnClickListener {
            enviarDatos()
        }

    }

    private fun enviarDatos() {
        val presionEditText = findViewById<EditText>(R.id.presion)
        val presionValue = presionEditText.text.toString()
        println("presionValue: $presionValue")

        val url = "http://192.168.80.134:8000/api/presion"

        val jsonObject = JSONObject()
        jsonObject.put("presion", presionValue)

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

    override fun onSensorChanged(event: SensorEvent) {
        if (event.sensor.type == Sensor.TYPE_PRESSURE) {
            val presvalue = event.values[0]
            val formattedPressure = "$presvalue"
            pressureDataEditText.text = Editable.Factory.getInstance().newEditable(formattedPressure)
        }
    }

    override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {
        // Maneja cambios en la precisión del sensor si es necesario
    }

    override fun onResume() {
        super.onResume()
        pressureSensor?.let {
            sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_NORMAL)
        }
    }
}