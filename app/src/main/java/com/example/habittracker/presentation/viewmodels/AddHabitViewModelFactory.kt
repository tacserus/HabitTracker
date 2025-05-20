package com.example.habittracker.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.domain.usecases.AddHabitUseCase
import com.example.domain.usecases.GetHabitByIdUseCase
import com.example.domain.usecases.UpdateHabitUseCase
import javax.inject.Inject

@Suppress("UNCHECKED_CAST")
class AddHabitViewModelFactory @Inject constructor(
    val addHabitUseCase: AddHabitUseCase,
    val updateHabitUseCase: UpdateHabitUseCase,
    val getHabitByIdUseCase: GetHabitByIdUseCase
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return AddHabitViewModel(addHabitUseCase, updateHabitUseCase, getHabitByIdUseCase) as T
    }
}