package com.example.domain

import com.example.domain.models.Habit
import kotlinx.coroutines.flow.Flow

interface HabitRepository {
    suspend fun getHabitById(id: String): Habit?
    suspend fun getAllHabits(): Flow<List<Habit>>
    suspend fun getListAllHabits(): List<Habit>

    suspend fun updateHabit(habit: Habit)
    suspend fun addHabit(habit: Habit)
    suspend fun deleteHabit(habit: Habit)
    suspend fun updateDoneMark(habit: Habit, date: Long)

    suspend fun syncHabits()
}