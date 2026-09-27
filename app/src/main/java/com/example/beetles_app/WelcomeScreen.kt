package com.example.beetles_app

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun WelcomeScreen(
    modifier: Modifier = Modifier,
    onStart: () -> Unit = {}
){
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
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

        Button(
            onClick = onStart,
            modifier = Modifier.fillMaxWidth().padding(top = 48.dp, start = 48.dp, end = 48.dp)
        ) {
            Text(
                text = "Start",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}