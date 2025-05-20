package com.example.domain.usecases

import com.example.domain.HabitRepository
import com.example.domain.models.HabitModel
import kotlinx.coroutines.flow.Flow

class GetAllHabitsUseCase(private val repository: HabitRepository) {
    suspend fun execute(): Flow<List<HabitModel>> {
        return repository.getAllHabits()
    }
}