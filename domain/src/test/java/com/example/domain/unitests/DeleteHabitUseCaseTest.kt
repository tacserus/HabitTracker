package com.example.domain.unitests

import com.example.domain.TestHabitModel.Companion.firstTestHabitModel
import com.example.domain.HabitRepository
import com.example.domain.usecases.DeleteHabitUseCase
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

class DeleteHabitUseCaseTest {
    private lateinit var deleteHabitUseCase: DeleteHabitUseCase

    @Test
    fun `execute should call repository deleteHabit with correct habit model`() = runTest {
        val mockHabitRepository: HabitRepository = mockk(relaxed = true)
        deleteHabitUseCase = DeleteHabitUseCase(mockHabitRepository)

        deleteHabitUseCase.execute(firstTestHabitModel)

        coVerify(exactly = 1) { mockHabitRepository.deleteHabit(firstTestHabitModel) }
    }
}