package com.example.habittracker.dagger.components

import com.example.habittracker.dagger.modules.AddHabitViewModelModule
import com.example.habittracker.dagger.modules.ContextModule
import com.example.habittracker.dagger.modules.DatabaseModule
import com.example.habittracker.dagger.modules.HabitListViewModelModule
import com.example.habittracker.dagger.modules.RetrofitModule
import com.example.habittracker.presentation.fragments.AddHabitFragment
import com.example.habittracker.presentation.fragments.BottomSheetFragment
import com.example.habittracker.presentation.fragments.HabitFragment
import dagger.Component
import javax.inject.Singleton


@Singleton
@Component(modules = [
    ContextModule::class,
    RetrofitModule::class,
    DatabaseModule::class,
    HabitListViewModelModule::class,
    AddHabitViewModelModule::class
])
interface AppComponent {
    fun inject(fragment: HabitFragment)
    fun inject(fragment: BottomSheetFragment)
    fun inject(fragment: AddHabitFragment)
}