package com.example.domain.models.states


data class AddHabitState (
    val id: String,
    val title: String,
    val description: String,
    val priority: String,
    val type: String,
    val count: String,
    val frequency: String,
    val doneMarks: List<Long>
)