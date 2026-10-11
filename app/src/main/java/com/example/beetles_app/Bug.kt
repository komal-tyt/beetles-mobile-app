package com.example.beetles_app

import androidx.compose.ui.graphics.Color

enum class BugType(
    val defaultSpeed: Float,
    val defaultSize: Float,
    val score: Int,
    val color: Color
) {
    NORMAL(defaultSpeed = 0.007f, defaultSize = 0.15f, score = 5, color = Color(0xFF6B8E23)),
    FAST(defaultSpeed = 0.01f, defaultSize = 0.09f, score = 15, color = Color(0xFFFFA500)),
    RARE(defaultSpeed = 0.003f, defaultSize = 0.12f, score = 10, color = Color(0xFF9932CC))
}

data class Bug(
    val id: Long,
    val x: Float,
    val y: Float,
    val vx: Float,
    val vy: Float,
    val size: Float,
    val type: BugType
)