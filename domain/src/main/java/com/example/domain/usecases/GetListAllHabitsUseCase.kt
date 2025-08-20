package com.example.domain.usecases

import com.example.domain.HabitRepository
import com.example.domain.models.Habit

class GetListAllHabitsUseCase(private val repository: HabitRepository) {
    suspend fun execute(): List<Habit> {
        return repository.getListAllHabits()
    }
}