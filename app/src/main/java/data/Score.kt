package com.example.beetles_app.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "scores")
data class Score(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val firstName: String,
    val lastName: String,
    val score: Int,
    val speed: Float,
    val hits: Int,
    val misses: Int,
    val date: Long = System.currentTimeMillis()
)