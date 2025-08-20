package com.example.habittracker.presentation.enums

import com.example.habittracker.R

enum class SortingType(val id: Int) {
    DEFAULT(R.string.habit_name),
    COUNT(R.string.habit_count),
    FREQUENCY(R.string.habit_frequency)
}