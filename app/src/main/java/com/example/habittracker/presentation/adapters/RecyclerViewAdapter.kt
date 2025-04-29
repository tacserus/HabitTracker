package com.example.habittracker.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.habittracker.databinding.ItemLayoutBinding
import com.example.habittracker.domain.models.Habit
import com.example.habittracker.presentation.holders.ViewHolder


class RecyclerViewAdapter(
    private val onItemClicked: (String) -> Unit
) : RecyclerView.Adapter<ViewHolder>() {
    private var habits = listOf<Habit>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemLayoutBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = habits[position]
        holder.bind(item)

        holder.itemView.setOnClickListener { onItemClicked(item.id) }
    }

    override fun getItemCount(): Int {
        return habits.size
    }

    fun submit(newHabits: List<Habit>) {
        val diffCallback = HabitDiffCallback(habits, newHabits)
        val diffResult = DiffUtil.calculateDiff(diffCallback)

        habits = newHabits
        diffResult.dispatchUpdatesTo(this)
    }
}