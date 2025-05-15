package com.example.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.domain.models.HabitStatus
import kotlinx.coroutines.flow.Flow

@Dao
interface HabitDao {
    @Query("SELECT * FROM habits WHERE id = :id")
    suspend fun getHabitById(id: String): HabitEntity?

    @Query("SELECT * FROM habits")
    fun getAllHabits(): Flow<List<HabitEntity>>

    @Query("SELECT * FROM habits")
    suspend fun getListAllHabits(): List<HabitEntity>

    @Query("SELECT * FROM habits WHERE habitStatus = :status")
    suspend fun getHabitsByStatus(status: HabitStatus): List<HabitEntity>

    @Query("SELECT * FROM habits WHERE isDoneMarksSynced = 0")
    suspend fun getHabitsToSyncDoneMarks(): List<HabitEntity>

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateHabit(nameEntity: HabitEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHabit(nameEntity: HabitEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHabits(habits: List<HabitEntity>)

    @Delete
    fun deleteHabit(nameEntity: HabitEntity)
}
