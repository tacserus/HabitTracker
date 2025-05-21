package com.example.habittracker.testscreens

import androidx.test.espresso.matcher.ViewMatchers.isChecked
import androidx.test.espresso.matcher.ViewMatchers.isNotChecked
import androidx.test.espresso.matcher.ViewMatchers.withSpinnerText
import com.example.domain.models.Priority
import com.example.habittracker.R
import io.github.kakaocup.kakao.common.views.KView
import io.github.kakaocup.kakao.edit.KEditText
import io.github.kakaocup.kakao.screen.Screen

class AddHabitTestScreen : Screen<AddHabitTestScreen>() {
    val editTextTitle = KEditText { withId(R.id.editTextTitle) }
    val editTextDescription = KEditText { withId(R.id.editTextDescription) }
    val spinnerPriority = KView { withId(R.id.spinnerPriority) }
    val radioButtonGoodHabit = KView { withId(R.id.radioButtonGoodHabit) }
    val radioButtonBadHabit = KView { withId(R.id.radioButtonBadHabit) }
    val editTextQuantity = KEditText { withId(R.id.editTextQuantity) }
    val editTextFrequency = KEditText { withId(R.id.editTextFrequency) }

    fun checkEmptyState() {
        editTextTitle {
            isVisible()
            hasText("")
        }
        editTextDescription {
            isVisible()
            hasText("")
        }
        spinnerPriority {
            isVisible()
            withSpinnerText(Priority.Lite.description)
        }
        radioButtonGoodHabit {
            isVisible()
            isChecked()
        }
        radioButtonBadHabit {
            isVisible()
            isNotChecked()
        }
        editTextQuantity {
            isVisible()
            hasText("")
        }
        editTextFrequency {
            isVisible()
            hasText("")
        }
    }
}