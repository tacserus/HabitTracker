package com.example.habittracker.domain.models

sealed class AddHabitEvent {
    data object NavigateBack : AddHabitEvent()
}