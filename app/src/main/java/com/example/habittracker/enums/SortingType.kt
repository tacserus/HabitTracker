package com.example.habittracker.enums

import com.example.habittracker.R

enum class SortingType(val id: Int) {
    DEFAULT(R.string.habit_name),
    QUANTITY(R.string.habit_quantity),
    FREQUENCY(R.string.habit_frequency)
}