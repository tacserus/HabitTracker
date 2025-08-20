package com.example.domain.usecases

import com.example.domain.HabitRepository
import com.example.domain.models.Habit

class DeleteHabitUseCase(private val repository: HabitRepository) {
    suspend fun execute(habitEntity: Habit) {
        repository.deleteHabit(habitEntity)
    }
}