package com.example.data.mapper

import com.example.data.database.HabitEntity
import com.example.data.database.HabitType
import com.example.data.database.Priority
import com.example.domain.models.AddHabitState
import com.example.domain.models.HabitDto
import com.example.domain.models.HabitStatus
import java.util.UUID

class HabitMapper {
    companion object {
        const val TAG = "HabitMapper"
        val INSTANCE: HabitMapper = HabitMapper()
    }

    fun dtoToEntity(habitDto: HabitDto, existingLocalId: String? = null): HabitEntity {
        val localId = existingLocalId ?: UUID.randomUUID().toString()
        return HabitEntity(
            id = localId,
            apiId = habitDto.uid,
            title = habitDto.title,
            description = habitDto.description,
            priority = Priority.entries.toTypedArray().getOrElse(habitDto.priority) { Priority.Lite },
            type = HabitType.entries.toTypedArray().getOrElse(habitDto.type) { HabitType.GoodHabit },
            count = habitDto.count.toString(),
            frequency = habitDto.frequency.toString(),
            color = habitDto.color,
            date = habitDto.date.toLong() * 1000,
            habitStatus = HabitStatus.SYNCED,
            doneMarks = habitDto.done_dates.map { it * 1000 },
            isDoneMarksSynced = true
        )
    }

    fun entityToDto(habitEntity: HabitEntity): HabitDto {
        return HabitDto(
            uid = habitEntity.apiId,
            title = habitEntity.title,
            description = habitEntity.description,
            priority = habitEntity.priority.ordinal,
            type = habitEntity.type.ordinal,
            count = habitEntity.count.toIntOrNull() ?: 0,
            frequency = habitEntity.frequency.toIntOrNull() ?: 0,
            color = habitEntity.color,
            date = (habitEntity.date / 1000).toInt(),
            done_dates = habitEntity.doneMarks.map { it / 1000 }
        )
    }

    fun stateToEntity(addHabitState: AddHabitState, priority: Priority, type: HabitType): HabitEntity {
        return HabitEntity(
            id = addHabitState.id,
            apiId = addHabitState.apiId,
            title = addHabitState.title,
            description = addHabitState.description,
            priority = priority,
            type = type,
            count = addHabitState.count,
            frequency = addHabitState.frequency,
            color = 0,
            date = System.currentTimeMillis(),
            habitStatus = addHabitState.habitStatus,
            doneMarks = addHabitState.doneMarks.toList(),
            isDoneMarksSynced = addHabitState.isDoneMarksSynced
        )
    }

    fun entityToState(habitEntity: HabitEntity, defaultPriority: String, defaultType: String): AddHabitState {
        return AddHabitState(
            id = habitEntity.id,
            apiId = habitEntity.apiId,
            title = habitEntity.title,
            description = habitEntity.description,
            priority = defaultPriority,
            type = defaultType,
            count = habitEntity.count,
            frequency = habitEntity.frequency,
            habitStatus = if (habitEntity.apiId == null) HabitStatus.ADD else HabitStatus.UPDATE,
            doneMarks = habitEntity.doneMarks.toList(),
            isDoneMarksSynced = habitEntity.isDoneMarksSynced
        )
    }
}