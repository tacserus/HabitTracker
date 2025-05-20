package com.example.habittracker.dagger.modules

import com.example.data.HabitRepositoryImpl
import com.example.domain.usecases.AddDoneMarkUseCase
import com.example.domain.usecases.AddHabitUseCase
import com.example.domain.usecases.DeleteHabitUseCase
import com.example.domain.usecases.GetAllHabitsUseCase
import com.example.domain.usecases.GetHabitByIdUseCase
import com.example.domain.usecases.GetListAllHabitsUseCase
import com.example.domain.usecases.SyncHabitsUseCase
import com.example.domain.usecases.UpdateHabitUseCase
import dagger.Module
import dagger.Provides

@Module
class DomainModule {
    @Provides
    fun provideAddHabitUseCase(habitRepositoryImpl: HabitRepositoryImpl): AddHabitUseCase {
        return AddHabitUseCase(habitRepositoryImpl)
    }

    @Provides
    fun provideGetHabitByIdUseCase(habitRepositoryImpl: HabitRepositoryImpl): GetHabitByIdUseCase {
        return GetHabitByIdUseCase(habitRepositoryImpl)
    }

    @Provides
    fun provideGetAllHabitsUseCase(habitRepositoryImpl: HabitRepositoryImpl): GetAllHabitsUseCase {
        return GetAllHabitsUseCase(habitRepositoryImpl)
    }

    @Provides
    fun provideUpdateHabitsUseCase(habitRepositoryImpl: HabitRepositoryImpl): UpdateHabitUseCase {
        return UpdateHabitUseCase(habitRepositoryImpl)
    }

    @Provides
    fun provideDeleteHabitUseCase(habitRepositoryImpl: HabitRepositoryImpl): DeleteHabitUseCase {
        return DeleteHabitUseCase(habitRepositoryImpl)
    }

    @Provides
    fun provideAddHabitDoneDateUseCase(habitRepositoryImpl: HabitRepositoryImpl): AddDoneMarkUseCase {
        return AddDoneMarkUseCase(habitRepositoryImpl)
    }

    @Provides
    fun provideGetListAllHabitsUseCase(habitRepositoryImpl: HabitRepositoryImpl): GetListAllHabitsUseCase {
        return GetListAllHabitsUseCase(habitRepositoryImpl)
    }

    @Provides
    fun provideSyncHabitsUseCase(habitRepositoryImpl: HabitRepositoryImpl): SyncHabitsUseCase {
        return SyncHabitsUseCase(habitRepositoryImpl)
    }
}