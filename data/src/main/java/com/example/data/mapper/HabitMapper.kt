package com.example.data.mapper

import com.example.data.database.HabitEntity
import com.example.domain.models.AddHabitState
import com.example.domain.models.HabitDto
import com.example.domain.models.HabitModel
import com.example.domain.models.HabitStatus
import com.example.domain.models.Priority
import com.example.domain.models.Type
import java.util.UUID

class HabitMapper {
    companion object {
        const val TAG = "HabitMapper"
        val INSTANCE: HabitMapper = HabitMapper()
    }

    fun entityToModel(habitEntity: HabitEntity): HabitModel {

        return HabitModel(
            id = habitEntity.id,
            apiId = habitEntity.apiId,
            title = habitEntity.title,
            description = habitEntity.description,
            priority = habitEntity.priority,
            type = habitEntity.type,
            count = habitEntity.count,
            frequency = habitEntity.frequency,
            color = habitEntity.color,
            date = habitEntity.date,
            habitStatus = habitEntity.habitStatus,
            doneMarks = habitEntity.doneMarks,
            isDoneMarksSynced = habitEntity.isDoneMarksSynced
        )
    }

    fun modelToEntity(habitModel: HabitModel): HabitEntity {
        return HabitEntity(
            id = habitModel.id,
            apiId = habitModel.apiId,
            title = habitModel.title,
            description = habitModel.description,
            priority = habitModel.priority,
            type = habitModel.type,
            count = habitModel.count,
            frequency = habitModel.frequency,
            color = habitModel.color,
            date = habitModel.date,
            habitStatus = habitModel.habitStatus,
            doneMarks = habitModel.doneMarks,
            isDoneMarksSynced = habitModel.isDoneMarksSynced
        )
    }

    fun dtoToEntity(habitDto: HabitDto, existingLocalId: String? = null): HabitEntity {
        val localId = existingLocalId ?: UUID.randomUUID().toString()
        return HabitEntity(
            id = localId,
            apiId = habitDto.uid,
            title = habitDto.title,
            description = habitDto.description,
            priority = Priority.entries.toTypedArray().getOrElse(habitDto.priority) { Priority.Lite },
            type = Type.entries.toTypedArray().getOrElse(habitDto.type) { Type.GoodHabit },
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

    fun stateToModel(addHabitState: AddHabitState, priority: Priority, type: Type): HabitModel {
        return HabitModel(
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

    fun modelToState(habitModel: HabitModel, defaultPriority: String, defaultType: String): AddHabitState {
        return AddHabitState(
            id = habitModel.id,
            apiId = habitModel.apiId,
            title = habitModel.title,
            description = habitModel.description,
            priority = defaultPriority,
            type = defaultType,
            count = habitModel.count,
            frequency = habitModel.frequency,
            habitStatus = if (habitModel.apiId == null) HabitStatus.ADD else HabitStatus.UPDATE,
            doneMarks = habitModel.doneMarks.toList(),
            isDoneMarksSynced = habitModel.isDoneMarksSynced
        )
    }
}