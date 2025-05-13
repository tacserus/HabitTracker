package com.example.habittracker.dagger.modules

import com.example.habittracker.data.database.HabitRepository
import com.example.habittracker.presentation.viewmodels.AddHabitViewModel
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AddHabitViewModelModule {
    @Singleton
    @Provides
    fun provideAddHabitViewModel(
        habitRepository: HabitRepository
    ): AddHabitViewModel {
        return AddHabitViewModel(
            habitRepository
        )
    }
}