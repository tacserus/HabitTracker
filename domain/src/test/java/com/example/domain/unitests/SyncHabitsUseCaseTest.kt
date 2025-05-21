package com.example.domain.unitests

import com.example.domain.HabitRepository
import com.example.domain.usecases.SyncHabitsUseCase
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

class SyncHabitsUseCaseTest {
    private lateinit var syncHabitsUseCase: SyncHabitsUseCase

    @Test
    fun `execute should call repository syncHabits`() = runTest {
        val mockHabitRepository: HabitRepository = mockk(relaxed = true)
        syncHabitsUseCase = SyncHabitsUseCase(mockHabitRepository)

        syncHabitsUseCase.execute()

        coVerify(exactly = 1) { mockHabitRepository.syncHabits() }
    }
}