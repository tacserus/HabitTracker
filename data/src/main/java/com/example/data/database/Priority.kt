package com.example.data.database

import com.example.data.R

enum class Priority(val id: Int) {
    Lite(R.string.sometimes),
    Medium(R.string.often),
    Hard(R.string.daily)
}