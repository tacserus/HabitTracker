package com.example.habittracker.dagger.modules

import androidx.lifecycle.ViewModelProvider
import com.example.habittracker.data.HabitRepository
import com.example.habittracker.presentation.viewmodels.ViewModelFactory
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class ViewModelFactoryModule {
    @Singleton
    @Provides
    fun provideViewModelFactory(habitRepository: HabitRepository): ViewModelProvider.Factory {
        return ViewModelFactory(habitRepository)
    }
}