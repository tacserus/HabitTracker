package com.example.domain.usecases

import com.example.domain.HabitRepository
import com.example.domain.models.HabitModel

class AddHabitUseCase(private val repository: HabitRepository) {
    suspend fun execute(habitEntity: HabitModel) {
        repository.addHabit(habitEntity)
    }
}