package com.example.habittracker.presentation.holders

import androidx.recyclerview.widget.RecyclerView
import com.example.habittracker.databinding.ItemLayoutBinding
import com.example.habittracker.domain.models.HabitEntity

class ViewHolder(
    private val binding: ItemLayoutBinding,
    private val onCompleteClicked: (String) -> Unit,
    private val onDeleteClicked: (String) -> Unit
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(habit: HabitEntity) {
        binding.item = habit

        binding.completeButton.setOnClickListener {
            onCompleteClicked(habit.id)
        }

        binding.deleteButton.setOnClickListener {
            onDeleteClicked(habit.id)
        }

        binding.executePendingBindings()
    }
}