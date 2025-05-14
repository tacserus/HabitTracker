package com.example.habittracker.data

import android.util.Log
import com.example.habittracker.data.api.HabitApiService
import com.example.habittracker.data.database.HabitDao
import com.example.habittracker.domain.HabitMapper
import com.example.habittracker.domain.enums.HabitStatus
import com.example.habittracker.domain.models.HabitDoneMark
import com.example.habittracker.domain.models.HabitEntity
import com.example.habittracker.domain.models.HabitRequestUID
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class HabitRepository(
    private val habitDao: HabitDao,
    private val habitApiService: HabitApiService
) {
    suspend fun getHabitById(id: String): HabitEntity? {
        return habitDao.getHabitById(id)
    }

    fun getAllHabits(): Flow<List<HabitEntity>> {
        return habitDao.getAllHabits()
            .map { habits -> habits.filter { it.habitStatus != HabitStatus.DELETE } }
    }

    suspend fun getListAllHabits(): List<HabitEntity> {
        return habitDao.getListAllHabits()
    }

    suspend fun updateHabit(habit: HabitEntity) {
        habitDao.updateHabit(habit)
    }

    suspend fun addHabit(habit: HabitEntity) {
        habitDao.insertHabit(habit)
    }

    suspend fun deleteHabit(habit: HabitEntity) {
        habitDao.insertHabit(habit.copy(habitStatus = HabitStatus.DELETE))
    }

    suspend fun updateDoneMark(id: String) {
        val updatedHabit = habitDao.getHabitById(id)

        if (updatedHabit != null) {
            val date = System.currentTimeMillis()
            val newDoneMarks = updatedHabit.doneMarks.plus(date)
            updateHabit(updatedHabit.copy(doneMarks = newDoneMarks, isDoneMarksSynced = false))
        }
    }

    suspend fun syncHabits() {
        Log.d("Sync", "Starting habit synchronization...")

        processLocalCreations()
        processLocalUpdates()
        processLocalDeletions()
        processDoneMarks()

        fetchAndMergeServerData()

        Log.d("Sync", "Habit synchronization finished.")
    }

    private suspend fun processLocalCreations() {
        val habitsToCreate = habitDao.getHabitsByStatus(HabitStatus.ADD)
        if (habitsToCreate.isEmpty()) return
        Log.d("Sync", "Processing ${habitsToCreate.size} local creations...")

        for (localHabit in habitsToCreate) {
            try {
                val requestDto = HabitMapper.INSTANCE.entityToDto(localHabit).copy(uid = null)
                val response = habitApiService.putHabit(requestDto)

                if (response.isSuccessful && response.body() != null) {
                    val createdDto = response.body()!!
                    val updatedEntity = localHabit.copy(
                        apiId = createdDto.uid,
                        habitStatus = HabitStatus.SYNCED
                    )
                    habitDao.updateHabit(updatedEntity)
                    Log.i("Sync", "Successfully created habit on server: ${updatedEntity.title} (API ID: ${updatedEntity.apiId})")
                } else {
                    Log.e("Sync", "Failed to create habit '${localHabit.title}' on server. Code: ${response.code()}. Message: ${response.message()}")
                }
            } catch (e: Exception) {
                Log.e("Sync", "Exception creating habit '${localHabit.title}' on server: ${e.message}", e)
            }
        }
    }

    private suspend fun processLocalUpdates() {
        val habitsToUpdate = habitDao.getHabitsByStatus(HabitStatus.UPDATE)
        if (habitsToUpdate.isEmpty()) return
        Log.d("Sync", "Processing ${habitsToUpdate.size} local updates...")

        for (localHabit in habitsToUpdate) {
            if (localHabit.apiId == null) {
                Log.w("Sync", "Skipping update for habit '${localHabit.title}' as it has no apiId. Attempting to treat as new.")
                continue
            }
            try {
                val requestDto = HabitMapper.INSTANCE.entityToDto(localHabit)
                val response = habitApiService.putHabit(requestDto)


                if (response.isSuccessful) {
                    val serverResponseDto = response.body()
                    val updatedEntity = if (serverResponseDto != null) {
                        localHabit.copy(apiId = serverResponseDto.uid, habitStatus = HabitStatus.SYNCED)
                    } else {
                        localHabit.copy(habitStatus = HabitStatus.SYNCED)
                    }

                    habitDao.updateHabit(updatedEntity)
                    Log.i("Sync", "Successfully updated habit on server: ${updatedEntity.title}")
                } else {
                    Log.e("Sync", "Failed to update habit '${localHabit.title}' on server. Code: ${response.code()}. Message: ${response.message()}")
                }
            } catch (e: Exception) {
                Log.e("Sync", "Exception updating habit '${localHabit.title}' on server: ${e.message}", e)
            }
        }
    }

    private suspend fun processLocalDeletions() {
        val habitsToDelete = habitDao.getHabitsByStatus(HabitStatus.DELETE)
        if (habitsToDelete.isEmpty()) return
        Log.d("Sync", "Processing ${habitsToDelete.size} local deletions...")

        for (localHabit in habitsToDelete) {
            if (localHabit.apiId == null) {
                Log.i("Sync", "Deleting local-only habit (never synced): ${localHabit.title}")
                habitDao.deleteHabit(localHabit)
                continue
            }
            try {
                val response = habitApiService.deleteHabit(HabitRequestUID(localHabit.apiId))

                if (response.isSuccessful) {
                    habitDao.deleteHabit(localHabit)
                    Log.i("Sync", "Successfully deleted habit from server and local: ${localHabit.title}")
                } else {
                    Log.e("Sync", "Failed to delete habit '${localHabit.title}' from server. Code: ${response.code()}. Message: ${response.message()}")
                }
            } catch (e: Exception) {
                Log.e("Sync", "Exception deleting habit '${localHabit.title}' from server: ${e.message}", e)
            }
        }
    }

    private suspend fun processDoneMarks() {
        val habitsToSyncDoneMarks = habitDao.getHabitsToSyncDoneMarks()
        if (habitsToSyncDoneMarks.isEmpty()) return
        Log.d("Sync", "Processing ${habitsToSyncDoneMarks.size} to sync done marks...")

        for (habitToSyncDoneMarks in habitsToSyncDoneMarks) {
            if (habitToSyncDoneMarks.apiId == null) {
                Log.i("Sync", "Done Marks for habitLocalId ${habitToSyncDoneMarks.id} are not synced")
                continue
            }
            var isSuccessful = true
            for (doneMark in habitToSyncDoneMarks.doneMarks) {
                try {
                    Log.i("Sync", "${(doneMark / 1000).toInt()} ${habitToSyncDoneMarks.apiId}")
                    val response = habitApiService.addDoneMark(HabitDoneMark(
                        doneMark / 1000,
                        habitToSyncDoneMarks.apiId
                    ))

                    if (response.isSuccessful) {
                        Log.i("Sync", "Done Marks $doneMark for habitApiId ${habitToSyncDoneMarks.apiId} are successful synced")
                    } else {
                        isSuccessful = false
                        Log.e("Sync", "Done Mark $doneMark for habitApiId ${habitToSyncDoneMarks.apiId} are not synced. Code: ${response.code()}. Message: ${response.message()}")
                    }
                } catch (e: Exception) {
                    isSuccessful = false
                    Log.e("Sync", "Exception sync habit don marks for habitApiId ${habitToSyncDoneMarks.apiId} from server: ${e.message}", e)
                }
            }
            if (isSuccessful) {
                habitDao.insertHabit(habitToSyncDoneMarks.copy(isDoneMarksSynced = true))
            }
        }
    }

    private suspend fun fetchAndMergeServerData() {
        Log.d("Sync", "Fetching and merging server data...")
        try {
            val serverResponse = habitApiService.getHabits()
            if (!serverResponse.isSuccessful || serverResponse.body() == null) {
                Log.e("Sync", "Failed to fetch habits from server. Code: ${serverResponse.code()}. Message: ${serverResponse.message()}")
                return
            }

            val serverHabitDtos = serverResponse.body()!!
            Log.i("Sync", "Fetched ${serverHabitDtos.size} habits from server.")


            val localHabits = habitDao.getListAllHabits()

            val localHabitsMapByApiId = localHabits
                .filter { it.apiId != null }
                .associateBy { it.apiId }

            val serverApiIds = serverHabitDtos.mapNotNull { it.uid }.toSet()

            for (serverDto in serverHabitDtos) {
                if (serverDto.uid == null) {
                    Log.w("Sync", "Server DTO with null UID found: ${serverDto.title}")
                    continue
                }

                Log.i("Sync", "apiid ${serverDto.uid} server habit.")

                val existingLocalHabit = localHabitsMapByApiId[serverDto.uid]

                if (existingLocalHabit == null) {
                    Log.i("Sync", "New habit from server: ${serverDto.title}. Adding locally.")
                    val newEntity = HabitMapper.INSTANCE.dtoToEntity(serverDto)
                    habitDao.insertHabit(newEntity)
                } else {
                    if (existingLocalHabit.habitStatus == HabitStatus.SYNCED) {
                        val serverMappedEntity = HabitMapper.INSTANCE.dtoToEntity(serverDto, existingLocalHabit.id)
                        habitDao.updateHabit(serverMappedEntity)
                    } else {
                        Log.i("Sync", "Local habit '${existingLocalHabit.title}' has pending changes (${existingLocalHabit.habitStatus}). Skipping update from server for now.")
                    }
                }
            }

            for (localHabit in localHabits) {
                if (localHabit.apiId != null && !serverApiIds.contains(localHabit.apiId) &&
                    localHabit.habitStatus != HabitStatus.ADD && localHabit.habitStatus != HabitStatus.DELETE) {
                    Log.i("Sync", "Deleting local habit '${localHabit.title}' as it's no longer on server.")
                    habitDao.deleteHabit(localHabit)
                }
            }
        } catch (e: Exception) {
            Log.e("Sync", "Exception during fetchAndMergeServerData: ${e.message}", e)
        }
    }
}
