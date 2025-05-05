package com.example.habittracker.dagger.modules

import android.app.Application
import android.content.Context
import com.example.habittracker.data.database.HabitsRepository
import com.example.habittracker.presentation.viewmodels.HabitListViewModel
import dagger.Module
import dagger.Provides

@Module
class HabitListViewModelModule {
    @Provides
    fun provideHabitListViewModel(
        habitsRepository: HabitsRepository,
        context: Context
    ): HabitListViewModel {
        return HabitListViewModel(
            habitsRepository,
            context as Application
        )
    }
}