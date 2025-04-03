package com.example.habittracker.models

data class AddHabitState (
    val id: Long?,
    val title: String,
    val description: String,
    val priority: String,
    val type: String,
    val quantity: String,
    val frequency: String
)