package com.example.habittracker.data.database

import androidx.lifecycle.LiveData
import com.example.habittracker.domain.models.Habit

class HabitsRepository(private val habitsDb: HabitsDb) {
    fun getHabitById(id: String): Habit? {
        return habitsDb.habitDao.getItemById(id)
    }

    fun getAllHabits(): LiveData<List<Habit>> {
        return habitsDb.habitDao.getAllItems()
    }

    suspend fun updateHabit(habit: Habit) {
        habitsDb.habitDao.updateItem(habit)
    }

    suspend fun addHabit(habit: Habit) {
        habitsDb.habitDao.insertItem(habit)
    }

    fun deleteHabit(habit: Habit) {
        habitsDb.habitDao.deleteItem(habit)
    }
}