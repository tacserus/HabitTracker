package com.example.habittracker.dagger.modules

import android.content.Context
import androidx.room.Room
import com.example.habittracker.R
import com.example.habittracker.data.database.HabitDao
import com.example.habittracker.data.database.HabitsDb
import com.example.habittracker.data.database.HabitsRepository
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import javax.inject.Singleton


@Module
class DatabaseModule {
    @Singleton
    @Provides
    fun provideHabitsDb(context: Context): HabitsDb {
        return Room.databaseBuilder(
            context.applicationContext,
            HabitsDb::class.java,
            context.getString(R.string.table_name)
        ).allowMainThreadQueries().fallbackToDestructiveMigration().build()
    }

    @Singleton
    @Provides
    fun provideHabitDao(habitsDb: HabitsDb): HabitDao {
        return habitsDb.habitDao
    }

    @Singleton
    @Provides
    fun provideRepository(habitsDb: HabitsDb, retrofit: Retrofit): HabitsRepository {
        return HabitsRepository(habitsDb, retrofit)
    }
}