package com.example.habittracker.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.data.HabitRepository
import javax.inject.Inject

@Suppress("UNCHECKED_CAST")
class ViewModelFactory @Inject constructor(private val habitRepository: HabitRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(HabitListViewModel::class.java) -> {
                HabitListViewModel(habitRepository) as T
            }
            modelClass.isAssignableFrom(AddHabitViewModel::class.java) -> {
                AddHabitViewModel(habitRepository) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}