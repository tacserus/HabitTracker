package com.example.domain.models

import java.io.Serializable

data class HabitModel (
    val id: String,
    val apiId: String? = null,
    val title: String,
    val description: String,
    val priority: Priority,
    val type: Type,
    val count: String,
    val frequency: String,
    val color: Int,
    val date: Long,
    val habitStatus: HabitStatus,
    val doneMarks: List<Long>,
    val isDoneMarksSynced: Boolean
) : Serializable