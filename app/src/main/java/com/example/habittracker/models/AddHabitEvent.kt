package com.example.habittracker.models

sealed class AddHabitEvent {
    data object NavigateBack : AddHabitEvent()
}