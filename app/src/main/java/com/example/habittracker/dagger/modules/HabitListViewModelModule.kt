package com.example.habittracker.dagger.modules

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
        habitRepository: HabitRepository
    ): HabitListViewModel {
        return HabitListViewModel(
            habitRepository
        )
    }
}