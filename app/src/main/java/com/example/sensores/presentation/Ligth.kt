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

class Ligth : ComponentActivity(), SensorEventListener {
    private lateinit var sensorManager: SensorManager
    private var ligthSensor: Sensor? = null
    private lateinit var ligthDataTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_light)

        ligthDataTextView = findViewById(R.id.luz)
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        ligthSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT)

        ligthSensor?.let {
            sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_NORMAL)
        } ?: run {
            // Maneja la situación en la que el sensor de humedad no está disponible
        }
    }

    override fun onSensorChanged(event: SensorEvent) {
        if (event.sensor.type == Sensor.TYPE_LIGHT) {
            val ligthvalue = event.values[0]
            ligthDataTextView.text = "$ligthvalue lux"
        }
    }

    override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {
        // Maneja cambios en la precisión del sensor si es necesario
    }

    override fun onResume() {
        super.onResume()
        ligthSensor?.let {
            sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_NORMAL)
        }
    }
}
