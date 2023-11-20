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

class Pressure : ComponentActivity(), SensorEventListener {
    private lateinit var sensorManager: SensorManager
    private var pressureSensor: Sensor? = null
    private lateinit var pressureDataTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pressure)

        pressureDataTextView = findViewById(R.id.presion)
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        pressureSensor = sensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE)

        pressureSensor?.let {
            sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_NORMAL)
        } ?: run {
            // Maneja la situación en la que el sensor de humedad no está disponible
        }
    }

    override fun onSensorChanged(event: SensorEvent) {
        if (event.sensor.type == Sensor.TYPE_PRESSURE) {
            val presvalue = event.values[0]
            pressureDataTextView.text = "$presvalue hPa"
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