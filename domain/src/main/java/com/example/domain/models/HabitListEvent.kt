package com.example.domain.models

sealed class HabitListEvent {
    data class ShowLowToast(val difference: Int) : HabitListEvent()
    data object ShowHighToast: HabitListEvent()
}