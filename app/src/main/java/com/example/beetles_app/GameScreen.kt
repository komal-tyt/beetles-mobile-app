package com.example.beetles_app

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

@Composable
fun GameScreen(
    modifier: Modifier = Modifier,
    onExit: () -> Unit = {}
) {
    val roundDuration = SettingsLogic.state.roundDuration

    var timeLeft by remember { mutableStateOf(roundDuration) }
    var showDialog by remember { mutableStateOf(false) }
    var roundKey by remember { mutableStateOf(0) }

    LaunchedEffect(roundKey) {
        timeLeft = roundDuration
        showDialog = false
        while (timeLeft > 0) {
            delay(1000)
            timeLeft -= 1
        }
        showDialog = true
    }

    Box(
        modifier = modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "Beetles",
                fontSize = 48.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Serif,
                color = Color(0xFFFDFDFD),
                textAlign = TextAlign.Center
            )
            Text(
                text = "apocalypse",
                fontSize = 18.sp,
                color = Color(0xFF9BB0C4),
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 3.dp)
            )
            Text(
                text = "Left: $timeLeft sec",
                fontSize = 20.sp,
                color = Color(0xFFFDFDFD),
                fontFamily = FontFamily.Serif,
                modifier = Modifier.padding(top = 24.dp),
                textAlign = TextAlign.Center
            )
        }
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { },
            title = {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Round over",
                        textAlign = TextAlign.Center
                    )
                }
            },
            confirmButton = {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    Button(onClick = onExit) {
                        Text("Exit")
                    }
                    Button(onClick = { roundKey += 1 }) {
                        Text("Repeat")
                    }
                }
            }
        )
    }
}