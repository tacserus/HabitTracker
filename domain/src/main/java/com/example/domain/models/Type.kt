package com.example.domain.models

import java.io.Serializable

enum class Type(val description: String) : Serializable {
    GoodHabit("Хорошая привычка"),
    BadHabit("Плохая привычка");
}