package com.example.beetles_app.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Query("SELECT * FROM users ORDER BY firstName ASC")
    fun getAllUsers(): Flow<List<User>>

    @Query("SELECT * FROM users WHERE firstName = :firstName AND lastName = :lastName AND dateOfBirth = :dateOfBirth LIMIT 1")
    suspend fun findUser(firstName: String, lastName: String, dateOfBirth: String): User?

    @Insert
    suspend fun insertUser(user: User)
}