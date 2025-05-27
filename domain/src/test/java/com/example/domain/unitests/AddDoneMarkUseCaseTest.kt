package com.example.domain.unitests

import com.example.domain.TestHabitModel.Companion.firstTestHabitModel
import com.example.domain.HabitRepository
import com.example.domain.usecases.AddDoneMarkUseCase
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import java.time.LocalDateTime
import java.time.ZoneOffset


class AddDoneMarkUseCaseTest {
    private lateinit var addDoneMarkUseCase: AddDoneMarkUseCase
    private val testDate = LocalDateTime.now().minusDays(2).toEpochSecond(ZoneOffset.UTC)


    @Test
    fun `execute should call repository updateDoneMark with correct parameters`() = runTest {
        val mockHabitRepository: HabitRepository = mockk(relaxed = true)
        addDoneMarkUseCase = AddDoneMarkUseCase(mockHabitRepository)

        addDoneMarkUseCase.execute(firstTestHabitModel, testDate)

        coVerify(exactly = 1) { mockHabitRepository.updateDoneMark(firstTestHabitModel, testDate) }
    }
}