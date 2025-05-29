package com.example.domain.usecases

import com.example.domain.HabitRepository
import com.example.domain.models.Habit

class GetHabitByIdUseCase(private val repository: HabitRepository) {
    suspend fun execute(id: String): Habit? {
        return repository.getHabitById(id)
    }
}