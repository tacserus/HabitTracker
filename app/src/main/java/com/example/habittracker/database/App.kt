package com.example.habittracker.database

import android.app.Application

class App : Application() {
    val database by lazy { HabitsDb.createDataBase(this) }
}