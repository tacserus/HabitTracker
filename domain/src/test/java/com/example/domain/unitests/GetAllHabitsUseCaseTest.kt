package com.example.domain.unitests

import com.example.domain.TestHabitModel.Companion.firstTestHabitModel
import com.example.domain.TestHabitModel.Companion.secondTestHabitModel
import com.example.domain.HabitRepository
import com.example.domain.models.HabitModel
import com.example.domain.usecases.GetAllHabitsUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class GetAllHabitsUseCaseTest {
    private lateinit var getAllHabitsUseCase: GetAllHabitsUseCase

    @Test
    fun `execute should return flow from repository getAllHabits`() = runTest {
        val mockHabitRepository: HabitRepository = mockk(relaxed = true)
        getAllHabitsUseCase = GetAllHabitsUseCase(mockHabitRepository)

        val testHabitList = listOf(firstTestHabitModel, secondTestHabitModel)

        coEvery { mockHabitRepository.getAllHabits() } returns flowOf(testHabitList)

        val result = getAllHabitsUseCase.execute().toList()

        coVerify(exactly = 1) { mockHabitRepository.getAllHabits() }

        assertEquals(listOf(testHabitList), result)
    }

    @Test
    fun `execute should return empty list when repository returns empty flow`() = runTest {
        val mockHabitRepository: HabitRepository = mockk(relaxed = true)
        getAllHabitsUseCase = GetAllHabitsUseCase(mockHabitRepository)

        coEvery { mockHabitRepository.getAllHabits() } returns flowOf(emptyList())

        val result = getAllHabitsUseCase.execute().toList()

        coVerify(exactly = 1) { mockHabitRepository.getAllHabits() }

        assertEquals(listOf(emptyList<HabitModel>()), result)
    }
}