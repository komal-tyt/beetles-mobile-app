package com.example.beetles_app

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class GameViewModel : ViewModel() {
    var score by mutableStateOf(0)
        private set
    var hits by mutableStateOf(0)
        private set
    var misses by mutableStateOf(0)
        private set
    var timeLeft by mutableStateOf(SettingsLogic.state.roundDuration)
        private set

    private var timerJob: Job? = null

    init {
        startTimer()
    }

    private fun startTimer() {
        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            while (timeLeft > 0) {
                delay(1000)
                timeLeft--
            }
        }
    }

    fun addScore(points: Int) {
        score += points
        hits++
    }

    fun addMiss() {
        misses++
    }

    fun restart() {
        score = 0
        hits = 0
        misses = 0
        timeLeft = SettingsLogic.state.roundDuration
        startTimer()
    }
}