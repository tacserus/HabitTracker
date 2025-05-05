package com.example.habittracker.dagger.modules

import android.app.Application
import android.content.Context
import com.example.habittracker.data.database.HabitsRepository
import com.example.habittracker.presentation.viewmodels.AddHabitViewModel
import dagger.Module
import dagger.Provides

@Module
class AddHabitViewModelModule {
    @Provides
    fun provideAddHabitViewModel(
        habitsRepository: HabitsRepository,
        context: Context
    ): AddHabitViewModel {
        return AddHabitViewModel(
            habitsRepository,
            context as Application
        )
    }
}