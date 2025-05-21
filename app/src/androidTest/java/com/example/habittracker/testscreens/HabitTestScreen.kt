package com.example.habittracker.testscreens

import com.example.habittracker.R
import io.github.kakaocup.kakao.screen.Screen
import io.github.kakaocup.kakao.text.KButton

class HabitTestScreen : Screen<HabitTestScreen>() {
    val addingFab = KButton { withId(R.id.addingFab) }
}
