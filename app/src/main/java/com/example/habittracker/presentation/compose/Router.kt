package com.example.habittracker.presentation.compose

sealed class Router(val route: String) {
    data object HabitsPager : Router("habits_pager_screen")

    data object AddEditHabit : Router("add_edit_habit_screen") {
        fun createRoute(habitId: String?) = "add_edit_habit_screen?habitId=$habitId"
    }

    data object Info : Router("info_screen")
}