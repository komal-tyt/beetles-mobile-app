package com.example.beetles_app

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.beetles_app.data.AppDatabase
import com.example.beetles_app.data.User
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun RegistrationScreen(
    onBack: () -> Unit,
    onUserCreated: () -> Unit
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val userDao = remember { AppDatabase.getDatabase(context).userDao() }

    val user = RegistrationScreenLogic.state
    val courses = listOf("1", "2", "3", "4")

    Column(
        modifier = Modifier.fillMaxWidth().verticalScroll(rememberScrollState())
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            horizontalArrangement = Arrangement.Start
        ) {
            Button(onClick = {
                RegistrationScreenLogic.clearForm()
                onBack()
            }) {
                Text("Back")
            }
        }

        Text(
            text = "Registration user",
            fontSize = 25.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.Serif,
            color = Color(0xFFFDFDFD),
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth().padding(top = 10.dp)
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
                    modifier = Modifier.fillMaxWidth().padding(start = 25.dp, end = 25.dp, top = 25.dp),
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
                        Text("Choice the course: ${user.course}")
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
                    modifier = Modifier.padding(start = 15.dp, end= 15.dp, top = 25.dp).clip(RoundedCornerShape(20.dp)).background(Color(0xFFD6E4F0))
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

                RegistrationScreenLogic.errorMessage?.let { error ->
                    Text(
                        text = error,
                        color = Color(0xFFFF6B6B),
                        fontSize = 14.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth().padding(top = 16.dp)
                    )
                }

                Button(
                    onClick = {
                        if (RegistrationScreenLogic.isFormFilled()) {
                            scope.launch {
                                val existing = userDao.findUser(user.firstName, user.lastName, user.dateOfBirth)
                                if (existing != null) {
                                    RegistrationScreenLogic.setError("User with these data already exists!")
                                } else {
                                    val s = SettingsLogic.state
                                    val zodiac = getZodiacSign(user.dateOfBirth)
                                    userDao.insertUser(
                                        User(
                                            firstName = user.firstName,
                                            lastName = user.lastName,
                                            gender = user.gender,
                                            course = user.course,
                                            dateOfBirth = user.dateOfBirth,
                                            zodiacSign = zodiac,
                                            speed = s.speed,
                                            maxCockroaches = s.maxCockroaches,
                                            bonusInterval = s.bonusInterval,
                                            roundDuration = s.roundDuration
                                        )
                                    )
                                    RegistrationScreenLogic.clearForm()
                                    onUserCreated()
                                }
                            }
                        } else {
                            RegistrationScreenLogic.setError("Fill all fields!")
                        }
                    },
                    modifier = Modifier.fillMaxWidth().padding(start = 30.dp, end = 30.dp, top = 25.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50))
                ) {
                    Text("Save & Register", fontSize = 16.sp)
                }
            }
        }
    }
}