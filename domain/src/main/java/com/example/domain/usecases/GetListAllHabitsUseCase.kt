package com.example.domain.usecases

import com.example.domain.HabitRepository
import com.example.domain.models.HabitModel

class GetListAllHabitsUseCase(private val repository: HabitRepository) {
    suspend fun execute(): List<HabitModel> {
        return repository.getListAllHabits()
    }
}