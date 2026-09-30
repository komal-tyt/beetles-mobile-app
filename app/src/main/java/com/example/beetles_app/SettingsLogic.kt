package com.example.beetles_app

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

class SettingsLogic {
    var state by mutableStateOf(GameSettings())
        private set

    fun onSpeedChange(v: Float)      { state = state.copy(speed = v) }
    fun onCockroachesChange(v: Int)  { state = state.copy(maxCockroaches = v) }
    fun onBonusIntervalChange(v: Int){ state = state.copy(bonusInterval = v) }
    fun onRoundDurationChange(v: Int){ state = state.copy(roundDuration = v) }
}

data class GameSettings(
    val speed: Float = 1f,
    val maxCockroaches: Int = 5,
    val bonusInterval: Int = 10,
    val roundDuration: Int = 60
)