package com.example.habittracker.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.domain.usecases.AddDoneMarkUseCase
import com.example.domain.usecases.DeleteHabitUseCase
import com.example.domain.usecases.GetAllHabitsUseCase
import com.example.domain.usecases.GetHabitByIdUseCase
import com.example.domain.usecases.GetListAllHabitsUseCase
import com.example.domain.usecases.SyncHabitsUseCase
import javax.inject.Inject

@Suppress("UNCHECKED_CAST")
class HabitListViewModelFactory @Inject constructor (
    val getAllHabitsUseCase: GetAllHabitsUseCase,
    val deleteHabitUseCase: DeleteHabitUseCase,
    val addDoneMarkUseCase: AddDoneMarkUseCase,
    val getHabitByIdUseCase: GetHabitByIdUseCase,
    val syncHabitsUseCase: SyncHabitsUseCase,
    val getListAllHabitsUseCase: GetListAllHabitsUseCase
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return HabitListViewModel(
            getAllHabitsUseCase,
            deleteHabitUseCase,
            addDoneMarkUseCase,
            getHabitByIdUseCase,
            syncHabitsUseCase,
            getListAllHabitsUseCase
        ) as T
    }
}