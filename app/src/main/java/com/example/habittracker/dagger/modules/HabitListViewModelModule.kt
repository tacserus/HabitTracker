package com.example.habittracker.dagger.modules

import android.app.Application
import android.content.Context
import com.example.habittracker.data.database.HabitRepository
import com.example.habittracker.presentation.viewmodels.HabitListViewModel
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class HabitListViewModelModule {
    @Singleton
    @Provides
    fun provideHabitListViewModel(
        habitRepository: HabitRepository,
        context: Context
    ): HabitListViewModel {
        return HabitListViewModel(
            habitRepository,
            context as Application
        )
    }
}