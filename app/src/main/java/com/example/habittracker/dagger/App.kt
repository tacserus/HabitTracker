package com.example.habittracker.dagger

import android.app.Application
import com.example.habittracker.dagger.components.AppComponent
import com.example.habittracker.dagger.components.DaggerAppComponent
import com.example.habittracker.dagger.components.PresentationSubcomponent
import com.example.habittracker.dagger.modules.ContextModule

class App : Application() {
    private lateinit var appComponent: AppComponent
    lateinit var presentationSubcomponent: PresentationSubcomponent

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent
            .builder()
            .contextModule(ContextModule(context = this))
            .build()

        presentationSubcomponent = appComponent.presentationSubcomponentFactory().create()
    }
}