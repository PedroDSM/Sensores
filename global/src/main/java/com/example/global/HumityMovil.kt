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

class HumityMovil : ComponentActivity() {

    private lateinit var humedadTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.global_activity_humity)

        humedadTextView = findViewById(R.id.humedad)

        // Inicia la tarea periódica de actualización cada 5 segundos
        val timer = Timer()
        timer.scheduleAtFixedRate(object : TimerTask() {
            override fun run() {
                obtenerDatos()
            }
        }, 0, 5000)
    }

    private fun obtenerDatos() {
        val url = "http://192.168.80.134:8000/api/humedad"

        val request = JsonObjectRequest(
            Request.Method.GET, url, null,
            Response.Listener { response ->
                // Manejar la respuesta del servidor
                val humedad = response.optDouble("humedad", 0.0)
                actualizarUI(humedad)
            },
            Response.ErrorListener { error ->
                // Manejar el error de la solicitud
                println("Error al obtener datos: ${error.message}")
            })

        // Agregar la solicitud a la cola de Volley
        Volley.newRequestQueue(this).add(request)
    }

    private fun actualizarUI(humedad: Double) {
        runOnUiThread {
            // Actualiza la UI con el valor de humedad
            humedadTextView.text = String.format("%.2f %%", humedad)
        }
    }
}
