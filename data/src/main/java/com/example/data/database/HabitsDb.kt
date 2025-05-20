package com.example.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(
    entities = [
        HabitEntity::class
    ],
    version = 1
)
@TypeConverters(Converters::class)
abstract class HabitsDb : RoomDatabase() {
    abstract val habitDao: HabitDao
}