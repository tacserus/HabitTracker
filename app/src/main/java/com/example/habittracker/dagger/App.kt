package com.example.habittracker.dagger

import android.app.Application
import com.example.habittracker.dagger.components.AppComponent
import com.example.habittracker.dagger.components.DaggerAppComponent
import com.example.habittracker.dagger.modules.ContextModule

class App : Application() {
    lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent
            .builder()
            .contextModule(ContextModule(context = this))
            .build()
    }
}