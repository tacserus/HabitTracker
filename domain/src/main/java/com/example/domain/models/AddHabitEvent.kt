package com.example.domain.models

sealed class AddHabitEvent {
    data object NavigateBack : AddHabitEvent()
}