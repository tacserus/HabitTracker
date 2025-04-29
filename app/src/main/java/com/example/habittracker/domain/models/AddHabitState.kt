package com.example.habittracker.domain.models

data class AddHabitState (
    val id: String,
    val title: String,
    val description: String,
    val priority: String,
    val type: String,
    val quantity: String,
    val frequency: String
)