package com.example.global

import android.os.Bundle
import android.widget.TextView
import androidx.activity.ComponentActivity
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import java.util.Timer
import java.util.TimerTask

class TemperatureMovil : ComponentActivity() {

    private lateinit var tempTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.global_activity_temperature)

        tempTextView = findViewById(R.id.temperatura)

        // Inicia la tarea periódica de actualización cada 5 segundos
        val timer = Timer()
        timer.scheduleAtFixedRate(object : TimerTask() {
            override fun run() {
                obtenerDatos()
            }
        }, 0, 5000)
    }

    private fun obtenerDatos() {
        val url = "http://192.168.80.134:8000/api/temperatura"

        val request = JsonObjectRequest(
            Request.Method.GET, url, null,
            Response.Listener { response ->
                // Manejar la respuesta del servidor
                val temperatura = response.optDouble("temperatura", 0.0)
                actualizarUI(temperatura)
            },
            Response.ErrorListener { error ->
                // Manejar el error de la solicitud
                println("Error al obtener datos: ${error.message}")
            })

        // Agregar la solicitud a la cola de Volley
        Volley.newRequestQueue(this).add(request)
    }

    private fun actualizarUI(temperatura: Double) {
        runOnUiThread {
            // Actualiza la UI con el valor de humedad
            tempTextView.text = String.format("%.2f %%", temperatura)
        }
    }
}