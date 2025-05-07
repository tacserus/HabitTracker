package com.example.habittracker.domain

import android.app.Application
import com.example.habittracker.domain.enums.HabitType
import com.example.habittracker.domain.enums.Priority
import com.example.habittracker.domain.models.AddHabitState
import com.example.habittracker.domain.models.HabitDto
import com.example.habittracker.domain.models.HabitEntity
import java.util.UUID

class HabitMapper {
    companion object {
        const val TAG = "HabitMapper"
        val INSTANCE: HabitMapper = HabitMapper()
    }

    fun dtoToEntity(habitDto: HabitDto): HabitEntity {
        return HabitEntity(
            id = habitDto.uid ?: UUID.randomUUID().toString(),
            apiId = habitDto.uid,
            title = habitDto.title,
            description = habitDto.description,
            priority = Priority.entries[habitDto.priority],
            type = HabitType.entries[habitDto.type],
            frequency = habitDto.frequency.toString(),
            count = habitDto.count.toString(),
            color = habitDto.color,
            date = habitDto.date.toLong() * 1000
        )
    }

    fun entityToDto(habitEntity: HabitEntity): HabitDto {
        return HabitDto(
            color = habitEntity.color,
            count = habitEntity.count.toInt(),
            date = (habitEntity.date / 1000).toInt(),
            description = habitEntity.description,
            done_dates = listOf(0),
            frequency = habitEntity.frequency.toInt(),
            priority = habitEntity.priority.ordinal,
            title = habitEntity.title,
            type = habitEntity.type.ordinal,
            uid = habitEntity.apiId
        )
    }

    fun stateToEntity(state: AddHabitState, application: Application): HabitEntity {
        val priority = Priority.entries.find {
            application.getString(it.id) == state.priority
        } ?: Priority.Lite
        val type = HabitType.entries.find { application.getString(it.id) == state.type } ?: HabitType.GoodHabit
        return HabitEntity(
            id = state.id,
            apiId = state.apiId,
            title = state.title,
            description = state.description,
            priority = priority,
            type = type,
            count = state.count,
            frequency = state.frequency,
            color = 0,
            date = System.currentTimeMillis()
        )
    }

    fun entityToState(habitEntity: HabitEntity, application: Application): AddHabitState {
        return AddHabitState(
            id = habitEntity.id,
            apiId = habitEntity.apiId,
            title = habitEntity.title,
            description = habitEntity.description,
            priority = application.getString(habitEntity.priority.id),
            type = application.getString(habitEntity.type.id),
            count = habitEntity.count,
            frequency = habitEntity.frequency,
        )
    }
}