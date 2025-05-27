package com.example.domain.usecases

import com.example.domain.HabitRepository
import com.example.domain.models.HabitModel

class AddDoneMarkUseCase(private val repository: HabitRepository) {
    suspend fun execute(habitModel: HabitModel, date: Long) {
        repository.updateDoneMark(habitModel, date)
    }
}