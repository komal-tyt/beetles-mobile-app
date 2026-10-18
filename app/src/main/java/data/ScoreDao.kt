package com.example.beetles_app.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.beetles_app.data.Score
import kotlinx.coroutines.flow.Flow

@Dao
interface ScoreDao {

    @Insert
    suspend fun insert(score: Score)

    @Query("SELECT * FROM scores ORDER BY score DESC")
    fun getAllScores(): Flow<List<Score>>

    @Query("DELETE FROM scores")
    suspend fun deleteAll()

    @Query("""
        SELECT * FROM scores 
        WHERE firstName = :firstName 
        AND (lastName = :lastName OR (:lastName = '' AND lastName = ''))
        LIMIT 1
    """)
    suspend fun getByName(firstName: String, lastName: String): Score?

    @Query("""
        DELETE FROM scores 
        WHERE firstName = :firstName 
        AND (lastName = :lastName OR (:lastName = '' AND lastName = ''))
    """)
    suspend fun deleteByName(firstName: String, lastName: String)
}