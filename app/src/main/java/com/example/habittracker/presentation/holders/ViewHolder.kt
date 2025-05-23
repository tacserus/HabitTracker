package com.example.habittracker.presentation.holders

import androidx.recyclerview.widget.RecyclerView
import com.example.domain.models.HabitModel
import com.example.habittracker.databinding.ItemLayoutBinding

class ViewHolder(
    private val binding: ItemLayoutBinding,
    private val onCompleteClicked: (String) -> Unit,
    private val onDeleteClicked: (String) -> Unit
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(habit: HabitModel) {
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