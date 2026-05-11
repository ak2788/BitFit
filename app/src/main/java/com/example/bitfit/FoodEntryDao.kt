package com.example.bitfit

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface FoodEntryDao {

    @Insert
    fun insert(entry: FoodEntry) //suspend fun insert(entry: FoodEntry)

    @Query("SELECT * FROM food_entries ORDER BY id DESC")
    fun getAllEntries(): Flow<List<FoodEntry>> //suspend fun getAllEntries(): List<FoodEntry>
}