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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun SettingsScreen(
    modifier: Modifier = Modifier,
    logic: SettingsLogic = remember { SettingsLogic() }
) {
    val s = logic.state

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
                        onValueChange = logic::onSpeedChange,
                        valueRange = 0.5f..3f,
                        steps = 4
                    )
                }
                SettingItem("Max. beetles", s.maxCockroaches.toString()) {
                    Slider(
                        value = s.maxCockroaches.toFloat(),
                        onValueChange = { logic.onCockroachesChange(it.toInt()) },
                        valueRange = 1f..20f,
                        steps = 18
                    )
                }
                SettingItem("Bonus interval (sec)", s.bonusInterval.toString()) {
                    Slider(
                        value = s.bonusInterval.toFloat(),
                        onValueChange = { logic.onBonusIntervalChange(it.toInt()) },
                        valueRange = 5f..60f,
                        steps = 10
                    )
                }
                SettingItem("Round duration (sec)", s.roundDuration.toString()) {
                    Slider(
                        value = s.roundDuration.toFloat(),
                        onValueChange = { logic.onRoundDurationChange(it.toInt()) },
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