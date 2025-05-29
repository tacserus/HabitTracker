package com.example.domain.usecases

import com.example.domain.HabitRepository
import com.example.domain.models.Habit
import kotlinx.coroutines.flow.Flow

class GetAllHabitsUseCase(private val repository: HabitRepository) {
    suspend fun execute(): Flow<List<Habit>> {
        return repository.getAllHabits()
    }
}