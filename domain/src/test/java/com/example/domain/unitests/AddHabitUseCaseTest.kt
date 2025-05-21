package com.example.domain.unitests

import com.example.domain.TestHabitModel.Companion.firstTestHabitModel
import com.example.domain.HabitRepository
import com.example.domain.usecases.AddHabitUseCase
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

class AddHabitUseCaseTest {
    private lateinit var addHabitUseCase: AddHabitUseCase

    @Test
    fun `execute should call repository addHabit with correct habit model`() = runTest {
        val mockHabitRepository: HabitRepository = mockk(relaxed = true)
        addHabitUseCase = AddHabitUseCase(mockHabitRepository)

        addHabitUseCase.execute(firstTestHabitModel)

        coVerify(exactly = 1) { mockHabitRepository.addHabit(firstTestHabitModel) }
    }
}