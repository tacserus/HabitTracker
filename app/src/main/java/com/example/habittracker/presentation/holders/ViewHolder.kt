package com.example.habittracker.presentation.holders

import androidx.recyclerview.widget.RecyclerView
import com.example.habittracker.databinding.ItemLayoutBinding
import com.example.habittracker.domain.models.Habit

class ViewHolder(
    private val binding: ItemLayoutBinding,
    private val onCompleteClicked: (String) -> Unit
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(habit: Habit) {
        binding.item = habit

        binding.completeButton.setOnClickListener {
            onCompleteClicked(habit.id)
        }

        binding.executePendingBindings()
    }
}