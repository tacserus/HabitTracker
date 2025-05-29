package com.example.domain.usecases

import com.example.domain.HabitRepository
import com.example.domain.models.Habit

class AddDoneMarkUseCase(private val repository: HabitRepository) {
    suspend fun execute(habit: Habit, date: Long) {
        repository.updateDoneMark(habit, date)
    }
}