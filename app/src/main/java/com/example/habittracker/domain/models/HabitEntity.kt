package com.example.habittracker.domain.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.habittracker.domain.enums.HabitStatus
import com.example.habittracker.domain.enums.HabitType
import com.example.habittracker.domain.enums.Priority
import java.io.Serializable

@Entity(tableName = "habits")
data class HabitEntity (
    @PrimaryKey var id: String,
    val apiId: String? = null,
    val title: String,
    val description: String,
    val priority: Priority,
    val type: HabitType,
    val count: String,
    val frequency: String,
    val color: Int,
    val date: Long,
    val habitStatus: HabitStatus,
    val doneMarks: List<Long>,
    val isDoneMarksSynced: Boolean
) : Serializable