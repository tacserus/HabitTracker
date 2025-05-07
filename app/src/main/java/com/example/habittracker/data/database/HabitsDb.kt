package com.example.habittracker.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.habittracker.domain.models.HabitEntity

@Database(
    entities = [
        HabitEntity::class
    ],
    version = 1
)
abstract class HabitsDb : RoomDatabase() {
    abstract val habitDao: HabitDao
}