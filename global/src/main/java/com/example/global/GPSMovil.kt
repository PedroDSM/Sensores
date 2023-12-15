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

class GPSMovil : ComponentActivity() {

    private lateinit var lonTextView: TextView
    private lateinit var latTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.global_activity_gps)

        lonTextView = findViewById(R.id.lon)
        latTextView = findViewById(R.id.lat)

        // Inicia la tarea periódica de actualización cada 5 segundos
        val timer = Timer()
        timer.scheduleAtFixedRate(object : TimerTask() {
            override fun run() {
                obtenerDatos()
            }
        }, 0, 5000)
    }

    private fun obtenerDatos() {
        val url = "http://192.168.80.134:8000/api/gps"

        val request = JsonObjectRequest(
            Request.Method.GET, url, null,
            Response.Listener { response ->
                // Manejar la respuesta del servidor
                val lon = response.optDouble("longitud", 0.0)
                val lat = response.optDouble("latitud", 0.0)
                actualizarUI(lon, lat)
            },
            Response.ErrorListener { error ->
                // Manejar el error de la solicitud
                println("Error al obtener datos: ${error.message}")
            })

        // Agregar la solicitud a la cola de Volley
        Volley.newRequestQueue(this).add(request)
    }

    private fun actualizarUI(lon: Double, lat: Double) {
        runOnUiThread {
            // Actualiza la UI con el valor de gps
            lonTextView.text = String.format("%.2f %%", lon)
            latTextView.text = String.format("%.2f %%", lat)
        }
    }
}