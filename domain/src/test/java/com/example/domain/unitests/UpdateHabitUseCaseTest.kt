package com.example.domain.unitests

import com.example.domain.TestHabitModel.Companion.firstTestHabit
import com.example.domain.HabitRepository
import com.example.domain.usecases.UpdateHabitUseCase
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

class UpdateHabitUseCaseTest {
    private lateinit var updateHabitUseCase: UpdateHabitUseCase

    @Test
    fun `execute should call repository updateHabit with correct habit model`() = runTest {
        val mockHabitRepository: HabitRepository = mockk(relaxed = true)
        updateHabitUseCase = UpdateHabitUseCase(mockHabitRepository)

        updateHabitUseCase.execute(firstTestHabit)

        coVerify(exactly = 1) { mockHabitRepository.updateHabit(firstTestHabit) }
    }
}