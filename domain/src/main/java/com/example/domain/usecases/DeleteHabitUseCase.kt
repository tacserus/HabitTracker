package com.example.domain.usecases

import com.example.domain.HabitRepository
import com.example.domain.models.HabitModel

class DeleteHabitUseCase(private val repository: HabitRepository) {
    suspend fun execute(habitEntity: HabitModel) {
        repository.deleteHabit(habitEntity)
    }
}