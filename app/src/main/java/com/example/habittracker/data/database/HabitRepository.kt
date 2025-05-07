package com.example.habittracker.data.database

import androidx.lifecycle.LiveData
import com.example.habittracker.data.api.HabitApiService
import com.example.habittracker.domain.HabitMapper
import com.example.habittracker.domain.models.HabitEntity
import com.example.habittracker.domain.models.HabitRequestUID
import kotlinx.coroutines.delay

class HabitRepository(
    private val habitDao: HabitDao,
    private val habitApiService: HabitApiService
) {
    private companion object {
        const val MAX_REPEAT_COUNT = 4
        const val REPEAT_DELAY = 1000L
    }

    suspend fun getHabitById(id: String): HabitEntity? {
        return habitDao.getItemById(id)
    }

    fun getAllHabits(): LiveData<List<HabitEntity>> {
        return habitDao.getAllItems()
    }

    suspend fun updateHabit(habit: HabitEntity) {
        insertHabitsForApi(listOf(habit))
    }

    suspend fun addHabit(habit: HabitEntity) {
        insertHabitsForApi(listOf(habit))
    }

    suspend fun deleteItem(habit: HabitEntity) {
        habit.isDeleted = true
        habitDao.insertItem(habit.copy())
        deleteHabitsForApi(listOf(habit))
    }

    suspend fun sync() {
        val apiHabits = getHabitsFromApi()
        val localHabits = getAllHabits().value ?: listOf()
        val syncedHabits = joinHabits(localHabits, apiHabits)
        val localDeletedHabits = habitDao.getDeletedHabits()

        insertHabitsForApi(syncedHabits)
        deleteHabitsForApi(localDeletedHabits)
    }

    private suspend fun getHabitsFromApi(): List<HabitEntity> {
        var apiHabits = listOf<HabitEntity>()
        var repeatCount = 0
        var isSuccessful = false

        while (!isSuccessful && repeatCount < MAX_REPEAT_COUNT) {
            repeatCount++
            try {
                val response = habitApiService.getHabits()
                if (response.isSuccessful) {
                    apiHabits = response.body()?.map {
                        HabitMapper.INSTANCE.dtoToEntity(it)
                    } ?: listOf()
                    isSuccessful = true
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }

            if (!isSuccessful)
                delay(REPEAT_DELAY)
        }
        return apiHabits
    }

    private fun joinHabits(localHabits: List<HabitEntity>, apiHabits: List<HabitEntity>): List<HabitEntity> {
        val oldLocalMap = mutableMapOf<String, HabitEntity>()
        val newLocalEntities = mutableListOf<HabitEntity>()

        for (localHabit in localHabits) {
            val apiId = localHabit.apiId
            
            if (apiId != null) {
                oldLocalMap[apiId] = localHabit
            } else {
                if (!localHabit.isDeleted) {
                    newLocalEntities.add(localHabit)
                }
            }
        }
        
        val joinedHabits = mutableListOf<HabitEntity>()

        for (apiHabit in apiHabits) {
            if (oldLocalMap.containsKey(apiHabit.id)) {
                val localHabit = oldLocalMap[apiHabit.id]
                
                if (localHabit != null) {
                    if (!localHabit.isDeleted) {
                        joinedHabits.remove(apiHabit)
                        joinedHabits.add(localHabit)
                    } else {
                        joinedHabits.remove(apiHabit)
                    }
                }
            } else {
                joinedHabits.add(apiHabit)
            }
        }
        
        joinedHabits.addAll(newLocalEntities)

        return joinedHabits
    }

    private suspend fun insertHabitsForApi(habits: List<HabitEntity>) {
        for (habit in habits) {
            var isSuccessful = false
            var repeatCount = 0

            while (!isSuccessful && repeatCount < MAX_REPEAT_COUNT) {
                repeatCount++
                try {
                    val response = habitApiService.putHabit(HabitMapper.INSTANCE.entityToDto(habit))
                    if (response.isSuccessful) {
                        val newId = response.body()!!.uid
                        habitDao.deleteItem(habit)
                        habitDao.insertItem(habit.copy(
                            id = newId,
                            apiId = newId
                        ))
                        isSuccessful = true
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }

                if (!isSuccessful)
                    delay(REPEAT_DELAY)
            }
            
            if (!isSuccessful) {
                habitDao.insertItem(habit.copy())
            }
        }
    }

    private suspend fun deleteHabitsForApi(habits: List<HabitEntity>) {
        for (habit in habits) {
            var isSuccessful = false
            var repeatCount = 0

            while (!isSuccessful && repeatCount < MAX_REPEAT_COUNT) {
                repeatCount++
                try {
                    val response = habitApiService.deleteHabit(HabitRequestUID(habit.id))
                    if (response.isSuccessful) {
                        habitDao.deleteItem(habit)
                        isSuccessful = true
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                if (!isSuccessful)
                    delay(REPEAT_DELAY)
            }
        }
    }
}
