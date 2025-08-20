package com.example.data.api

data class HabitDto (
    val uid: String?,
    val color: Int,
    val count: Int,
    val date: Int,
    val description: String,
    val done_dates: List<Long>,
    val frequency: Int,
    val priority: Int,
    val title: String,
    val type: Int
)