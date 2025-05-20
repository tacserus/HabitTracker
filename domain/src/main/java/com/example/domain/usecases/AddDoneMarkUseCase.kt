package com.example.domain.usecases

import com.example.domain.HabitRepository

class AddDoneMarkUseCase(private val repository: HabitRepository) {
    suspend fun execute(id: String) {
        repository.updateDoneMark(id)
    }
}