package com.example.habittracker.domain.enums

import com.example.habittracker.R

enum class ToastMessage(val id: Int) {
    GoodLow(R.string.good_low_message),
    GoodHigh(R.string.good_high_message),
    BadLow(R.string.bad_low_message),
    BadHigh(R.string.bad_high_message)
}