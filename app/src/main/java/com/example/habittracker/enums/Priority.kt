package com.example.habittracker.enums

import com.example.habittracker.R

enum class Priority(val id: Int) {
    Lite(R.string.sometimes),
    Medium(R.string.often),
    Hard(R.string.daily)
}