package com.example.domain.models

import java.io.Serializable

data class Habit (
    val id: String,
    val title: String,
    val description: String,
    val priority: Priority,
    val type: Type,
    val count: String,
    val frequency: String,
    val color: Int,
    val date: Long,
    val doneMarks: List<Long>
) : Serializable