package com.example.beetles_app

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun RegistrationScreen(
    modifier: Modifier = Modifier,
    logic: RegistrationScreenLogic = remember { RegistrationScreenLogic() }
) {
    val user = logic.state
    val courses = listOf("1", "2", "3", "4")

    Column(
        modifier = modifier.fillMaxWidth().verticalScroll(rememberScrollState())
    ) {
        Text(
            text = "Registration user",
            fontSize = 25.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.Serif,
            color = Color(0xFFFDFDFD),
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )

        Card(
            modifier = Modifier.fillMaxWidth().padding(25.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF1c242e)),
            shape = RoundedCornerShape(30.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth().padding(top = 25.dp, bottom = 25.dp)
            ) {
                TextField(
                    value = user.firstName,
                    onValueChange = logic::onFirstNameChange,
                    label = { Text("Input the name") },
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 25.dp),
                )

                TextField(
                    value = user.lastName,
                    onValueChange = logic::onLastNameChange,
                    label = { Text("Input the last name") },
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier.fillMaxWidth().padding(start = 25.dp, end = 25.dp, top = 25.dp),
                )

                Row(
                    modifier = Modifier.fillMaxWidth()
                        .padding(start = 25.dp, end = 25.dp, top = 25.dp),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        RadioButton(
                            selected = user.gender == "Man",
                            onClick = { logic.onGenderChange("Man") },
                        )
                        Text(text = "Man", color = Color(0xFFFDFDFD))
                    }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        RadioButton(
                            selected = user.gender == "Woman",
                            onClick = { logic.onGenderChange("Woman") },
                        )
                        Text(text = "Woman", color = Color(0xFFFDFDFD))
                    }
                }

                Box {
                    Button(
                        onClick = logic::openCourseMenu,
                        modifier = Modifier.fillMaxWidth().padding(start = 30.dp, end = 30.dp)
                    ) {
                        Text("Choice the course")
                    }
                    DropdownMenu(
                        expanded = logic.courseMenuExpanded,
                        onDismissRequest = logic::closeCourseMenu,
                    ) {
                        courses.forEach { course ->
                            DropdownMenuItem(
                                text = { Text(course) },
                                onClick = { logic.onCourseChange(course) }
                            )
                        }
                    }
                }

                Text(
                    text = "Select the game difficulty",
                    color = Color(0xFF9BB0C4),
                    fontSize = 14.sp,
                    modifier = Modifier.padding(start = 25.dp, top = 25.dp),
                )
                Slider(
                    value = user.difficulty,
                    onValueChange = { logic.onDifficultyChange(it.toInt().toFloat()) },
                    valueRange = 1f..10f,
                    steps = 8,
                )
                Row(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 25.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = "Easy", color = Color(0xFF9BB0C4), fontSize = 12.sp)
                    Text(text = user.difficulty.toString(), color = Color(0xFFFDFDFD))
                    Text(text = "Hard", color = Color(0xFF9BB0C4), fontSize = 12.sp)
                }

                Text(
                    text = "Date of birth",
                    color = Color(0xFF9BB0C4),
                    modifier = Modifier.padding(start = 25.dp, top = 25.dp),
                    fontSize = 14.sp
                )
                val datePickerState = rememberDatePickerState()
                LaunchedEffect(datePickerState.selectedDateMillis) {
                    val millis = datePickerState.selectedDateMillis
                    if (millis != null) {
                        logic.onDateChange(logic.formatDate(millis))
                    }
                }
                Box(
                    modifier = Modifier.padding(start = 15.dp, end= 15.dp, top = 25.dp).clip(RoundedCornerShape(20.dp)).background(Color(
                        0xFFD6E4F0
                    )
                    )

                ){
                    DatePicker(
                        state = datePickerState,
                        modifier = Modifier.scale(0.8f),
                        colors = DatePickerDefaults.colors(
                            containerColor = Color.Transparent,
                            todayContentColor = Color(0xFF9BB0C4)
                        )
                    )
                }
                Button(
                    onClick = logic::save,
                    modifier = Modifier.fillMaxWidth().padding(start = 30.dp, end = 30.dp, top = 25.dp)
                ) {
                    Text("Save")
                }

                logic.savedUser?.let { u ->
                    Column(modifier = Modifier.fillMaxWidth().padding(25.dp)) {
                        Text(text = "First name: ${u.firstName}", color = Color(0xFFFDFDFD))
                        Text(text = "Last name: ${u.lastName}", color = Color(0xFFFDFDFD))
                        Text(text = "Gender: ${u.gender}", color = Color(0xFFFDFDFD))
                        Text(text = "Course: ${u.course}", color = Color(0xFFFDFDFD))
                        Text(text = "Difficulty: ${u.difficulty}", color = Color(0xFFFDFDFD))
                        Text(text = "Date of birth: ${u.dateOfBirth}", color = Color(0xFFFDFDFD))
                        Text(
                            text = "Zodiac sign: ${u.zodiacSign}",
                            color = Color(0xFFFDFDFD),
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}