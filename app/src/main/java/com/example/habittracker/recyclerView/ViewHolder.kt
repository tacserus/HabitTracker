package com.example.habittracker.recyclerView

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.habittracker.R
import com.example.habittracker.models.Item

class ViewHolder(
    itemView: View
) : RecyclerView.ViewHolder(itemView) {

    private val title: TextView = itemView.findViewById(R.id.title)
    private val description: TextView = itemView.findViewById(R.id.description)
    private val habitPriority: TextView = itemView.findViewById(R.id.habitPriority)
    private val habitType: TextView = itemView.findViewById(R.id.habitType)
    private val quantity: TextView = itemView.findViewById(R.id.quantity)
    private val frequency: TextView = itemView.findViewById(R.id.frequency)

    fun bind(item: Item) {
        title.text = item.title
        description.text = item.description
        habitPriority.text = item.priority
        habitType.text = item.type
        quantity.text = "${item.quantity}"
        frequency.text = "${item.frequency}"
    }
}