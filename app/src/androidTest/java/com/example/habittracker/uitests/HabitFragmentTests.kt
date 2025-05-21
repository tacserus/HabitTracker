package com.example.habittracker.uitests

import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.habittracker.presentation.activities.MainActivity
import com.example.habittracker.testscreens.AddHabitTestScreen
import com.example.habittracker.testscreens.HabitTestScreen
import io.github.kakaocup.kakao.screen.Screen.Companion.onScreen
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class HabitFragmentTests {
    @get:Rule
    val activityRule = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun addButton_shouldOpenAddHabitTestScreen() {
        onScreen<HabitTestScreen> {
            addingFab.click()
        }
        onScreen<AddHabitTestScreen> {
            checkEmptyState()
        }
    }
}
