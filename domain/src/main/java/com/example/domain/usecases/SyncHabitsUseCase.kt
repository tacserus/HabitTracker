package com.example.domain.usecases

import com.example.domain.HabitRepository

class SyncHabitsUseCase(private val repository: HabitRepository) {
    suspend fun execute() {
        return repository.syncHabits()
    }
}