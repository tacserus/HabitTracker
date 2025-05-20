package com.example.domain.usecases

import com.example.domain.HabitRepository
import com.example.domain.models.HabitModel

class UpdateHabitUseCase(private val repository: HabitRepository) {
    suspend fun execute(habitModel: HabitModel) {
        return repository.updateHabit(habitModel)
    }
}