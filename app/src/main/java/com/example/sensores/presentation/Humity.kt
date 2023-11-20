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

class Humity : ComponentActivity(), SensorEventListener {
    private lateinit var sensorManager: SensorManager
    private var humiditySensor: Sensor? = null
    private lateinit var humidityDataTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_humity)

        humidityDataTextView = findViewById(R.id.humedad)
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        humiditySensor = sensorManager.getDefaultSensor(Sensor.TYPE_RELATIVE_HUMIDITY) // Asegúrate de utilizar el tipo de sensor de humedad adecuado

        humiditySensor?.let {
            sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_NORMAL)
        } ?: run {
            // Maneja la situación en la que el sensor de humedad no está disponible
        }
    }

    override fun onSensorChanged(event: SensorEvent) {
        if (event.sensor.type == Sensor.TYPE_RELATIVE_HUMIDITY) {
            val humidityValue = event.values[0]
            humidityDataTextView.text = "$humidityValue %"
        }
    }

    override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {
        // Maneja cambios en la precisión del sensor si es necesario
    }

    override fun onResume() {
        super.onResume()
        humiditySensor?.let {
            sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_NORMAL)
        }
    }
}