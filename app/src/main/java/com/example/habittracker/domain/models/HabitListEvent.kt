package com.example.habittracker.domain.models

sealed class HabitListEvent {
    data class ShowLowToast(val difference: Int) : HabitListEvent()
    data object ShowHighToast: HabitListEvent()
}