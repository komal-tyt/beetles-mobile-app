package com.example.beetles_app

import androidx.compose.ui.graphics.Color

enum class BugType(
    val defaultSpeed: Float,
    val defaultSize: Float,
    val score: Int,
    val color: Color,
    val imageRes: Int
) {
    NORMAL(
        defaultSpeed = 0.001f,
        defaultSize = 0.15f,
        score = 5,
        color = Color(0xFF6B8E23),
        imageRes = R.drawable.bug_normal
    ),
    FAST(
        defaultSpeed = 0.003f,
        defaultSize = 0.1f,
        score = 15,
        color = Color(0xFFFFA500),
        imageRes = R.drawable.bug_fast
    ),
    RARE(
        defaultSpeed = 0.0035f,
        defaultSize = 0.12f,
        score = 10,
        color = Color(0xFF9932CC),
        imageRes = R.drawable.bug_rare
    )
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