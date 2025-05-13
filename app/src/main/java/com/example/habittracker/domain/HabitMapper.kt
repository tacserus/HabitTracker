package com.example.habittracker.domain

import android.app.Application
import com.example.habittracker.domain.enums.HabitStatus
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

    fun dtoToEntity(dto: HabitDto, existingLocalId: String? = null): HabitEntity {
        val localId = existingLocalId ?: UUID.randomUUID().toString()
        return HabitEntity(
            id = localId,
            apiId = dto.uid,
            title = dto.title,
            description = dto.description,
            priority = Priority.entries.toTypedArray().getOrElse(dto.priority) { Priority.Lite },
            type = HabitType.entries.toTypedArray().getOrElse(dto.type) { HabitType.GoodHabit },
            count = dto.count.toString(),
            frequency = dto.frequency.toString(),
            color = dto.color,
            date = dto.date.toLong() * 1000,
            habitStatus = HabitStatus.SYNCED
        )
    }

    fun entityToDto(entity: HabitEntity): HabitDto {
        return HabitDto(
            uid = entity.apiId,
            title = entity.title,
            description = entity.description,
            priority = entity.priority.ordinal,
            type = entity.type.ordinal,
            count = entity.count.toIntOrNull() ?: 0,
            frequency = entity.frequency.toIntOrNull() ?: 0,
            color = entity.color,
            date = (entity.date / 1000).toInt(),
            done_dates = listOf()
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
            date = System.currentTimeMillis(),
            habitStatus = state.habitStatus
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
            habitStatus = if (habitEntity.apiId == null) HabitStatus.ADD else HabitStatus.UPDATE
        )
    }
}