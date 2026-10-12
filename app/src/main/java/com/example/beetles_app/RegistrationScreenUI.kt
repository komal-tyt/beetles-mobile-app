package com.example.beetles_app

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.setValue
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.runtime.getValue
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

@Composable
fun RegistrationScreen(
    modifier: Modifier = Modifier,
    onPlay: () -> Unit = {}
) {
    val user = RegistrationScreenLogic.state
    val courses = listOf("1", "2", "3", "4")
    var showError by remember { mutableStateOf(false) }
    LaunchedEffect(showError) {
        if (showError) {
            delay(2000)
            showError = false
        }
    }

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
            modifier = Modifier.fillMaxWidth().padding(top = 25.dp)
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
                    onValueChange = RegistrationScreenLogic::onFirstNameChange,
                    label = { Text("Input the name") },
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 25.dp),
                )

                TextField(
                    value = user.lastName,
                    onValueChange = RegistrationScreenLogic::onLastNameChange,
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
                            onClick = { RegistrationScreenLogic.onGenderChange("Man") },
                        )
                        Text(text = "Man", color = Color(0xFFFDFDFD))
                    }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        RadioButton(
                            selected = user.gender == "Woman",
                            onClick = { RegistrationScreenLogic.onGenderChange("Woman") },
                        )
                        Text(text = "Woman", color = Color(0xFFFDFDFD))
                    }
                }

                Box {
                    Button(
                        onClick = RegistrationScreenLogic::openCourseMenu,
                        modifier = Modifier.fillMaxWidth().padding(start = 30.dp, end = 30.dp)
                    ) {
                        Text("Choice the course")
                    }
                    DropdownMenu(
                        expanded = RegistrationScreenLogic.courseMenuExpanded,
                        onDismissRequest = RegistrationScreenLogic::closeCourseMenu,
                    ) {
                        courses.forEach { course ->
                            DropdownMenuItem(
                                text = { Text(course) },
                                onClick = { RegistrationScreenLogic.onCourseChange(course) }
                            )
                        }
                    }
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
                        RegistrationScreenLogic.onDateChange(RegistrationScreenLogic.formatDate(millis))
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
                            containerColor = Color(0xFFD6E4F0),
                            todayContentColor = Color(0xFF9BB0C4)
                        )
                    )
                }
                Button(
                    onClick = {
                        if (RegistrationScreenLogic.isFormFilled()) {
                            RegistrationScreenLogic.save()
                            showError = false
                        } else {
                            showError = true
                        }
                    },
                    modifier = Modifier.fillMaxWidth().padding(start = 30.dp, end = 30.dp, top = 25.dp)
                ) {
                    Text("Save")
                }
                RegistrationScreenLogic.savedUser?.let { user ->
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(25.dp)
                    ) {
                        Text(text = "First name: ${user.firstName}", color = Color(0xFFFDFDFD))
                        Text(text = "Last name: ${user.lastName}", color = Color(0xFFFDFDFD))
                        Text(text = "Gender: ${user.gender}", color = Color(0xFFFDFDFD))
                        Text(text = "Course: ${user.course}", color = Color(0xFFFDFDFD))
                        Text(text = "Date of birth: ${user.dateOfBirth}", color = Color(0xFFFDFDFD))
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = getZodiacSymbol(user.zodiacSign),
                                fontSize = 40.sp,
                                color = Color(0xFFFDFDFD)
                            )
                            Text(
                                text = "  Zodiac sign: ${user.zodiacSign}",
                                color = Color(0xFFFDFDFD),
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
                if (showError) {
                    Text(
                        text = "Fill all fields!",
                        color = Color(0xFFFF6B6B),
                        fontSize = 14.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth().padding(top = 16.dp)
                    )
                }
                Button(
                    onClick = {
                        if (RegistrationScreenLogic.canPlay()) {
                            onPlay()
                        } else {
                            showError = true
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 30.dp, end = 30.dp, top = 25.dp, bottom = 25.dp)
                ) {
                    Text(
                        text = "Play",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}