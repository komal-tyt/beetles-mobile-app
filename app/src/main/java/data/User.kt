package com.example.beetles_app.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class User(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val firstName: String,
    val lastName: String,
    val gender: String,
    val course: String,
    val dateOfBirth: String,
    val zodiacSign: String,
    val speed: Float,
    val maxCockroaches: Int,
    val bonusInterval: Int,
    val roundDuration: Int
)