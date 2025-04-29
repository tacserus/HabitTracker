package com.example.habittracker.presentation.viewmodels

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.habittracker.data.database.HabitsRepository

class ViewModelFactory(
    private val habitsRepository: HabitsRepository,
    private val application: Application? = null,
    private val id: String? = null
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(HabitListViewModel::class.java) -> {
                HabitListViewModel(
                    habitsRepository,
                    requireNotNull(application)
                ) as T
            }
            modelClass.isAssignableFrom(AddHabitViewModel::class.java) -> {
                AddHabitViewModel(
                    habitsRepository,
                    requireNotNull(application),
                    requireNotNull(id)
                ) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }
}