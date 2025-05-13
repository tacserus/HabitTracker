package com.example.habittracker.domain.models

import com.example.habittracker.domain.enums.HabitStatus

data class AddHabitState (
    val id: String,
    val apiId: String?,
    val title: String,
    val description: String,
    val priority: String,
    val type: String,
    val count: String,
    val frequency: String,
    val habitStatus: HabitStatus
)