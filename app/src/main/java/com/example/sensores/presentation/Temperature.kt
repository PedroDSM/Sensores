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

class Temperature : ComponentActivity(), SensorEventListener {
    private lateinit var sensorManager: SensorManager
    private var temperatureSensor: Sensor? = null
    private lateinit var temperatureDataEditText: EditText
    private lateinit var btnEnviar: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_temperature)

        temperatureDataEditText = findViewById(R.id.temperatura)
        btnEnviar = findViewById(R.id.enviar)
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        temperatureSensor = sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE)

        temperatureSensor?.let {
            sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_NORMAL)
        } ?: run {
            // Maneja la situación en la que el sensor de humedad no está disponible
        }

        btnEnviar.setOnClickListener {
            enviarDatos()
        }
    }

    private fun enviarDatos() {
        val temperatura = findViewById<EditText>(R.id.temperatura)
        val tempValue = temperatura.text.toString()
        println("tempValue: $tempValue")

        val url = "http://192.168.80.134:8000/api/temperatura"

        val jsonObject = JSONObject()
        jsonObject.put("temperatura", tempValue)

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
        if (event.sensor.type == Sensor.TYPE_AMBIENT_TEMPERATURE) {
            val tempvalue = event.values[0]
            val formattedTemp = "$tempvalue"
            temperatureDataEditText.text = Editable.Factory.getInstance().newEditable(formattedTemp)
        }
    }

    override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {
        // Maneja cambios en la precisión del sensor si es necesario
    }

    override fun onResume() {
        super.onResume()
        temperatureSensor?.let {
            sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_NORMAL)
        }
    }
}