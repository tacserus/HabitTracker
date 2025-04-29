package com.example.habittracker.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.habittracker.R
import com.example.habittracker.domain.models.Habit

@Database(
    entities = [
        Habit::class
    ],
    version = 1
)
abstract class HabitsDb : RoomDatabase() {
    abstract val habitDao: HabitDao

    companion object {
        fun createDataBase(context: Context): HabitsDb {
            return Room.databaseBuilder(
                context,
                HabitsDb::class.java,
                context.getString(R.string.table_name)
            ).allowMainThreadQueries().build()
        }
    }
}