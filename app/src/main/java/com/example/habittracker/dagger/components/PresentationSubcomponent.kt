package com.example.habittracker.dagger.components

import com.example.habittracker.dagger.modules.DomainModule
import com.example.habittracker.dagger.modules.ViewModelFactoryModule
import com.example.habittracker.presentation.fragments.AddHabitFragment
import com.example.habittracker.presentation.fragments.BottomSheetFragment
import com.example.habittracker.presentation.fragments.HabitFragment
import dagger.Subcomponent

@Subcomponent(modules = [ViewModelFactoryModule::class, DomainModule::class])
interface PresentationSubcomponent {
    @Subcomponent.Factory
    interface Factory {
        fun create(): PresentationSubcomponent
    }

    fun inject(fragment: HabitFragment)
    fun inject(fragment: BottomSheetFragment)
    fun inject(fragment: AddHabitFragment)
}