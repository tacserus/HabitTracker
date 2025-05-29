package com.example.habittracker.presentation.activities

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import com.example.habittracker.dagger.App
import com.example.habittracker.presentation.compose.ComposeAppNavigation
import com.example.habittracker.presentation.viewmodels.AddHabitViewModelFactory
import com.example.habittracker.presentation.viewmodels.HabitListViewModelFactory
import javax.inject.Inject


class MainActivity : AppCompatActivity() {
    @Inject
    lateinit var habitListViewModelFactory: HabitListViewModelFactory
    @Inject
    lateinit var addHabitViewModelFactory: AddHabitViewModelFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        (application as App).presentationSubcomponent.inject(this)

        super.onCreate(savedInstanceState)
        setContent {
            ComposeAppNavigation(
                habitListViewModelFactory,
                addHabitViewModelFactory
            )
        }
    }
}