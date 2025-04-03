package com.example.habittracker.holders

import androidx.recyclerview.widget.RecyclerView
import com.example.habittracker.databinding.ItemLayoutBinding
import com.example.habittracker.models.Habit

class ViewHolder(
    private val binding: ItemLayoutBinding
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(habit: Habit) {
        binding.item = habit

        binding.executePendingBindings()
    }
}