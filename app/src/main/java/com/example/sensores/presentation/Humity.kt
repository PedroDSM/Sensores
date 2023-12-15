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

class Humity : ComponentActivity(), SensorEventListener {
    private lateinit var sensorManager: SensorManager
    private var humiditySensor: Sensor? = null
    private lateinit var humidityDataTextView: EditText
    private lateinit var btnEnviar: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_humity)

        humidityDataTextView = findViewById(R.id.humedad)
        btnEnviar = findViewById(R.id.enviar)
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        humiditySensor = sensorManager.getDefaultSensor(Sensor.TYPE_RELATIVE_HUMIDITY)

        humiditySensor?.let {
            sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_NORMAL)
        } ?: run {
            // Handle the situation where the humidity sensor is not available
        }

        btnEnviar.setOnClickListener {
            enviarDatos()
        }
    }

    private fun enviarDatos() {
        val humedadEditText = findViewById<EditText>(R.id.humedad)
        val humedadValue = humedadEditText.text.toString()
        println("humedadValue: $humedadValue")

        val url = "http://192.168.80.134:8000/api/humedad"

        val jsonObject = JSONObject()
        jsonObject.put("humedad", humedadValue)

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
        if (event.sensor.type == Sensor.TYPE_RELATIVE_HUMIDITY) {
            val humidityValue = event.values[0]
            val formattedHumidity = "$humidityValue"
            humidityDataTextView.text = Editable.Factory.getInstance().newEditable(formattedHumidity)
        }
    }


    override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {
        // Handle changes in sensor accuracy if necessary
    }

    override fun onResume() {
        super.onResume()
        humiditySensor?.let {
            sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_NORMAL)
        }
    }
}
