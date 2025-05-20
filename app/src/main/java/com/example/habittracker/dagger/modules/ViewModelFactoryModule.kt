package com.example.habittracker.dagger.modules

import androidx.lifecycle.ViewModelProvider
import com.example.domain.usecases.AddDoneMarkUseCase
import com.example.domain.usecases.AddHabitUseCase
import com.example.domain.usecases.DeleteHabitUseCase
import com.example.domain.usecases.GetAllHabitsUseCase
import com.example.domain.usecases.GetHabitByIdUseCase
import com.example.domain.usecases.GetListAllHabitsUseCase
import com.example.domain.usecases.SyncHabitsUseCase
import com.example.domain.usecases.UpdateHabitUseCase
import com.example.habittracker.presentation.viewmodels.AddHabitViewModelFactory
import com.example.habittracker.presentation.viewmodels.HabitListViewModelFactory
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class ViewModelFactoryModule {
    @Singleton
    @Provides
    fun provideAddHabitViewModelFactory(
        addHabitUseCase: AddHabitUseCase,
        updateHabitUseCase: UpdateHabitUseCase,
        getHabitByIdUseCase: GetHabitByIdUseCase
    ): ViewModelProvider.Factory {
        return AddHabitViewModelFactory(addHabitUseCase,updateHabitUseCase, getHabitByIdUseCase)
    }

    @Singleton
    @Provides
    fun provideHabitListViewModelFactory(
        getAllHabitsUseCase: GetAllHabitsUseCase,
        deleteHabitUseCase: DeleteHabitUseCase,
        addDoneMarkUseCase: AddDoneMarkUseCase,
        getHabitByIdUseCase: GetHabitByIdUseCase,
        syncHabitsUseCase: SyncHabitsUseCase,
        getListAllHabitsUseCase: GetListAllHabitsUseCase
    ): ViewModelProvider.Factory {
        return HabitListViewModelFactory(
            getAllHabitsUseCase,
            deleteHabitUseCase,
            addDoneMarkUseCase,
            getHabitByIdUseCase,
            syncHabitsUseCase,
            getListAllHabitsUseCase
        )
    }
}