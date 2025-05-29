package com.example.data.mapper

import com.example.data.api.HabitDto
import com.example.data.database.HabitEntity
import com.example.data.database.HabitStatus
import com.example.domain.models.Habit
import com.example.domain.models.Priority
import com.example.domain.models.Type
import com.example.domain.models.states.AddHabitState
import java.util.UUID

class HabitMapper {
    companion object {
        const val TAG = "HabitMapper"
        val INSTANCE: HabitMapper = HabitMapper()
    }

    fun entityToModel(habitEntity: HabitEntity): Habit {

        return Habit(
            id = habitEntity.id,
            title = habitEntity.title,
            description = habitEntity.description,
            priority = habitEntity.priority,
            type = habitEntity.type,
            count = habitEntity.count,
            frequency = habitEntity.frequency,
            color = habitEntity.color,
            date = habitEntity.date,
            doneMarks = habitEntity.doneMarks
        )
    }

    fun modelToEntity(
        habit: Habit,
        habitStatus: HabitStatus,
        apiId: String? = null
    ): HabitEntity {
        return HabitEntity(
            id = habit.id,
            apiId = apiId,
            title = habit.title,
            description = habit.description,
            priority = habit.priority,
            type = habit.type,
            count = habit.count,
            frequency = habit.frequency,
            color = habit.color,
            date = habit.date,
            habitStatus = habitStatus,
            doneMarks = habit.doneMarks,
            isDoneMarksSynced = false
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

    fun stateToModel(addHabitState: AddHabitState, priority: Priority, type: Type): Habit {
        return Habit(
            id = addHabitState.id,
            title = addHabitState.title,
            description = addHabitState.description,
            priority = priority,
            type = type,
            count = addHabitState.count,
            frequency = addHabitState.frequency,
            color = 0,
            date = System.currentTimeMillis(),
            doneMarks = addHabitState.doneMarks.toList()
        )
    }

    fun modelToState(habit: Habit, defaultPriority: String, defaultType: String): AddHabitState {
        return AddHabitState(
            id = habit.id,
            title = habit.title,
            description = habit.description,
            priority = defaultPriority,
            type = defaultType,
            count = habit.count,
            frequency = habit.frequency,
            doneMarks = habit.doneMarks.toList()
        )
    }
}