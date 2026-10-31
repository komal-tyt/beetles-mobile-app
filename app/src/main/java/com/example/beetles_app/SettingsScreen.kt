package com.example.beetles_app

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.beetles_app.data.AppDatabase
import kotlinx.coroutines.launch

@Composable
fun SettingsScreen(modifier: Modifier = Modifier) {
    val s = SettingsLogic.state
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val userDao = remember { AppDatabase.getDatabase(context).userDao() }

    // Вспомогательная функция для сохранения настроек в БД
    fun saveToDatabase(speed: Float, maxCockroaches: Int, bonusInterval: Int, roundDuration: Int) {
        val currentUser = RegistrationScreenLogic.selectedUser
        if (currentUser != null) {
            scope.launch {
                val updatedUser = currentUser.copy(
                    speed = speed,
                    maxCockroaches = maxCockroaches,
                    bonusInterval = bonusInterval,
                    roundDuration = roundDuration
                )
                userDao.updateUser(updatedUser)
                // Обновляем локальную копию, чтобы при повторном выборе настройки не сбросились
                RegistrationScreenLogic.selectedUser = updatedUser
            }
        }
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        Card(
            modifier = Modifier.fillMaxWidth().padding(8.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF1c242e))
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                SettingItem("Game speed", s.speed.toString()) {
                    Slider(
                        value = s.speed,
                        onValueChange = {
                            SettingsLogic.onSpeedChange(it)
                            saveToDatabase(it, s.maxCockroaches, s.bonusInterval, s.roundDuration)
                        },
                        valueRange = 0.5f..3f,
                        steps = 4
                    )
                }
                SettingItem("Max. beetles", s.maxCockroaches.toString()) {
                    Slider(
                        value = s.maxCockroaches.toFloat(),
                        onValueChange = {
                            val newValue = it.toInt()
                            SettingsLogic.onCockroachesChange(newValue)
                            saveToDatabase(s.speed, newValue, s.bonusInterval, s.roundDuration)
                        },
                        valueRange = 1f..20f,
                        steps = 18
                    )
                }
                SettingItem("Bonus interval (sec)", s.bonusInterval.toString()) {
                    Slider(
                        value = s.bonusInterval.toFloat(),
                        onValueChange = {
                            val newValue = it.toInt()
                            SettingsLogic.onBonusIntervalChange(newValue)
                            saveToDatabase(s.speed, s.maxCockroaches, newValue, s.roundDuration)
                        },
                        valueRange = 5f..60f,
                        steps = 10
                    )
                }
                SettingItem("Round duration (sec)", s.roundDuration.toString()) {
                    Slider(
                        value = s.roundDuration.toFloat(),
                        onValueChange = {
                            val newValue = it.toInt()
                            SettingsLogic.onRoundDurationChange(newValue)
                            saveToDatabase(s.speed, s.maxCockroaches, s.bonusInterval, newValue)
                        },
                        valueRange = 30f..180f,
                        steps = 14
                    )
                }
            }
        }
    }
}

@Composable
private fun SettingItem(
    title: String,
    value: String,
    content: @Composable () -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)) {
        Text(
            text = "$title: $value",
            color = Color(0xFFFDFDFD),
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium
        )
        content()
    }
}