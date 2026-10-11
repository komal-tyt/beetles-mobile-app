package com.example.beetles_app

import androidx.compose.ui.graphics.Color

enum class BugType(
    val defaultSpeed: Float,
    val defaultSize: Float,
    val score: Int,
    val color: Color
) {
    NORMAL(defaultSpeed = 0.005f, defaultSize = 0.05f, score = 1, color = Color(0xFF6B8E23)),
    FAST(defaultSpeed = 0.01f, defaultSize = 0.03f, score = 2, color = Color(0xFFFFA500)),
    RARE(defaultSpeed = 0.001f, defaultSize = 0.07f, score = 67, color = Color(0xFF9932CC))
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