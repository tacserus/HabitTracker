package com.example.domain.models


data class AddHabitState (
    val id: String,
    val apiId: String?,
    val title: String,
    val description: String,
    val priority: String,
    val type: String,
    val count: String,
    val frequency: String,
    val habitStatus: HabitStatus,
    val doneMarks: List<Long>,
    val isDoneMarksSynced: Boolean
)