package com.example.sensores.presentation

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.widget.TextView
import androidx.activity.ComponentActivity
import com.example.sensores.R

class Temperature : ComponentActivity(), SensorEventListener {
    private lateinit var sensorManager: SensorManager
    private var temperatureSensor: Sensor? = null
    private lateinit var temperatureDataTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_temperature)

        temperatureDataTextView = findViewById(R.id.temperatura)
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        temperatureSensor = sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE)

        temperatureSensor?.let {
            sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_NORMAL)
        } ?: run {
            // Maneja la situación en la que el sensor de humedad no está disponible
        }
    }

    override fun onSensorChanged(event: SensorEvent) {
        if (event.sensor.type == Sensor.TYPE_AMBIENT_TEMPERATURE) {
            val tempvalue = event.values[0]
            temperatureDataTextView.text = "$tempvalue ºC"
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