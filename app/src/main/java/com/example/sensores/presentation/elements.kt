package com.example.sensores.presentation

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.sensores.R

class elements(private val context: Context): RecyclerView.Adapter<elements.ViewHolder>() {

    val titles = arrayOf("GPS",
        "Humedad",
        "Temperatura",
        "Luz",
        "Presion")

    val icons = arrayOf(
        R.drawable.gps,
        R.drawable.humidity,
        R.drawable.temperature,
        R.drawable.ligth,
        R.drawable.pressure)

    val activity = arrayOf(
        GPS::class.java,
        Humity::class.java,
        Temperature::class.java,
        Ligth::class.java,
        Pressure::class.java
    )

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        var itemIcon: ImageView
        var itemTitle: TextView

        init{
            itemIcon = itemView.findViewById(R.id.IconImage)
            itemTitle = itemView.findViewById(R.id.Titles)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.cards, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return titles.size
        return icons.size
        return activity.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemTitle.text = titles[position]
        holder.itemIcon.setImageResource(icons[position])
        // Agregar un click listener para abrir la actividad correspondiente
        holder.itemView.setOnClickListener {
            val activityClass = activity[position]
            val intent = Intent(context, activityClass)
            context.startActivity(intent)
        }
    }
}


