package com.example.domain.usecases

import com.example.domain.HabitRepository
import com.example.domain.models.HabitModel

class GetHabitByIdUseCase(private val repository: HabitRepository) {
    suspend fun execute(id: String): HabitModel? {
        return repository.getHabitById(id)
    }
}