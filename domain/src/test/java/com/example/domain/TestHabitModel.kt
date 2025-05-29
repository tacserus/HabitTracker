package com.example.domain

import com.example.domain.models.Habit
import com.example.domain.models.Priority
import com.example.domain.models.Type
import java.time.LocalDateTime
import java.time.ZoneOffset

class TestHabitModel {
    companion object {
        private val today = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC)
        private val tomorrow = LocalDateTime.now().plusDays(1).toEpochSecond(ZoneOffset.UTC)

        val firstTestHabit = Habit(
            id = "test_id_1",
            title = "Test Habit 1",
            description = "Description 1",
            priority = Priority.Lite,
            type = Type.GoodHabit,
            count = "1",
            frequency = "1",
            color = 0,
            date = today,
            doneMarks = mutableListOf()
        )
        val secondTestHabit = Habit(
            id = "test_id_2",
            title = "Test Habit 2",
            description = "Description 2",
            priority = Priority.Hard,
            type = Type.BadHabit,
            count = "2",
            frequency = "2",
            color = 0,
            date = tomorrow,
            doneMarks = mutableListOf()
        )
    }
}