package com.example.habittracker.data.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.habittracker.domain.models.HabitEntity

@Dao
interface HabitDao {
    @Query("SELECT * FROM habits WHERE id = :id")
    suspend fun getItemById(id: String): HabitEntity?

    @Query("SELECT * FROM habits WHERE isDeleted = 0")
    fun getAllItems(): LiveData<List<HabitEntity>>

    @Query("SELECT * FROM habits WHERE isDeleted = 1")
    suspend fun getDeletedHabits(): List<HabitEntity>

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateItem(nameEntity: HabitEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertItem(nameEntity: HabitEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHabits(habits: List<HabitEntity>)

    @Delete
    fun deleteItem(nameEntity: HabitEntity)
}
