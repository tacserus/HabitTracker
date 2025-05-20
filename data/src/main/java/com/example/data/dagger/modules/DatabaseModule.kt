package com.example.data.dagger.modules

import android.content.Context
import androidx.room.Room
import com.example.data.HabitRepositoryImpl
import com.example.data.R
import com.example.data.api.HabitApiService
import com.example.data.database.HabitDao
import com.example.data.database.HabitsDb
import dagger.Module
import dagger.Provides
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
        ).fallbackToDestructiveMigration().build()
    }

    @Singleton
    @Provides
    fun provideHabitDao(habitsDb: HabitsDb): HabitDao {
        return habitsDb.habitDao
    }

    @Singleton
    @Provides
    fun provideHabitRepositoryImpl(
        habitDao: HabitDao,
        habitApiService: HabitApiService
    ): HabitRepositoryImpl {
        return HabitRepositoryImpl(habitDao, habitApiService)
    }
}