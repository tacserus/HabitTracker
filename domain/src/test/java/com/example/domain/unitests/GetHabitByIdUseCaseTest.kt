package com.example.domain.unitests

import com.example.domain.TestHabitModel.Companion.firstTestHabitModel
import com.example.domain.HabitRepository
import com.example.domain.usecases.GetHabitByIdUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class GetHabitByIdUseCaseTest {
    private lateinit var getHabitByIdUseCase: GetHabitByIdUseCase
    private val existingHabitId = "test_id_1"
    private val nonExistingHabitId = "non_existent_id"

    @Test
    fun `execute with existing id should return habit model from repository`() = runTest {
        val mockHabitRepository: HabitRepository = mockk(relaxed = true)
        getHabitByIdUseCase = GetHabitByIdUseCase(mockHabitRepository)

        coEvery { mockHabitRepository.getHabitById(existingHabitId) } returns firstTestHabitModel

        val result = getHabitByIdUseCase.execute(existingHabitId)

        coVerify(exactly = 1) { mockHabitRepository.getHabitById(existingHabitId) }

        assertEquals(firstTestHabitModel, result)
    }

    @Test
    fun `execute with non existing id should return null from repository`() = runTest {
        val mockHabitRepository: HabitRepository = mockk(relaxed = true)
        getHabitByIdUseCase = GetHabitByIdUseCase(mockHabitRepository)

        coEvery { mockHabitRepository.getHabitById(nonExistingHabitId) } returns null

        val result = getHabitByIdUseCase.execute(nonExistingHabitId)

        coVerify(exactly = 1) { mockHabitRepository.getHabitById(nonExistingHabitId) }

        assertEquals(null, result)
    }
}