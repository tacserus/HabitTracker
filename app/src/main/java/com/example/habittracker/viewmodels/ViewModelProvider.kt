package com.example.habittracker.viewmodels

import androidx.fragment.app.FragmentActivity
import com.example.habittracker.database.App
import com.example.habittracker.database.HabitsRepositoryProvider

class ViewModelProvider private constructor() {
    private var habitListViewModel: HabitListViewModel? = null
    private var addHabitViewModel: AddHabitViewModel? = null

    fun getHabitListViewModel(context: FragmentActivity): HabitListViewModel {
        if (habitListViewModel == null) {
            val database = (context.application as App).database
            val repository = HabitsRepositoryProvider.getRepository(database)
            habitListViewModel = HabitListViewModel(repository)
        }

        return habitListViewModel!!
    }

    fun getAddHabitViewModel(context: FragmentActivity): AddHabitViewModel {
        if (addHabitViewModel == null) {
            val database = (context.application as App).database
            val repository = HabitsRepositoryProvider.getRepository(database)
            addHabitViewModel = AddHabitViewModel(repository)
        }

        return addHabitViewModel!!
    }

    companion object {
        val instance: ViewModelProvider by lazy { ViewModelProvider() }
    }
}