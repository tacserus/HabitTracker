package com.example.habittracker.dagger.components

import com.example.data.dagger.modules.DatabaseModule
import com.example.data.dagger.modules.RetrofitModule
import com.example.habittracker.dagger.modules.ContextModule
import dagger.Component
import javax.inject.Singleton


@Singleton
@Component(modules = [ContextModule::class, DatabaseModule::class, RetrofitModule::class])
interface AppComponent {
    fun presentationSubcomponentFactory(): PresentationSubcomponent.Factory
}
