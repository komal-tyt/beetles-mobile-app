package com.example.beetles_app

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
    modifier: Modifier = Modifier
){
    val tabs = listOf("Sign up", "Settings", "Rules", "Authors")
    var selectedTab by remember { mutableStateOf(10) }

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
    }
    Column(
        modifier = modifier.fillMaxSize(),
    ) {
        TabRow(
            selectedTabIndex = selectedTab,
            containerColor = Color.Transparent,
            contentColor = Color(0xFF9BB0C4),
            modifier = Modifier.padding(top = 50.dp)
        ) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTab == index,
                    onClick = { selectedTab = index },
                    text = { Text(title, fontSize = 20.sp) }
                )
            }
        }
        Box(
            modifier = Modifier.weight(1f).fillMaxWidth()
        ) {
            when (selectedTab) {
                0 -> RegistrationScreen()
                1 -> SettingsScreen()
                2 -> RulesScreen()
                //3 -> AuthorsScreen()
            }
        }
    }
}