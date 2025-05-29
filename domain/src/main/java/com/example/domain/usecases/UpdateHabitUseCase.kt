package com.example.domain.usecases

import com.example.domain.HabitRepository
import com.example.domain.models.Habit

class UpdateHabitUseCase(private val repository: HabitRepository) {
    suspend fun execute(habit: Habit) {
        return repository.updateHabit(habit)
    }
}