package com.example.domain

import com.example.domain.models.HabitModel
import kotlinx.coroutines.flow.Flow

interface HabitRepository {
    suspend fun getHabitById(id: String): HabitModel?
    suspend fun getAllHabits(): Flow<List<HabitModel>>
    suspend fun getListAllHabits(): List<HabitModel>

    suspend fun updateHabit(habitModel: HabitModel)
    suspend fun addHabit(habitModel: HabitModel)
    suspend fun deleteHabit(habitModel: HabitModel)
    suspend fun updateDoneMark(id: String)

    suspend fun syncHabits()
}