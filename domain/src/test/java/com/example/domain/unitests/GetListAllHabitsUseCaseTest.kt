package com.example.domain.unitests

import com.example.domain.TestHabitModel.Companion.firstTestHabit
import com.example.domain.TestHabitModel.Companion.secondTestHabit
import com.example.domain.HabitRepository
import com.example.domain.models.Habit
import com.example.domain.usecases.GetListAllHabitsUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class GetListAllHabitsUseCaseTest {

    private lateinit var getListAllHabitsUseCase: GetListAllHabitsUseCase

    @BeforeEach
    fun setUp() {

    }

    @Test
    fun `execute should return list from repository getListAllHabits`() = runTest {
        val mockHabitRepository: HabitRepository = mockk(relaxed = true)
        getListAllHabitsUseCase = GetListAllHabitsUseCase(mockHabitRepository)

        val testHabitList = listOf(firstTestHabit, secondTestHabit)
        coEvery { mockHabitRepository.getListAllHabits() } returns testHabitList

        val result = getListAllHabitsUseCase.execute()

        coVerify(exactly = 1) { mockHabitRepository.getListAllHabits() }

        assertEquals(testHabitList, result)
    }

    @Test
    fun `execute should return empty list when repository returns empty list`() = runTest {
        val mockHabitRepository: HabitRepository = mockk(relaxed = true)
        getListAllHabitsUseCase = GetListAllHabitsUseCase(mockHabitRepository)

        coEvery { mockHabitRepository.getListAllHabits() } returns emptyList()

        val result = getListAllHabitsUseCase.execute()

        coVerify(exactly = 1) { mockHabitRepository.getListAllHabits() }

        assertEquals(emptyList<Habit>(), result)
    }
}