package com.example.beetles_app

enum class BugType(
    val defaultSpeed: Float,
    val defaultSize: Float,
    val score: Int
) {
    NORMAL(defaultSpeed = 0.15f, defaultSize = 0.10f, score = 1),
    FAST(defaultSpeed = 0.30f, defaultSize = 0.07f, score = 2),
    RARE(defaultSpeed = 0.08f, defaultSize = 0.14f, score = 67)
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